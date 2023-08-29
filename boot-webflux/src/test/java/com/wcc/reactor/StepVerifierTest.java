package com.wcc.reactor;

import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

/**
 * @author weTeam
 * @date 2023/8/29
 **/
public class StepVerifierTest {

    @Test
    public void testStepVerifier() {
        Flux<String> zip = Flux.just(1, 2, 3, 4)
                .log()
                .map(i -> i * 2)
                .zipWith(Flux.range(0, Integer.MAX_VALUE),
                        (one, two) -> String.format("First Flux: %d, Second Flux: %d", one, two));
        StepVerifier.create(zip).expectNext("First Flux: 2, Second Flux: 0",
                "First Flux: 4, Second Flux: 1",
                "First Flux: 6, Second Flux: 2",
                "First Flux: 8, Second Flux: 3")
                .verifyComplete();

    }
}
