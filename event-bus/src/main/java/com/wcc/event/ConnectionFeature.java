package com.wcc.event;

public enum ConnectionFeature {

    //消费同一个broker的消息
    consumeSameBroker,
    //消费同一个connection的消息
    consumeSameConnection,
    //订阅其他broker的消息
    consumeAnotherBroker,
    ;
    public boolean in(long mask) {
        return (mask & getMask()) != 0;
    }

    public boolean in(ConnectionFeature... dict) {
        return in(toMask(dict));
    }
    public long getMask() {
        return 1L << ordinal();
    }


    public static long toMask(ConnectionFeature... t) {
        if (t == null) {
            return 0L;
        }
        long value = 0L;
        for (ConnectionFeature t1 : t) {
            value |= t1.getMask();
        }
        return value;
    }
}
