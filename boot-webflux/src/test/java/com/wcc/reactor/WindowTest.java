package com.wcc.reactor;

import com.wcc.flux.util.AggType;
import org.apache.commons.lang3.RandomUtils;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.publisher.UnicastProcessor;

import java.time.Duration;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class WindowTest {

    @Test
    public void windowApi() {

        List<Integer> numList = IntStream.range(0, 100).boxed().collect(Collectors.toList());
        //设置step=2, 3, 4
        Flux.fromIterable(numList)
                .window(10)
                .doOnNext(integerFlux -> {
                    integerFlux.collectList().doOnNext(System.out::println).subscribe();
                }).subscribe();
        System.out.println("-------  start window step 10 ----------");
        Flux.fromIterable(numList)
                .window(10, 10)
                .doOnNext(integerFlux -> {
                    integerFlux.collectList().doOnNext(System.out::println).subscribe();
                }).subscribe();
        System.out.println("-------  start window step 5 ----------");
        Flux.fromIterable(numList)
                .window(10, 5)
                .doOnNext(integerFlux -> {
                    integerFlux.collectList().doOnNext(System.out::println).subscribe();
                }).subscribe();

        testWindowTime();

        testWindowSizeTime();

        testWindowAgg(numList);

        boolean handleFirst = true;
        int counter = 2;

        testWindowShake(handleFirst, counter);
    }

    private void testWindowShake(boolean handleFirst, int counter) {
        ArrayList<Map<String, Integer>> maps = new ArrayList<>();
        IntStream.range(0, 100).forEach(i -> {
            HashMap<String, Integer> map = new HashMap<>();
            map.put("deviceId", RandomUtils.nextInt(1, 100) & 8);
            map.put("productId", 1);
            map.put("value", i);
            maps.add(map);
        });

        Flux.fromIterable(maps)
                // as 数据分组
                .as(mapFlux -> mapFlux.groupBy(v -> v.get("deviceId"))
                        .flatMap(g -> g.window(3))
                ).flatMap(group -> {
                    Flux<Map<String, Integer>> filter = group.filter(data -> (data.get("value") & 2) == 0);
//                    Flux<Map<String, Integer>> mapFlux = handleFirst ? filter.take(1) : filter.takeLast(1);
//                    return filter.count().filter(i -> i >= counter).flatMapMany(i -> mapFlux);
                    return filter.collectList().flatMap(list -> {
                        if (list.size() >= counter) {
                            Map<String, Integer> stringIntegerMap = handleFirst ? list.get(0) : list.get(list.size() - 1);
                            return Mono.just(stringIntegerMap);
                        }
                        return Mono.empty();
                    });
                })
                .doOnNext(avg -> System.out.println("shade=" + avg))
                .subscribe();
    }

    private void testWindowAgg(List<Integer> numList) {
        Flux.fromIterable(numList)
                .as((flux) ->// 数据按条件三个数据分组
                        flux.window(30)
                )
                // 处理分组数据
                .flatMap(group ->
                        // 数据过滤
                        group.filter(i -> (i & 2) == 0)
                                // 数据转换
                                .map(i -> (Object) i)
                                // as 流聚合
                                .as(window -> AggType.avg.getComputer().apply(window)))
                .doOnNext(avg -> System.out.println("avg=" + avg))
                .subscribe();
    }

    private void testWindowTime() {
        UnicastProcessor<Integer> hotSource = UnicastProcessor.create();
        Flux<Integer> hotFlux = hotSource
                .publish()
                .autoConnect()
                .onBackpressureBuffer(10);

        CompletableFuture<Void> future = CompletableFuture.runAsync(() -> {
            IntStream.range(0, 100).forEach(
                    value -> {
                        try {
                            Thread.sleep(100);
                        } catch (InterruptedException ignored) {

                        }
                        hotSource.onNext(value);
                    }
            );
        });

        System.out.println("--------  start window time 1s  --------");
        hotFlux.window(Duration.ofSeconds(1))
                .doOnNext(integerFlux -> {
                    integerFlux.collectList().doOnNext(System.out::println).subscribe();
                }).subscribe();
        future.thenRun(hotSource::onComplete);
        future.join();
    }

    private void testWindowSizeTime() {
        UnicastProcessor<Integer> hotSource = UnicastProcessor.create();
        Flux<Integer> hotFlux = hotSource
                .publish()
                .autoConnect()
                .onBackpressureBuffer(10);

        CompletableFuture<Void> future = CompletableFuture.runAsync(() -> {
            IntStream.range(0, 100).forEach(
                    value -> {
                        try {
                            Thread.sleep(RandomUtils.nextInt(10, 200));
                        } catch (InterruptedException ignored) {

                        }
                        hotSource.onNext(value);
                    }
            );
        });

        System.out.println("--------  start window size 10 time 1s  --------");
        hotFlux.windowTimeout(20, Duration.ofSeconds(1))
                .doOnNext(integerFlux -> {
                    integerFlux.collectList().doOnNext(System.out::println).subscribe();
                }).subscribe();
        future.thenRun(hotSource::onComplete);
        future.join();
    }

    @Test
    public void bufferApi() {

    }
}
