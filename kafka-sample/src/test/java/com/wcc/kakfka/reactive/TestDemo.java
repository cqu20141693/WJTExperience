package com.wcc.kakfka.reactive;


import com.wcc.kafka.domain.KafkaConstants;
import com.wcc.kafka.domain.Message;
import com.wcc.kafka.domain.SimpleMessage;
import io.netty.buffer.Unpooled;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.RandomUtils;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.apache.kafka.common.serialization.ByteArrayDeserializer;
import org.apache.kafka.common.serialization.ByteBufferSerializer;
import org.junit.jupiter.api.Test;
import org.reactivestreams.Publisher;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.kafka.receiver.KafkaReceiver;
import reactor.kafka.receiver.ReceiverOptions;
import reactor.kafka.sender.KafkaSender;
import reactor.kafka.sender.SenderOptions;
import reactor.kafka.sender.SenderRecord;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.concurrent.CountDownLatch;
import java.util.function.Function;

@Slf4j
public class TestDemo {

    private KafkaReceiver<byte[], byte[]> receiver;
    private KafkaSender<ByteBuffer, ByteBuffer> sender;
    @Test
    public void testConsumer() throws InterruptedException {
        CountDownLatch countDownLatch = new CountDownLatch(1);
        KafkaProperties kafkaProperties = new KafkaProperties();
        String server = "10.113.75.55:9092";
        kafkaProperties.setBootstrapServers(Arrays.asList(server.split(",")));
        kafkaProperties.getConsumer().setClientId("cc-test-1");
        kafkaProperties.getConsumer().setGroupId("cc-iot-reactor");

        List<String> topics = Collections.singletonList(KafkaConstants.QUICK_EVENT_TOPIC);
        initConsumer(kafkaProperties, topics);
        receiver.receiveAutoAck()
                .flatMap(Function.identity())
                .<Message>map(record -> SimpleMessage.of(record.topic(),
                        record.key() == null ? null : Unpooled.wrappedBuffer(record.key()),
                        Unpooled.wrappedBuffer(record.value())))
                .doOnSubscribe(sub -> log.debug("subscribe kafka :{}", topics))
                .doOnCancel(() -> log.debug("unsubscribe kafka :{}", topics))
                .doOnNext(message -> {
                    log.info("receive kafka:topic={},v={}",message.getTopic(),new String(message.getPayload().array()));
                })
                .subscribe();
        countDownLatch.await();

    }
    @Test
    public void testSaslConsumer() throws InterruptedException {
        CountDownLatch countDownLatch = new CountDownLatch(1);
        KafkaProperties kafkaProperties = new KafkaProperties();
        String server="10.113.75.156:31402,10.168.40.141:30106,10.168.40.137:30778";
        kafkaProperties.setBootstrapServers(Arrays.asList(server.split(",")));
        kafkaProperties.getConsumer().setClientId("cc-test-1");
        kafkaProperties.getConsumer().setGroupId("cc-iot-reactor");
        Map<String, String> properties = kafkaProperties.getProperties();
        properties.put("security.protocol","SASL_PLAINTEXT");
        properties.put("sasl.mechanism","PLAIN");
        properties.put("sasl.jaas.config","org.apache.kafka.common.security.scram.ScramLoginModule required username=\"admin\" password=\"RaTVU5KifGcdwJRA\";");
        List<String> topics = Collections.singletonList(KafkaConstants.QUICK_EVENT_TOPIC);
        initConsumer(kafkaProperties, topics);
        receiver.receiveAutoAck()
                .flatMap(Function.identity())
                .<Message>map(record -> SimpleMessage.of(record.topic(),
                        record.key() == null ? null : Unpooled.wrappedBuffer(record.key()),
                        Unpooled.wrappedBuffer(record.value())))
                .doOnSubscribe(sub -> log.debug("subscribe kafka :{}", topics))
                .doOnCancel(() -> log.debug("unsubscribe kafka :{}", topics))
                .doOnNext(message -> {
                    log.info("receive kafka:topic={},v={}",message.getTopic(),new String(message.getPayload().array()));
                })
                .subscribe();
        countDownLatch.await();

    }

