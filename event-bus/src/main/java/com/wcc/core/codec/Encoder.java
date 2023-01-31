package com.wcc.core.codec;

import com.wcc.core.Payload;

public interface Encoder<T> {

    Payload encode(T body);
}
