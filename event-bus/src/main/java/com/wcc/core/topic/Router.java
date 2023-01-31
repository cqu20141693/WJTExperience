package com.wcc.core.topic;

import org.reactivestreams.Publisher;
import reactor.core.publisher.Flux;

import java.util.function.Function;

/**
 * 基于主题的路由器
 *
 *    Router.create()
 *          .route("/device/publish")
 *
 * Type parameters:<T>
 */
public interface Router<T, R> {

    Router<T, R> route(String topic, Function<T, Publisher<R>> handler);

    Router<T, R> remove(String topic);

    Flux<Publisher<R>> execute(String topic, T data);

    void close();

    static <T, R> Router<T, R> create() {
        return new TopicRouter<>();
    }

}
