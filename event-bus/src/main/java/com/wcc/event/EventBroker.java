package com.wcc.event;

import reactor.core.publisher.Flux;

/**
 * 事件路由代理
 */
public interface EventBroker {

    /**
     * @return ID
     */
    String getId();

    /**
     * @return 从代理中接收事件连接
     */
    Flux<EventConnection> accept();
}
