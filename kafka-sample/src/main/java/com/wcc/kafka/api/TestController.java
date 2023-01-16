package com.wcc.kafka.api;

import com.wcc.kafka.domain.DeviceData;
import com.wcc.kafka.domain.enums.DataType;
import com.wcc.kafka.handler.MessageProducer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("kafka")
public class TestController {
    @Autowired
    private MessageProducer messageProducer;

    @GetMapping(value = "hello")
    public Object hello(@RequestParam("content") String content) {
        messageProducer.sendMessage(content);
        return "SUCCESS";
    }

    @GetMapping(value = "device/data")
    public Object user(@RequestParam("deviceId")String deviceId, @RequestParam("property")String property,
                       @RequestParam("type")DataType type) {
        DeviceData deviceData = DeviceData.mockData(deviceId, property, type);
        messageProducer.sendUserMessage(deviceData);
        return "SUCCESS";
    }
}
