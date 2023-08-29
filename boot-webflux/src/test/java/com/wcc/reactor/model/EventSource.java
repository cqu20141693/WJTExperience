package com.wcc.reactor.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author weTeam
 * @date 2023/8/25
 **/
public class EventSource {
    private final List<EventListener> listeners = new ArrayList<>();

    public void registry(EventListener listener) {
        listeners.add(listener);
    }

    public void next(Event event) {
        System.out.println("<<< send event: " + event.getMsg());
        listeners.forEach(l -> l.onNext(event));
    }

    public void complete() {
        listeners.forEach(EventListener::onComplete);
    }

    public List<String> getHistory(long n) {
        return Arrays.asList("1","2","3");
    }

    @Data
    @AllArgsConstructor
    public static class Event {
        private String msg;
    }


}
