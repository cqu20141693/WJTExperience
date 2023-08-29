package com.wcc.reactor;

import com.cronutils.model.Cron;
import com.cronutils.model.CronType;
import com.cronutils.model.definition.CronDefinitionBuilder;
import com.cronutils.model.time.ExecutionTime;
import com.cronutils.parser.CronParser;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.publisher.SignalType;
import reactor.core.scheduler.Scheduler;
import reactor.core.scheduler.Schedulers;

import java.time.Duration;
import java.time.ZonedDateTime;
import java.util.function.Consumer;

@Slf4j
public class CornTaskTest {

    private final Scheduler scheduler = Schedulers.parallel();

    @Test
    public void testDelayInterval() {
        // Mono 单元素流，延迟生产一个事件
        Mono.delay(Duration.ofSeconds(1)).subscribe(System.out::println);
        // Flux 多元素流或者无限流，定时生产数据
        Flux.interval(Duration.ofMillis(1000)).take(5).subscribe(System.out::println);
    }

    @Test
    public void cornTaskTest() throws InterruptedException {
        String cronStr = "1/3 * * * * ?";
        cronTask(cronStr, t -> {
            log.info("cron test data={}", t);
        });
        Thread.sleep(30000);
    }

    public void cronTask(String cronStr, Consumer<Long> consumer) {
        CronParser parser = new CronParser(CronDefinitionBuilder.instanceDefinitionFor(CronType.QUARTZ));
        Cron cron = parser.parse(cronStr);
        ExecutionTime executionTime = ExecutionTime.forCron(cron);

        cronTask(executionTime, consumer);
    }

    private void cronTask(ExecutionTime executionTime, Consumer<Long> consumer) {


        Duration nextTime = executionTime.timeToNextExecution(ZonedDateTime.now()).orElse(Duration.ofSeconds(10));

        Mono.delay(nextTime, scheduler)
                .doOnNext(consumer).doFinally(signal -> {
                    if (signal != SignalType.CANCEL) {
                        cronTask(executionTime, consumer);
                    }
                }).subscribe();

    }
}
