package com.wcc.reactor;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import reactor.core.Disposable;
import reactor.core.publisher.EmitterProcessor;
import reactor.core.publisher.FluxSink;
import reactor.core.publisher.MonoSink;

@Slf4j
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class ProcessorTest {

    private final EmitterProcessor<String> processor = EmitterProcessor.create(false);
    FluxSink<String> sink = processor.sink(FluxSink.OverflowStrategy.BUFFER);

    @BeforeAll
    public void init() {
        processor.doOnNext(s -> {
                    log.info("start task->{}", s);

                }).doFinally(signalType -> {
                    log.info("finally {}", signalType);
                })
//                .subscribe();
                .subscribe(s -> {
                    log.info("subscribe {}", s);
                });
    }

    @AfterAll
    private void shutdown() {
        sink.complete();
    }

    @Test
    public void testSink() throws InterruptedException {

        CornTaskTest test = new CornTaskTest();
        test.cronTask("1/5 * * * * ?", t -> {
            sink.next(String.valueOf(System.currentTimeMillis()));
        });
        Thread.sleep(30000);
    }


    @Test
    public void testA() {
        final EmitterProcessor<String> messageProcessor = EmitterProcessor.create(false);
        final FluxSink<String> sink = messageProcessor.sink(FluxSink.OverflowStrategy.BUFFER);
        Disposable subscribe = messageProcessor.doOnNext(i -> {
            if(i.equals("10")){
                throw new RuntimeException();
            }
            log.info(i);
        }).doOnCancel(()->log.error("onCancel")).subscribe();
        for (int i = 0; i < 10; i++) {
            sink.next(String.valueOf(i));
        }
        System.out.println("hasDownstreams:" + messageProcessor.hasDownstreams());
        sink.next("tt");
        System.out.println("isTerminated:" + messageProcessor.isTerminated());
      // messageProcessor.onComplete();
        System.out.println("hasDownstreams:" + messageProcessor.hasDownstreams());
        System.out.println("isTerminated:" + messageProcessor.isTerminated());
       sink.next("ttt");
        System.out.println("isCancelled:" + messageProcessor.isCancelled());
        System.out.println("isDisposed:" + messageProcessor.isDisposed());
        subscribe.dispose();
         System.out.println("hasDownstreams:"+messageProcessor.hasDownstreams());
         System.out.println("isCancelled:"+messageProcessor.isCancelled());
         System.out.println("isDisposed:"+messageProcessor.isDisposed());

    }
}
