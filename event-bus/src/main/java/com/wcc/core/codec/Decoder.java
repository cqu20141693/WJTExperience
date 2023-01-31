package com.wcc.core.codec;

import com.wcc.core.Payload;

import javax.annotation.Nonnull;

public interface Decoder<T> {
    Class<T> forType();

    T decode(@Nonnull Payload payload);

    default boolean isDecodeFrom(Object nativeObject){
        if(nativeObject==null){
            return false;
        }
        return forType().isInstance(nativeObject);
    }
}
