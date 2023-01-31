package com.wcc.event.cluster;

import com.wcc.core.Payload;
import com.wcc.core.codec.Codec;
import com.wcc.core.codec.Codecs;
import com.wcc.event.*;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.ReactiveRedisConnectionFactory;
import org.springframework.data.redis.core.ReactiveRedisOperations;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.RedisSerializer;
import reactor.core.Disposable;
import reactor.core.Disposables;
import reactor.core.publisher.EmitterProcessor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.FluxSink;
import reactor.core.publisher.Mono;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
public abstract class AbstractClusterEventBroker implements EventBroker {

    // redis 客户端，用于事件通知
    protected final ReactiveRedisOperations<String, byte[]> redis;

    //事件ID
    private final String id;
    // 集群节点建立的连接通道
    private final EmitterProcessor<EventConnection> processor = EmitterProcessor.create(false);
    private final FluxSink<EventConnection> sink = processor.sink(FluxSink.OverflowStrategy.BUFFER);
    // 集群连接map
    private final Map<String, ClusterConnecting> connections = new ConcurrentHashMap<>();
    protected final ClusterManager clusterManager;

    private boolean started = false;

    protected final Codec<Subscription> subscriptionCodec = Codecs.lookup(Subscription.class);

    protected final Disposable.Composite disposable = Disposables.composite();

    public AbstractClusterEventBroker(ClusterManager clusterManager,
                                      ReactiveRedisConnectionFactory factory) {
        this.id = clusterManager.getClusterName();
        this.redis = new ReactiveRedisTemplate<>(factory, RedisSerializationContext.<String, byte[]>newSerializationContext()
                .key(RedisSerializer.string())
                .hashKey(RedisSerializer.string())
                .value(RedisSerializer.byteArray())
                .hashValue(RedisSerializer.byteArray())
                .build());
        this.clusterManager = clusterManager;
        startup();
    }

    public void startup() {
        if (started) {
            return;
        }
        started = true;

        // 建立集群连接，redis通过订阅其他节点的消息队列topic
        clusterManager
                .getHaManager()
                .getAllNode()
                .forEach(node -> {
                    if (!node.getId().equals(clusterManager.getCurrentServerId())) {
                        handleServerNodeJoin(node);
                        handleRemoteConnection(clusterManager.getCurrentServerId(), node.getId());
                    }
                });
        // 新增节点上线处理
        disposable.add(clusterManager.getHaManager()
                .subscribeServerOnline()
                .subscribe(node -> {
                    handleServerNodeJoin(node);
                    handleRemoteConnection(clusterManager.getCurrentServerId(), node.getId());
                }));
        // 节点下限处理
        disposable.add(clusterManager.getHaManager()
                .subscribeServerOffline()
                .subscribe(this::handleServerNodeLeave));
    }

    public void shutdown() {
        for (ClusterConnecting value : connections.values()) {
            value.disposable.dispose();
        }
        disposable.dispose();
    }

    protected void handleServerNodeJoin(ServerNode node) {

    }

    protected void handleServerNodeLeave(ServerNode node) {

    }

    protected void handleRemoteConnection(String localId, String remoteId) {
        connections
                .computeIfAbsent(remoteId, _id -> {
                    log.debug("handle redis connection:{}", remoteId);
                    ClusterConnecting connection = new ClusterConnecting(localId, _id);
                    sink.next(onConnectionCreated(connection));
                    return connection;
                });
    }

