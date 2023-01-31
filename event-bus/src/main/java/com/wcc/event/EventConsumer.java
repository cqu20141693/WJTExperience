package com.wcc.event;

import reactor.core.publisher.Flux;
import reactor.core.publisher.FluxSink;

/**
 * 事件消费者，作为生产者路由消息到其他代理实例
 */
public interface EventConsumer extends EventConnection {
    Flux<Subscription> handleSubscribe();

    Flux<Subscription> handleUnSubscribe();

    FluxSink<TopicPayload> sink();
}
