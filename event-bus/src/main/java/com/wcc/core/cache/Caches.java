package com.wcc.core.cache;

import com.github.benmanes.caffeine.cache.Caffeine;
import com.google.common.cache.CacheBuilder;
import org.jctools.maps.NonBlockingHashMap;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.function.Supplier;

/**
 * 缓存工具,根据环境来创建不同的ConcurrentMap实现,
 * 支持 jctoolsNonBlockingHashMap,CaffeineCaffeine,Guava. 优先级:
 * jctools
 * Caffeine
 * Guava
 *
 */
public class Caches {
    private static final Supplier<ConcurrentMap<Object, Object>> cacheSupplier;

    private static boolean caffeinePresent() {
        if (Boolean.getBoolean("wcc.cache.caffeine.disabled")) {
            return false;
        }
        try {
            Class.forName("com.github.benmanes.caffeine.cache.Cache");
            return true;
        } catch (ClassNotFoundException e) {
            return false;
        }
    }

    private static boolean jctoolPresent() {
        if (Boolean.getBoolean("wcc.cache.jctool.disabled")) {
            return false;
        }
        try {
            Class.forName("org.jctools.maps.NonBlockingHashMap");
            return true;
        } catch (ClassNotFoundException e) {
            return false;
        }
    }

    private static boolean guavaPresent() {
        return !Boolean.getBoolean("wcc.cache.guava.disabled");
    }

    private static ConcurrentMap<Object, Object> createCaffeine() {
        return Caffeine.newBuilder().build().asMap();
    }

    private static ConcurrentMap<Object, Object> createGuava() {
        return CacheBuilder.newBuilder().build().asMap();
    }


    static {
        if (jctoolPresent()) {
            cacheSupplier = NonBlockingHashMap::new;
        } else if (caffeinePresent()) {
            cacheSupplier = Caches::createCaffeine;
        } else if (guavaPresent()) {
            cacheSupplier = Caches::createGuava;
        } else {
            cacheSupplier = ConcurrentHashMap::new;
        }
    }

    @SuppressWarnings("all")
    public static <K, V> ConcurrentMap<K, V> newCache() {
        return (ConcurrentMap) cacheSupplier.get();
    }
}
