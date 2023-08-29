package com.wcc.reactor.model;

/**
 * @author weTeam
 * @date 2023/8/25
 **/

public interface EventListener {
    /**
     * 监听到事件
     *
     * @param event
     */
    void onNext(EventSource.Event event);

    /**
     * 当监听器完成
     */
    void onComplete();

    /**
     * 当Listener发生异常
     *
     * @param e
     */
    void onError(Throwable e);
}
