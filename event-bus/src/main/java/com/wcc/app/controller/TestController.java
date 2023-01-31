package com.wcc.app.controller;

import com.wcc.app.domain.PublishReq;
import com.wcc.app.domain.SubscribeReq;
import com.wcc.event.EventBus;
import com.wcc.event.SubscribeFeature;
import com.wcc.event.Subscription;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("bus")
@Slf4j
public class TestController {

    @Autowired
    private EventBus eventBus;
    @PostMapping("sub")
    public Mono<String> sub(@RequestBody SubscribeReq req){
        String[] ts = req.getTopics().toArray(new String[0]);
        Subscription subscription = Subscription.of(req.getSubscriber(),
                ts,req.getFeatures().toArray(new SubscribeFeature[0]));

        eventBus.subscribe(subscription).doOnNext(topicPayload -> {
            log.info("subscribe {} : {}",topicPayload.getTopic(),topicPayload.getPayload().bodyToString());
        }).subscribe();
        return Mono.just("success");
    }

    @PostMapping("pub")
    public Mono<Long> pub(@RequestBody PublishReq req){

       return eventBus.publish(req.getTopic(), Flux.fromIterable(req.getMessages()));
    }
}
