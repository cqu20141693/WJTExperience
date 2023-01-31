package com.wcc.event;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@AllArgsConstructor
@Getter
public enum SubscribeFeature {

    //如果相同的订阅者,只有一个订阅者收到消息
    shared("shared"),
    //订阅本地消息
    local("订阅本地消息"),
    //订阅来自代理的消息
    broker("订阅代理消息");

    private final String text;
    public boolean in(long mask) {
        return (mask & getMask()) != 0;
    }

    public boolean in(SubscribeFeature... dict) {
        return in(toMask(dict));
    }
    public long getMask() {
        return 1L << ordinal();
    }


   public static long toMask(SubscribeFeature... t) {
        if (t == null) {
            return 0L;
        }
        long value = 0L;
        for (SubscribeFeature t1 : t) {
            value |= t1.getMask();
        }
        return value;
    }

  public   static List<SubscribeFeature> getByMask(Class<SubscribeFeature> tClass, long mask) {

        return getByMask(Arrays.asList(tClass.getEnumConstants()), mask);
    }
    static  List<SubscribeFeature> getByMask(List<SubscribeFeature> allOptions, long mask) {
        if (allOptions.size() >= 64) {
            throw new UnsupportedOperationException("不支持选项超过64个数据字典!");
        }
        List<SubscribeFeature> arr = new ArrayList<>();
        List<SubscribeFeature> all = allOptions;
        for (SubscribeFeature t : all) {
            if (t.in(mask)) {
                arr.add(t);
            }
        }
        return arr;
    }
}

