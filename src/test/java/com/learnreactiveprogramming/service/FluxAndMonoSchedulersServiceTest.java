package com.learnreactiveprogramming.service;

import org.junit.jupiter.api.Test;
import reactor.test.StepVerifier;

/**
 * FluxAndMonoSchedulersServiceTest
 * <p>
 * Created by IntelliJ, Spring Framework Guru.
 *
 * @author architecture - pvraul
 * @version 05/04/2025 - 09:14
 * @since 1.17
 */
public class FluxAndMonoSchedulersServiceTest {

    FluxAndMonoSchedulersService fluxAndMonoSchedulersService = new FluxAndMonoSchedulersService();

    @Test
    void explore_publishOn() {
        var flux = fluxAndMonoSchedulersService.explore_publishOn();

        StepVerifier.create(flux)
                .expectNextCount(6)
                .verifyComplete();
    }

    @Test
    void explore_subscribeOn() {
        var flux = fluxAndMonoSchedulersService.explore_subscribeOn();

        StepVerifier.create(flux)
                .expectNextCount(6)
                .verifyComplete();
    }

    @Test
    void explore_parallel() {
        var flux = fluxAndMonoSchedulersService.explore_parallel();

        StepVerifier.create(flux)
                .expectNextCount(3)
                .verifyComplete();
    }

    @Test
    void explore_parallel_usingFlatmap() {
        var flux = fluxAndMonoSchedulersService.explore_parallel_usingFlatmap();

        StepVerifier.create(flux)
                .expectNextCount(3)
                .verifyComplete();
    }

    @Test
    void explore_parallel_usingFlatmap_1() {
        var flux = fluxAndMonoSchedulersService.explore_parallel_usingFlatmap_1();

        StepVerifier.create(flux)
                .expectNextCount(6)
                .verifyComplete();
    }
}