    @Test
    public void testProducer() throws InterruptedException {
        KafkaProperties kafkaProperties = new KafkaProperties();
        String server = "10.113.75.55:9092";
        kafkaProperties.setBootstrapServers(Arrays.asList(server.split(",")));
        kafkaProperties.getProducer().setClientId("cc-test-2");
        String topic = KafkaConstants.QUICK_EVENT_TOPIC;
        initProducer(kafkaProperties);
        String payload = RandomStringUtils.randomAlphabetic(8);
        send(Mono.just(SimpleMessage.of(topic,Unpooled.wrappedBuffer("1".getBytes()), Unpooled.wrappedBuffer(payload.getBytes())))).subscribe();
        Thread.sleep(10000);

    }
    @Test
    public void testSaslProducer() throws InterruptedException {
        KafkaProperties kafkaProperties = new KafkaProperties();
        String server="10.113.75.156:31402,10.168.40.141:30106,10.168.40.137:30778";
        kafkaProperties.setBootstrapServers(Arrays.asList(server.split(",")));
        Map<String, String> properties = kafkaProperties.getProperties();
        properties.put("security.protocol","SASL_PLAINTEXT");
        properties.put("sasl.mechanism","PLAIN");
        properties.put("sasl.jaas.config","org.apache.kafka.common.security.scram.ScramLoginModule required username=\"admin\" password=\"RaTVU5KifGcdwJRA\";");
        kafkaProperties.getProducer().setClientId("cc-test-2");
        String topic = KafkaConstants.QUICK_EVENT_TOPIC;
        initProducer(kafkaProperties);
        String payload = RandomStringUtils.randomAlphabetic(8);
        send(Mono.just(SimpleMessage.of(topic,Unpooled.wrappedBuffer(RandomStringUtils.randomAlphanumeric(4).getBytes()), Unpooled.wrappedBuffer(payload.getBytes())))).subscribe();
        Thread.sleep(1000);

    }    public void initConsumer(KafkaProperties properties, Collection<String> topics) {
        Map<String, Object> props = new HashMap<>();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, properties.getBootstrapServers());

        props.putAll(properties.getConsumer().buildProperties());
        props.putAll(properties.getProperties());

        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, ByteArrayDeserializer.class);
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, ByteArrayDeserializer.class);

        ReceiverOptions<byte[], byte[]> senderOptions = ReceiverOptions.<byte[], byte[]>create(props)
                .subscription(topics);
        receiver = KafkaReceiver.create(senderOptions);
    }

    public void initProducer(KafkaProperties properties){
        Map<String, Object> props = new HashMap<>();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, properties.getBootstrapServers());
        props.put(ProducerConfig.CLIENT_ID_CONFIG, properties.getClientId());

        props.putAll(properties.getProducer().buildProperties());
        props.putAll(properties.getProperties());
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, ByteBufferSerializer.class);
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, ByteBufferSerializer.class);
//        props.put(ProducerConfig.ACKS_CONFIG,"1");
        SenderOptions<ByteBuffer, ByteBuffer> senderOptions = SenderOptions.create(props);

        sender = KafkaSender.create(senderOptions);
    }

    public Mono<Void> send(Publisher<Message> publisher) {
        if (sender == null) {
            return Mono.error(new IllegalStateException("kafka sender is shutdown"));
        }
        return sender
                .send(Flux.from(publisher)
                        .map(msg -> SenderRecord.create(msg.getTopic(), null, null, msg.keyToNio(), msg.payloadToNio(), msg)))
                .flatMap(result -> {
                    if (null != result.exception()) {
                        return Mono.error(result.exception());
                    }
                    if (log.isDebugEnabled()) {
                        RecordMetadata metadata = result.recordMetadata();
                        log.debug("Kafka Message {} sent successfully, topic-partition={}-{} offset={} timestamp={}",
                                result.correlationMetadata(),
                                metadata.topic(),
                                metadata.partition(),
                                metadata.offset(),
                                metadata.timestamp());
                    }
                    return Mono.empty();
                })
                .then();
    }
}
