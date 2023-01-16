package com.wcc.kafka.handler;

import com.wcc.kafka.domain.KafkaConstants;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class MessageConsumer {

    /**
     * 手动确认消息
     * @param record
     * @param ack
     */
    @KafkaListener(topics = KafkaConstants.QUICK_EVENT_TOPIC, groupId = "${spring.kafka.consumer.group-id}")
    public void hello(ConsumerRecord<?, ?> record, Acknowledgment ack){
       log.info("简单消费：topic={},partition={},key={},value={}",record.topic(),record.partition(),record.key().toString(),record.value());
        ack.acknowledge();
    }
    @KafkaListener(topics = KafkaConstants.DEVICE_DATA_TOPIC, groupId = "${spring.kafka.consumer.group-id}")
    public void deviceData(ConsumerRecord<?, ?> record, Acknowledgment ack){
        log.info("kafka receive：topic={},partition={},value={}",record.topic(),record.partition(),record.value());
        ack.acknowledge();
    }
}
