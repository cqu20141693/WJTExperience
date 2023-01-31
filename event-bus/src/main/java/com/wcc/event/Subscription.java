package com.wcc.event;


import com.wcc.core.utils.TopicUtils;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.util.Assert;

import java.io.Serializable;
import java.util.*;
import java.util.stream.Collectors;

@AllArgsConstructor
@Getter
public class Subscription implements Serializable {
    private static final long serialVersionUID = -6849794470754667710L;

    public static final SubscribeFeature[] DEFAULT_FEATURES = SubscribeFeature.values();

    //订阅者标识
    private final String subscriber;

    //订阅主题,主题以/分割,如: /device/TS-01/09012/message 支持通配符 /device/**
    private final String[] topics;

    //订阅特性
    private final SubscribeFeature[] features;

    private Runnable doOnSubscribe;

    public static Subscription of(String subscriber, String... topic) {

        return Subscription
                .builder()
                .subscriberId(subscriber)
                .topics(topic)
                .build();
//        return new Subscription(subscriber, topic, DEFAULT_FEATURES, null);
    }

    public static Subscription of(String subscriber, String[] topic, SubscribeFeature... features) {
        return Subscription
                .builder()
                .subscriberId(subscriber)
                .topics(topic)
                .features(features)
                .build();
    }

    public static Subscription of(String subscriber, String topic, SubscribeFeature... features) {
        return Subscription
                .builder()
                .subscriberId(subscriber)
                .topics(topic)
                .features(features)
                .build();
        //return new Subscription(subscriber, new String[]{topic}, features, null);
    }

    public Subscription copy(SubscribeFeature... newFeatures) {
        return new Subscription(subscriber, topics, newFeatures, null);
    }

    public Subscription onSubscribe(Runnable sub) {
        this.doOnSubscribe = sub;
        return this;
    }

    public boolean hasFeature(SubscribeFeature feature) {
        return feature.in(this.features);
    }


    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        //订阅者标识
        private String subscriber;

        //订阅主题,主题以/分割,如: /device/TS-01/09012/message 支持通配符 /device/**
        private final Set<String> topics = new HashSet<>();

        //订阅特性
        private final Set<SubscribeFeature> features = new HashSet<>();

        private Runnable doOnSubscribe;

        public Builder randomSubscriberId() {
            return subscriberId(UUID.randomUUID().toString());
        }

        public Builder subscriberId(String id) {
            this.subscriber = id;
            return this;
        }

        public Builder topics(String... topics) {
            return topics(Arrays.asList(topics));
        }

        public Builder topics(Collection<String> topics) {
            this.topics.addAll(topics.stream()
                    .flatMap(topic -> TopicUtils.expand(topic).stream())
                    .collect(Collectors.toSet()));
            return this;
        }

        public Builder features(SubscribeFeature... features) {
            this.features.addAll(Arrays.asList(features));
            return this;
        }

        public Builder doOnSubscribe(Runnable runnable) {
            this.doOnSubscribe = runnable;
            return this;
        }

        public Builder justLocal() {
            this.features.clear();
            return features(SubscribeFeature.local);
        }

        public Builder justBroker() {
            this.features.clear();
            return features(SubscribeFeature.broker);
        }

        public Builder local() {
            return features(SubscribeFeature.local);
        }

        public Builder broker() {
            return features(SubscribeFeature.broker);
        }

        public Builder shared() {
            return features(SubscribeFeature.shared);
        }

        public Subscription build() {
            if (features.isEmpty()) {
                local();
            }
            Assert.notEmpty(topics, "topic cannot be empty");
            Assert.hasText(subscriber, "subscriber cannot be empty");
            return new Subscription(subscriber, topics.toArray(new String[0]), features.toArray(new SubscribeFeature[0]), doOnSubscribe);
        }

    }
}

