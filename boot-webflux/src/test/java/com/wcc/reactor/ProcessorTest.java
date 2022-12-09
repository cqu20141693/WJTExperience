package com.wcc.reactor;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import reactor.core.publisher.EmitterProcessor;
import reactor.core.publisher.FluxSink;

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
                .subscribe(s->{log.info("subscribe {}",s);});
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
}
