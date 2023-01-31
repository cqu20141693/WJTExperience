package com.wcc.app.domain;

import com.wcc.event.SubscribeFeature;
import lombok.Data;

import java.util.Set;

@Data
public class SubscribeReq {
    private String subscriber;
    private Set<String> topics;
    private Set<SubscribeFeature> features;
}
