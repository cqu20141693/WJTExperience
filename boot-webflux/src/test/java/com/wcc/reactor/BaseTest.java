package com.wcc.reactor;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;

import java.time.Duration;
import java.time.LocalDateTime;

/**
 * @author weTeam
 * @date 2023/8/18
 **/
public class BaseTest {
    public static final int CONCURRENT_SIZE = 100;
    public static final int PACK_SIZE = 10_000;

    private LocalDateTime start;

    @BeforeEach
    public void beforeEach() {
        start = LocalDateTime.now();
    }

    @AfterEach
    public void afterEach() {
        System.out.println(Duration.between(start, LocalDateTime.now()).toMillis());
    }
}
