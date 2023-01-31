package com.wcc.event;

import reactor.core.Disposable;
import reactor.core.publisher.Mono;

/**
 * 事件连接
 */
public interface EventConnection extends Disposable{
    String getId();

    boolean isAlive();

    void doOnDispose(Disposable disposable);

    EventBroker getBroker();

    default ConnectionFeature[] features() {
        return new ConnectionFeature[0];
    }

    /**
     * @return 是否为事件生产者
     */
    default boolean isProducer() {
        return this instanceof EventProducer;
    }

    /**
     * @return 是否为事件消费者
     */
    default boolean isConsumer() {
        return this instanceof EventConsumer;
    }

    /**
     * @return 转为事件生产者
     */
    default Mono<EventProducer> asProducer() {
        return isProducer() ? Mono.just(this).cast(EventProducer.class) : Mono.empty();
    }

    /**
     * @return 转为事件消费者
     */
    default Mono<EventConsumer> asConsumer() {
        return isConsumer() ? Mono.just(this).cast(EventConsumer.class) : Mono.empty();
    }
}