    protected ClusterConnecting onConnectionCreated(ClusterConnecting clusterConnecting) {
        return clusterConnecting;
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public Flux<EventConnection> accept() {
        return Flux.concat(Flux.fromIterable(connections.values()), processor).distinct();
    }

    protected abstract Flux<TopicPayload> listen(String localId, String brokerId);


    protected abstract Mono<Void> dispatch(String localId, String brokerId, TopicPayload payload);


    class ClusterConnecting implements EventProducer, EventConsumer {

        @Getter
        private final String brokerId;
        private final String localId;

        private final EmitterProcessor<TopicPayload> processor = EmitterProcessor.create(Integer.MAX_VALUE, false);
        private final FluxSink<TopicPayload> input = processor.sink(FluxSink.OverflowStrategy.BUFFER);

        FluxSink<TopicPayload> output;

        EmitterProcessor<Subscription> subProcessor = EmitterProcessor.create(Integer.MAX_VALUE, false);
        FluxSink<Subscription> subSink = subProcessor.sink(FluxSink.OverflowStrategy.BUFFER);

        EmitterProcessor<Subscription> unsubProcessor = EmitterProcessor.create(Integer.MAX_VALUE, false);
        FluxSink<Subscription> unsubSink = unsubProcessor.sink(FluxSink.OverflowStrategy.BUFFER);

        Composite disposable = Disposables.composite();

        private final String allSubsInfoKey;

        public ClusterConnecting(String localId, String brokerId) {
            this.brokerId = brokerId;
            this.localId = localId;
            //本地->其他节点的订阅信息
            allSubsInfoKey = "/broker/" + localId + "/" + brokerId + "/subs";

            disposable.add(subProcessor::onComplete);
            disposable.add(unsubProcessor::onComplete);
            disposable.add(processor::onComplete);

            disposable.add(listen(localId, brokerId)
                    .doOnNext(msg -> {
                        if (!processor.hasDownstreams()) {
                            msg.release();
                            return;
                        }
                        log.trace("{} handle cluster [{}] event {}", localId, brokerId, msg.getTopic());
                        input.next(msg);
                    })
                    .onErrorContinue((err, res) -> log.error(err.getMessage(), err))
                    .subscribe());


            disposable.add(redis
                    .listenToPattern("/broker/" + brokerId + "/" + localId + "/*")
                    .subscribe(msg -> {
                        Subscription subscription = Payload
                                .of(msg.getMessage())
                                .decode(subscriptionCodec);
                        if (subscription != null) {
                            if (msg.getChannel().endsWith("unsub") && unsubProcessor.hasDownstreams()) {
                                unsubSink.next(subscription);
                                return;
                            }
                            if (msg.getChannel().endsWith("sub") && subProcessor.hasDownstreams()) {
                                subSink.next(subscription);
                                return;
                            }
                        }
                    }));

            //加载其他节点订阅的信息
            String loadSubsInfoKey = "/broker/" + brokerId + "/" + localId + "/subs";
            disposable.add(redis
                    .opsForSet()
                    .members(loadSubsInfoKey)
                    .doOnNext(msg -> {
                        Subscription subscription = Payload.of(msg).decode(subscriptionCodec);
                        subSink.next(subscription);
                    })
                    .onErrorContinue((err, v) -> log.warn(err.getMessage(), err))
                    .subscribe());

            disposable.add(Flux.<TopicPayload>create(sink -> this.output = sink)
                    .flatMap(payload -> dispatch(localId, brokerId, payload)
                            .onErrorResume((err) -> {
                                log.error(err.getMessage(), err);
                                return Mono.empty();
                            }))
                    .onErrorContinue((err, res) -> log.error(err.getMessage(), err))
                    .subscribe());
        }

        /**
         * 发送订阅请求
         *
         * @param subscription 订阅请求
         */
        @Override
        public Mono<Void> subscribe(Subscription subscription) {
            byte[] sub = subscriptionCodec.encode(subscription).getBytes(true);
            String topic = "/broker/" + localId + "/" + brokerId + "/sub";

            return redis
                    .opsForSet()
                    .add(allSubsInfoKey, sub)
                    .then(redis.convertAndSend(topic, sub))
                    .then();
        }

        /**
         * 发送取消订阅请求
         *
         * @param subscription 订阅请求
         */
        @Override
        public Mono<Void> unsubscribe(Subscription subscription) {
            byte[] sub = subscriptionCodec.encode(subscription).getBytes(true);
            String topic = "/broker/" + localId + "/" + brokerId + "/unsub";
            return redis
                    .opsForSet()
                    .remove(allSubsInfoKey, new Object[]{sub})
                    .then(redis.convertAndSend(topic, sub))
                    .then();
        }

        /**
         * 从生产者订阅消息
         *
         * @return 消息流
         */
        @Override
        public Flux<TopicPayload> subscribe() {
            return processor;
        }

        /**
         * 使用集群ID作为集群连接ID
         *
         * @return brokerId
         */
        @Override
        public String getId() {
            return brokerId;
        }

        /**
         * 判断集群连接是否存活，默认返回true
         *
         * @return  true
         */
        @Override
        public boolean isAlive() {
            return true;
        }

        @Override
        public void doOnDispose(Disposable disposable) {
            this.disposable.add(disposable);
        }

        /**
         * @return 事件代理
         */
        @Override
        public EventBroker getBroker() {
            return AbstractClusterEventBroker.this;
        }

        /**
         * 默认订阅其他broker的消息
         */
        @Override
        public ConnectionFeature[] features() {
            return new ConnectionFeature[]{ConnectionFeature.consumeAnotherBroker};
        }

        /**
         * @return 消息订阅流
         */
        @Override
        public Flux<Subscription> handleSubscribe() {
            return subProcessor;
        }

        /**
         * @return 取消消息取消的流
         */
        @Override
        public Flux<Subscription> handleUnSubscribe() {
            return unsubProcessor;
        }

        @Override
        public FluxSink<TopicPayload> sink() {
            return output;
        }

        @Override
        public void dispose() {
            disposable.dispose();
        }

        @Override
        public boolean isDisposed() {
            return false;
        }
    }
}
