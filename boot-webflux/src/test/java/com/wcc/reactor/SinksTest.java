package com.wcc.reactor;

import org.apache.commons.math3.analysis.function.Sin;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Sinks;

import java.util.concurrent.locks.LockSupport;

/**
 * @author weTeam
 * @date 2023/8/16
 **/
public class SinksTest {

    @Test
    public void testSinks() {
        Sinks.Empty<Object> empty = Sinks.empty();

        //支持多订阅者，如果没有订阅者，那么接收的消息直接丢弃
        Sinks.ManySpec many = Sinks.many();
        Sinks.MulticastSpec multicast = many.multicast();
        Sinks.Many<Object> objectMany = multicast.onBackpressureBuffer();
        objectMany.emitNext(1, Sinks.EmitFailureHandler.FAIL_FAST);


        // reply
        Sinks.Many<Object> replayAll = many.replay().all();
        replayAll.tryEmitNext(1);
        replayAll.asFlux().map(i -> "replayAll:" + i).subscribe(System.out::println);

        // 单订阅者
        Sinks.Many<Object> unicastSink = many.unicast().onBackpressureBuffer();
        unicastSink.tryEmitNext(1);
        unicastSink.asFlux().map(i -> "unicastSink:" + i).subscribe(System.out::println);

        // Sink-Publisher 多订阅者
        Sinks.Many<Object> manySink = Sinks.many().multicast().onBackpressureBuffer();
        if (manySink.tryEmitNext(1).isFailure()) {
            LockSupport.parkNanos(10);
        }
        // Flux-Subscriber
        manySink.asFlux().map(i -> "manySink1:" + i).subscribe(System.out::println);
        manySink.asFlux().map(i -> "manySink2:" + i).subscribe(System.out::println);


        // sink-Mono 多订阅者
        Sinks.One<Object> oneSink = Sinks.one();
        oneSink.emitValue(1, Sinks.EmitFailureHandler.FAIL_FAST);
        // Mono
        oneSink.asMono().map(i -> "onSink1:" + i).subscribe(System.out::println);
        oneSink.asMono().map(i -> "onSink2:" + i).subscribe(System.out::println);

    }

}
