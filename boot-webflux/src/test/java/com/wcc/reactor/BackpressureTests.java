package com.wcc.reactor;

import com.wcc.reactor.model.EventListener;
import com.wcc.reactor.model.EventSource;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.reactivestreams.Subscription;
import reactor.core.publisher.BaseSubscriber;
import reactor.core.publisher.Flux;
import reactor.core.publisher.FluxSink;

import java.time.Duration;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author weTeam
 * @date 2023/8/18
 **/
public class BackpressureTests {

    // 定时发布者
    private Flux<Integer> timerPublisher;

    private Flux<Long> interval;

    @BeforeEach
    public void beforeEach() {
        // initialize publisher
        AtomicInteger count = new AtomicInteger();
        timerPublisher = Flux.create(sink ->
                // 启动一个定时器
                new Timer().schedule(new TimerTask() {
                    @Override
                    public void run() {
                        sink.next(count.getAndIncrement());
                        if (count.get() == 10) {
                            sink.complete();
                        }
                    }
                }, 100, 100)
        );

        interval = Flux.interval(Duration.ofSeconds(1));

    }

    /**
     * BUFFER：（默认值）以在下游无法跟上时缓冲所有信号。（这会实现无限缓冲，并可能导致OutOfMemoryError）
     * DROP：如果下游尚未准备好接收信号，则丢弃该信号
     * LATEST：让下游只从上游获取最新信号
     * ERROR：在下游无法跟上时发出错误信号IllegalStateException
     * IGNORE：完全忽略下游背压请求，当下游队列充满时会导致IllegalStateException
     */

    public class SlowSubscriber extends BaseSubscriber<EventSource.Event> {
        private int capacity;
        private int processTime;

        private ThreadPoolExecutor pool;

        public SlowSubscriber(int capacity, int processTime) {
            this.capacity = capacity;
            this.processTime = processTime;
            this.pool = new ThreadPoolExecutor(1, 1, 1,
                    TimeUnit.SECONDS, new ArrayBlockingQueue<>(capacity));
        }

        @Override
        protected void hookOnSubscribe(Subscription subscription) {
            System.out.println("========== request " + capacity + " ==========");
            request(capacity);
        }

        @Override
        protected void hookOnNext(EventSource.Event event) {
            pool.submit(() -> {
                System.out.println(">>> receive event: " + event.getMsg());
                try {
                    TimeUnit.MILLISECONDS.sleep(processTime);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println("========== request 1 ==========");
                request(1);
            });
        }

        @Override
        protected void hookOnComplete() {
            System.out.println("Complete");
        }

        @Override
        protected void hookOnError(Throwable throwable) {
            throwable.printStackTrace();
        }

        @Override
        protected void hookOnCancel() {
            System.out.println("Cancel");
        }
    }

    public class FastPublisher {
        private EventSource source;
        private int processTime;

        public FastPublisher(EventSource source, int processTime) {
            this.source = source;
            this.processTime = processTime;
        }

        public void send() {
            for (int i = 0; i < 10; i++) {
                source.next(new EventSource.Event("event " + i));
                try {
                    TimeUnit.MILLISECONDS.sleep(processTime);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            source.complete();
        }
    }


    private static final int SUBSCRIBER_CAPACITY = 2;
    private static final int PUBLISH_TIME = 200;
    private static final int CONSUME_TIME = 1000;

    private final EventSource source = new EventSource();

    public Flux<EventSource.Event> createFlux(FluxSink.OverflowStrategy backpressure) {
        return Flux.create(sink -> {
            source.registry(new EventListener() {
                @Override
                public void onNext(EventSource.Event event) {
                    sink.next(event);
                }

                @Override
                public void onComplete() {
                    sink.complete();
                }

                @Override
                public void onError(Throwable e) {
                    sink.error(e);
                }
            });
        }, backpressure);
    }

    public void testBuffer() {
        createFlux(FluxSink.OverflowStrategy.BUFFER).subscribe(new SlowSubscriber(SUBSCRIBER_CAPACITY, CONSUME_TIME));
        new FastPublisher(source, PUBLISH_TIME).send();

    }

    public void testDrop() {
        createFlux(FluxSink.OverflowStrategy.DROP).subscribe(new SlowSubscriber(SUBSCRIBER_CAPACITY, CONSUME_TIME));
        new FastPublisher(source, PUBLISH_TIME).send();
    }

    public void testError() {
        createFlux(FluxSink.OverflowStrategy.ERROR).subscribe(new SlowSubscriber(SUBSCRIBER_CAPACITY, CONSUME_TIME));
        new FastPublisher(source, PUBLISH_TIME).send();
    }

    public void testLatest() {
        createFlux(FluxSink.OverflowStrategy.LATEST).subscribe(new SlowSubscriber(SUBSCRIBER_CAPACITY, CONSUME_TIME));
        new FastPublisher(source, PUBLISH_TIME).send();
    }

    public void testIgnore() {
        createFlux(FluxSink.OverflowStrategy.IGNORE).subscribe(new SlowSubscriber(SUBSCRIBER_CAPACITY, CONSUME_TIME));
        new FastPublisher(source, PUBLISH_TIME).send();
    }

    @Test
    public void test() throws InterruptedException {
//        testBuffer();
//        testDrop();
//        testError();
//        testLatest();
        testIgnore();
        Thread.sleep(10000);
    }
}
