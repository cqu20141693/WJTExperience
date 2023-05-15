package com.wcc.reactor;

import com.alibaba.fastjson.JSONObject;
import io.micrometer.core.instrument.Counter;
import io.micrometer.prometheus.PrometheusConfig;
import io.micrometer.prometheus.PrometheusMeterRegistry;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.util.StopWatch;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;

@Slf4j
public class ReactorMonitorDemo {

    PrometheusMeterRegistry meterRegistry = new PrometheusMeterRegistry(PrometheusConfig.DEFAULT);

    @Test
    public void metricMonitor() throws InterruptedException {
        Flux<Integer> flux = Flux.range(1, 10)
                .delayElements(Duration.ofSeconds(1))
                // 统计被订阅的数据量
                .doOnSubscribe(s -> {
                    Counter.builder("myapp.flux.subscribe.count")
                            .description("The number of subscriptions to myapp Flux")
                            .tags("type", "flux")
                            .register(meterRegistry)
                            .increment();
                })
                // 统计完成的订阅者
                .doFinally(signalType -> {
                    Counter.builder("myapp.flux.complete.count")
                            .description("The number of completions of myapp Flux")
                            .tags("type", "flux")
                            .register(meterRegistry)
                            .increment();
                });
        flux.doOnNext(item -> log.info("first subscriber index={}", item)).subscribe();
        flux.doOnNext(item -> log.info("second subscriber index={}", item)).subscribe();

        String data = "Hello";
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        Mono<String> stringMono = Mono.just(data)
                .doOnNext(s -> meterRegistry.timer("myapp.mono.complete.useTime", "mono", "api/service"))
                .delayElement(Duration.ofSeconds(1))
                .map(str -> {
                    if (str.startsWith(data)) {
                        return str + "+";
                    }
                    return str;
                })
                .doOnSubscribe(s ->
                        Counter.builder("myapp.mono.subscribe.count")
                                .description("The number of subscriptions to myapp Mono")
                                .tags("type", "mono")
                                .register(meterRegistry)
                                .increment()
                )
                .doFinally(signalType -> {
                    Counter.builder("myapp.mono.complete.count")
                            .description("The number of completions of myapp Mono")
                            .tags("type", "mono")
                            .register(meterRegistry)
                            .increment();
                });
        stringMono.subscribe(System.out::println);
        stringMono.subscribe(System.out::println);
        stopWatch.stop();
        Thread.sleep(1000);
        log.info("registry={}", JSONObject.toJSONString(meterRegistry.scrape()));

        Thread.sleep(10000);

    }
}
