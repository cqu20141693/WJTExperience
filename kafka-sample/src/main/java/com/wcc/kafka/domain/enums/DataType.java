package com.wcc.kafka.domain.enums;

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.RandomUtils;

public enum DataType {
    INT,
    DOUBLE,
    FLOAT,
    STRING,
    BOOL,
    ;

    public static Object mockValue(DataType type){
        switch (type){
            case INT:
                return RandomUtils.nextInt();
            case DOUBLE:
                return RandomUtils.nextDouble();
            case FLOAT:
                return RandomUtils.nextFloat();
            case STRING:
                return RandomStringUtils.randomAlphanumeric(8);
            case BOOL:
                return RandomUtils.nextBoolean();
        }
        return null;
    }
}
