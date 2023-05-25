package com.wcc.flux.util;


import com.wcc.date.DateFormatter;
import org.reactivestreams.Publisher;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.time.*;
import java.util.*;
import java.util.function.BiFunction;
import java.util.function.Function;

public class CastUtils {
    public CastUtils() {
    }

    public static <T> Flux<T> handleFirst(Flux<?> stream, BiFunction<Object, Flux<?>, Publisher<T>> handler) {
        return stream.switchOnFirst((signal, objectFlux) -> {
            if (!signal.hasValue()) {
                return Mono.empty();
            } else {
                Object first = signal.get();
                return (Publisher) handler.apply(first, objectFlux);
            }
        });
    }

    public static Flux<Object> flatStream(Flux<?> stream) {
        return stream.flatMap((val) -> {
            if (val instanceof Object[]) {
                return Flux.just((Object[]) ((Object[]) val));
            } else if (val instanceof Iterable) {
                return Flux.fromIterable((Iterable) val);
            } else {
                return val instanceof Publisher ? Flux.from((Publisher) val) : Flux.just(val);
            }
        });
    }

    public static boolean castBoolean(Object value) {
        if (value instanceof Boolean) {
            return (Boolean) value;
        } else {
            String strVal = String.valueOf(value);
            return "true".equalsIgnoreCase(strVal) || "y".equalsIgnoreCase(strVal) || "ok".equalsIgnoreCase(strVal) || "yes".equalsIgnoreCase(strVal) || "1".equalsIgnoreCase(strVal);
        }
    }

    public static Map<Object, Object> castMap(List<Object> list) {
        return castMap(list, Function.identity(), Function.identity());
    }

    public static <K, V> Map<K, V> castMap(List<Object> list, Function<Object, K> keyMapper, Function<Object, V> valueMapper) {
        int size = list.size();
        Map<K, V> map = new LinkedHashMap(size);

        for (int i = 0; i < size / 2; ++i) {
            map.put(keyMapper.apply(list.get(i * 2)), valueMapper.apply(list.get(i * 2 + 1)));
        }

        return map;
    }

    public static List<Object> castArray(Object value) {
        if (value instanceof Collection) {
            return new ArrayList((Collection) value);
        } else {
            return value instanceof Object[] ? Arrays.asList((Object[]) ((Object[]) value)) : Collections.singletonList(value);
        }
    }

    public static String castString(Object val) {
        if (val instanceof byte[]) {
            return new String((byte[]) ((byte[]) val));
        } else {
            return val instanceof char[] ? new String((char[]) ((char[]) val)) : String.valueOf(val);
        }
    }

    public static Number castNumber(Object value, Function<Integer, Number> integerMapper, Function<Long, Number> longMapper, Function<Double, Number> doubleMapper, Function<Float, Number> floatMapper, Function<Number, Number> defaultMapper) {
        Number number = castNumber(value);
        if (number instanceof Integer) {
            return (Number) integerMapper.apply((Integer) number);
        } else if (number instanceof Long) {
            return (Number) longMapper.apply((Long) number);
        } else if (number instanceof Double) {
            return (Number) doubleMapper.apply((Double) number);
        } else {
            return number instanceof Float ? (Number) floatMapper.apply((Float) number) : (Number) defaultMapper.apply(number);
        }
    }

    public static Number castNumber(Object value) {
        if (value instanceof CharSequence) {
            String stringValue = String.valueOf(value);
            if (stringValue.startsWith("0x")) {
                return Long.parseLong(stringValue.substring(2), 16);
            }

            try {
                BigDecimal decimal = new BigDecimal(stringValue);
                if (decimal.scale() == 0) {
                    return decimal.longValue();
                }

                return decimal.doubleValue();
            } catch (NumberFormatException var4) {
                try {
                    return castDate(value).getTime();
                } catch (Throwable var3) {
                }
            }
        }

        if (value instanceof Character) {
            return Integer.valueOf((Character) value);
        } else if (value instanceof Boolean) {
            return (Boolean) value ? 1 : 0;
        } else if (value instanceof Number) {
            return (Number) value;
        } else if (value instanceof Date) {
            return ((Date) value).getTime();
        } else {
            throw new UnsupportedOperationException("can not cast to number:" + value);
        }
    }

    public static Date castDate(Object value) {
        if (value instanceof String) {
            if (StringUtils.isNumber(value)) {
                value = Long.parseLong(String.valueOf(value));
            } else {
                String maybeTimeValue = String.valueOf(value);
                LocalDateTime time = LocalDateTime.now();
                if (maybeTimeValue.contains("yyyy")) {
                    maybeTimeValue = maybeTimeValue.replace("yyyy", String.valueOf(time.getYear()));
                }

                if (maybeTimeValue.contains("MM")) {
                    maybeTimeValue = maybeTimeValue.replace("MM", String.valueOf(time.getMonthValue()));
                }

                if (maybeTimeValue.contains("dd")) {
                    maybeTimeValue = maybeTimeValue.replace("dd", String.valueOf(time.getDayOfMonth()));
                }

                if (maybeTimeValue.contains("hh")) {
                    maybeTimeValue = maybeTimeValue.replace("hh", String.valueOf(time.getHour()));
                }

                if (maybeTimeValue.contains("mm")) {
                    maybeTimeValue = maybeTimeValue.replace("mm", String.valueOf(time.getMinute()));
                }

                if (maybeTimeValue.contains("ss")) {
                    maybeTimeValue = maybeTimeValue.replace("ss", String.valueOf(time.getSecond()));
                }

                Date date = DateFormatter.fromString(maybeTimeValue);
                if (null != date) {
                    return date;
                }
            }
        }

        if (value instanceof Number) {
            return new Date(((Number) value).longValue());
        } else {
            if (value instanceof Instant) {
                value = Date.from((Instant) value);
            }

            if (value instanceof LocalDateTime) {
                value = Date.from(((LocalDateTime) value).atZone(ZoneId.systemDefault()).toInstant());
            }

            if (value instanceof LocalDate) {
                value = Date.from(((LocalDate) value).atStartOfDay(ZoneId.systemDefault()).toInstant());
            }

            if (value instanceof ZonedDateTime) {
                value = Date.from(((ZonedDateTime) value).toInstant());
            }

            if (value instanceof Date) {
                return (Date) value;
            } else {
                throw new UnsupportedOperationException("can not cast to date:" + value);
            }
        }
    }

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

            for (int var7 = 0; var7 < var6; ++var7) {
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

    public static Object tryGetFirstValue(Object value) {
        if (value instanceof Map && ((Map) value).size() > 0) {
            return ((Map) value).values().iterator().next();
        } else if (value instanceof Iterable) {
            Iterator<?> iterator = ((Iterable) value).iterator();
            return iterator.hasNext() ? iterator.next() : null;
        } else {
            return value;
        }
    }

    public static Optional<Object> tryGetFirstValueOptional(Object value) {
        return Optional.ofNullable(tryGetFirstValue(value));
    }

}
