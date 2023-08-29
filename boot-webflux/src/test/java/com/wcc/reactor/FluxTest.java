package com.wcc.reactor;

import com.wcc.reactor.model.EventListener;
import com.wcc.reactor.model.EventSource;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.core.scheduler.Schedulers;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

/**
 * @author weTeam
 * @date 2023/8/28
 **/
public class FluxTest {

    private final EventSource source = new EventSource();

    @Test
    public void testCreate() {
        Flux<String> flux = Flux.create(sink -> {
            source.registry(new EventListener() {
                @Override
                public void onNext(EventSource.Event event) {
                    sink.next(event.getMsg());
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
            // 当发生请求时调用
            sink.onRequest(n -> {
                List<String> messages = source.getHistory(n);
                for (String s : messages) {
                    sink.next(s);
                }
            });
        });
        // 先有订阅才会执行create
        flux.map(i -> "doOnRequest:" + i).log().subscribe(System.out::println);
        source.next(new EventSource.Event("next:1"));


    }

    @Test
    public void testGenerator() {
        // 无状态
        Flux.generate(sink -> {
            sink.next("Hello");
            sink.complete();
        }).subscribe(System.out::println);


        final Random random = new Random();
        // 初始化状态，同步的Sink，只能调用一次next生产事件
        Flux.generate(ArrayList::new, (list, sink) -> {
            int value = random.nextInt(100);
            list.add(value);
            sink.next(value);
            if (list.size() == 10)
                sink.complete();
            return list;
        }).subscribe(System.out::println);


        Flux.create(sink -> {
            for (int i = 0; i < 10; i++)
                sink.next(i);
            sink.complete();
        }).subscribe(System.out::println);

    }

    /**
     * 多个流合并为一个流，同时订阅多个流，合并流的顺序按照接收事件的顺序
     */
    @Test
    public void testMerge() {
        // 将多个流数据合并到一个流中处理，可用于同步多个流的事件
        // 按照元素的产生顺序
        Flux<Long> source1 = Flux.interval(Duration.ofMillis(0L), Duration.ofMillis(100L)).take(5);
        Flux<Long> source2 = Flux.interval(Duration.ofMillis(50L), Duration.ofMillis(100L)).take(5);
        Flux.merge(source1, source2).toStream().forEach(System.out::println);
        source2.mergeWith(source1).subscribe(System.out::println);


        // 同时订阅多个流，会缓存多个流的数据，先处理第一个流直到流完成，才处理第二个流
        Flux.mergeSequential(Flux.interval(Duration.ofMillis(50), Duration.ofMillis(100L)).map(i -> "mergeSequential1:" + i).take(10),
                        Flux.interval(Duration.ofMillis(0), Duration.ofMillis(100L)).map(i -> "mergeSequential2:" + i).take(5))
                .toStream().forEach(System.out::println);


    }

    /**
     * 多个流合并为一个流,按照流的顺讯进行订阅处理，先订阅第一个流直到完成，再订阅第二个流处理，流不是同时订阅的
     */
    @Test
    public void testConcat() {
        Flux<String> source1 = Flux.just("a", "b", "c");
        Flux<String> source2 = Flux.just("d", "e");
        Flux.concat(source1, source2).subscribe(System.out::println);
        System.out.println("----");
        source2.concatWith(source1).subscribe(System.out::println);
    }

    @Test
    public void testZip() {
        // 只有配对时才会合并，否则不会触发
        // 默认合并方式为tuple2Function()
        Flux.just("a", "b").zipWith(Flux.just("c", "d", "e")).subscribe(System.out::println);
        Flux<String> source1 = Flux.just("a", "b", "c");
        Flux<String> source2 = Flux.just("d", "e");
        Flux.zip(source1, source2).subscribe(System.out::println);

        // 自定义合并
        Flux.zip(source1, source2, (first, second) -> String.format("%s:%s", first, second)).subscribe(System.out::println);
        Flux.just("a", "b", "0").zipWith(Flux.just("c", "d"), (s1, s2) -> String.format("%s-%s", s1, s2)).subscribe(System.out::println);
    }


    @Test
    public void testCombineLatest() {

        //存在的问题为当流在数据节点间切换时，出现一个节点消费一份数据，不能实现多消费。
        // 如果单节点，会出现，当前节点重启过程中的数据丢失，
        // 多节点执行需要保证两个数据流在同一个节点消费，这样切换时，只需要初始化数据即可

        // 获取第一个流，可以通过merge进行历史数据的初始化查询和订阅事件消息
        Flux<Long> first = Flux.interval(Duration.ofMillis(1000)).take(5);
        // 第二个流，
        Flux<Long> interval = Flux.interval(Duration.ofMillis(50), Duration.ofMillis(100));
        // combine Latest，必须两个都有数据
        Flux.combineLatest(Arrays::toString,
                first,
                interval.take(6)).filter(i -> {
            // 如果两个数据都是初始化数据
            return true;
        }).toStream().forEach(System.out::println);

    }

    @Test
    public void testScheduler() {
        Flux.create(sink -> {
                    for (int i = 0; i < 10; i++) {
                        sink.next(Thread.currentThread().getName());
                    }
                    sink.complete();
                }).publishOn(Schedulers.single())
                .map(x -> String.format("[%s] %s", Thread.currentThread().getName(), x))
                .publishOn(Schedulers.boundedElastic())
                .map(x -> String.format("[%s] %s", Thread.currentThread().getName(), x))
                // 线程池(默认cpu核心数)，但是当前流只会固定绑定到一个线程
                .subscribeOn(Schedulers.parallel())
                .doOnNext(i -> {
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                })
                .toStream()
                .forEach(System.out::println);

    }
}
