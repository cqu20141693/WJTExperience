package com.wcc.kafka.handler;

import com.alibaba.fastjson.JSON;
import com.wcc.kafka.domain.DeviceData;
import com.wcc.kafka.domain.KafkaConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
public class MessageProducer {
    @Autowired
    private KafkaTemplate<String,Object> kafkaTemplate;

    /**
     * 发送消息
     * @param content
     */
    public void sendMessage(String content){
        kafkaTemplate.send(KafkaConstants.QUICK_EVENT_TOPIC,content);
    }

    public void sendUserMessage(DeviceData deviceData){
        kafkaTemplate.send(KafkaConstants.DEVICE_DATA_TOPIC, JSON.toJSONString(deviceData));
    }

}
