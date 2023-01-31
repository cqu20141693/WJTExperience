package com.wcc.app.domain;

import lombok.Data;

import java.util.Set;

@Data
public class PublishReq {
    private String topic;
    private Set<String> messages;
}
