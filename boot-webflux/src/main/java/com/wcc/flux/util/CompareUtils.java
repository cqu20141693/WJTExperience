package com.wcc.flux.util;


import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.Objects;

public class CompareUtils {
    public CompareUtils() {
    }

    public static int compare(Object source, Object target) {
        if (Objects.equals(source, target)) {
            return 0;
        } else if (source != null && target != null) {
            if (source.equals(target)) {
                return 0;
            } else {
                if (source instanceof Instant) {
                    source = Date.from((Instant)source);
                }

                if (target instanceof Instant) {
                    target = Date.from((Instant)target);
                }

                if (source instanceof LocalDateTime) {
                    source = Date.from(((LocalDateTime)source).atZone(ZoneId.systemDefault()).toInstant());
                }

                if (target instanceof LocalDateTime) {
                    target = Date.from(((LocalDateTime)target).atZone(ZoneId.systemDefault()).toInstant());
                }

                if (source instanceof LocalDate) {
                    source = Date.from(((LocalDate)source).atStartOfDay(ZoneId.systemDefault()).toInstant());
                }

                if (target instanceof LocalDate) {
                    target = Date.from(((LocalDate)target).atStartOfDay(ZoneId.systemDefault()).toInstant());
                }

                if (source instanceof Date) {
                    return compare((Date)source, target);
                } else if (target instanceof Date) {
                    return -compare((Date)target, source);
                } else if (source.getClass().isEnum()) {
                    return compare((Enum)source, target);
                } else if (target.getClass().isEnum()) {
                    return -compare((Enum)target, source);
                } else if (source instanceof Number) {
                    return compare((Number)source, target);
                } else if (target instanceof Number) {
                    return -compare((Number)target, source);
                } else if (source instanceof CharSequence) {
                    return compare(String.valueOf(source), target);
                } else if (target instanceof CharSequence) {
                    return -compare(String.valueOf(target), source);
                } else if (!(source instanceof Boolean) && !(target instanceof Boolean)) {
                    return -1;
                } else {
                    return CastUtils.castBoolean(target) == CastUtils.castBoolean(source) ? 0 : -1;
                }
            }
        } else {
            return -1;
        }
    }

    public static boolean equals(Object source, Object target) {
        try {
            return Objects.equals(source, target) || compare(source, target) == 0;
        } catch (Throwable var3) {
            return false;
        }
    }

    private static int compare(Number number, Object target) {
        return Double.compare(number.doubleValue(), CastUtils.castNumber(target).doubleValue());
    }

    private static int compare(Enum<?> e, Object target) {
        return target instanceof Number ? Integer.compare(e.ordinal(), ((Number)target).intValue()) : e.name().compareToIgnoreCase(String.valueOf(target));
    }

    private static int compare(String string, Object target) {
        return string.compareTo(String.valueOf(target));
    }

    private static int compare(Date date, Object target) {
        try {
            return CastUtils.castDate(target).compareTo(date);
        } catch (Exception var3) {
            return -1;
        }
    }
}

