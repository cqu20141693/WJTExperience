package com.wcc.flux.util;

import lombok.AllArgsConstructor;
import lombok.Getter;
import reactor.core.publisher.Flux;
import reactor.math.MathFlux;

import java.math.BigDecimal;
import java.time.Duration;
import java.util.Arrays;
import java.util.function.Function;

@AllArgsConstructor
@Getter
public enum AggType  {
    count("计数", flux -> flux.count().flux()),
    sum("求和", flux -> MathFlux.sumDouble(flux, CastUtils::castNumber).flux()),
    avg("平均值", flux -> MathFlux.averageDouble(flux, CastUtils::castNumber).flux()),
    max("最大值", flux -> MathFlux.max(flux, CompareUtils::compare).flux()),
    min("最小值", flux -> MathFlux.min(flux, CompareUtils::compare).flux()),

    first("最初值", flux -> flux.take(1)),
    last("最近值", flux -> flux.takeLast(1)),
    ;

    private final String text;
    private final Function<Flux<Object>, Flux<?>> computer;


    public static Duration parseDuration(String timeString) {
        char[] all = timeString.replace("ms", "S").toCharArray();
        if (all[0] == 'P' || all[0] == '-' && all[1] == 'P') {
            return Duration.parse(timeString);
        } else {
            Duration duration = Duration.ofSeconds(0L);
            char[] tmp = new char[32];
            int numIndex = 0;
            char[] var5 = all;
            int var6 = all.length;

            for(int var7 = 0; var7 < var6; ++var7) {
                char c = var5[var7];
                if (c == '-' || c >= '0' && c <= '9') {
                    tmp[numIndex++] = c;
                } else {
                    long val = (new BigDecimal(tmp, 0, numIndex)).longValue();
                    numIndex = 0;
                    Duration plus = null;
                    if (c != 'D' && c != 'd') {
                        if (c != 'H' && c != 'h') {
                            if (c != 'M' && c != 'm') {
                                if (c == 's') {
                                    plus = Duration.ofSeconds(val);
                                } else if (c == 'S') {
                                    plus = Duration.ofMillis(val);
                                } else if (c == 'W' || c == 'w') {
                                    plus = Duration.ofDays(val * 7L);
                                }
                            } else {
                                plus = Duration.ofMinutes(val);
                            }
                        } else {
                            plus = Duration.ofHours(val);
                        }
                    } else {
                        plus = Duration.ofDays(val);
                    }

                    if (plus != null) {
                        duration = duration.plus(plus);
                    }
                }
            }

            return duration;
        }
    }
    public static void main(String[] args) {
        System.out.println(Arrays.toString(AggType.values()));
    }
}
