package com.learnreactiveprogramming.service;

import java.util.List;
import org.junit.jupiter.api.Test;
import reactor.test.StepVerifier;

public class FluxAndMonoGeneratorServiceTest {

    FluxAndMonoGeneratorService fluxAndMonoGeneratorService = new FluxAndMonoGeneratorService();

    @Test
    public void namesFlux() {
        var namesFlux = fluxAndMonoGeneratorService.namesFlux();

        StepVerifier.create(namesFlux)
                .expectNext("adam")
                .expectNextCount(3)
                .verifyComplete();
    }

    @Test
    public void nameMono() {
        var nameMono = fluxAndMonoGeneratorService.nameMono();

        StepVerifier.create(nameMono)
                .expectNext("adam")
                .verifyComplete();
    }

    @Test
    public void namesFlux_map() {

        int stringLength = 3;

        var namesFlux = fluxAndMonoGeneratorService.namesFlux_map(stringLength);

        StepVerifier.create(namesFlux)
                .expectNext("4-ADAM", "4-ANNA", "4-JACK", "5-JENNY")
                .verifyComplete();
    }

    @Test
    void namesFlux_immutability() {
        var namesFlux = fluxAndMonoGeneratorService.namesFlux_immutability();

        StepVerifier.create(namesFlux)
                .expectNext("adam", "anna", "jack", "jenny")
                .verifyComplete();
    }

    @Test
    public void namesFlux_map_filter() {
        int stringLength = 4;
        var namesFlux = fluxAndMonoGeneratorService.namesFlux_map(stringLength);

        StepVerifier.create(namesFlux)
                .expectNext("5-JENNY")
                .verifyComplete();
    }

    @Test
    public void namesMono_map_filter() {
        int stringLength = 3;
        var namesMono = fluxAndMonoGeneratorService.namesMono_map_filter(stringLength);

        StepVerifier.create(namesMono)
                .expectNext("ALEX")
                .verifyComplete();
    }

    @Test
    public void namesMono_map_filter_1() {
        int stringLength = 4;
        var namesMono = fluxAndMonoGeneratorService.namesMono_map_filter(stringLength);

        StepVerifier.create(namesMono)
                .expectNext("default")
                .verifyComplete();
    }

    @Test
    public void namesMono_map_filter_switchIfEmpty() {
        int stringLength = 4;
        var namesMono = fluxAndMonoGeneratorService.namesMono_map_filter_switchIfEmpty(stringLength);

        StepVerifier.create(namesMono)
                .expectNext("default")
                .verifyComplete();
    }

    @Test
    public void namesFlux_flatmap() {
        int stringLength = 3;
        var namesFlux = fluxAndMonoGeneratorService.namesFlux_flatmap(stringLength);

        StepVerifier.create(namesFlux)
                .expectNext("A", "L", "E", "X", "C", "H", "L", "O", "E")
                .verifyComplete();
    }

    @Test
    public void namesFlux_flatmap_async() {
        int stringLength = 3;
        var namesFlux = fluxAndMonoGeneratorService.namesFlux_flatmap_async(stringLength);

        StepVerifier.create(namesFlux)
                //.expectNext("A", "L", "E", "X", "C", "H", "L", "O", "E")
                .expectNextCount(9)
                .verifyComplete();
    }

    @Test
    public void namesFlux_concatmap() {
        int stringLength = 3;
        var namesFlux = fluxAndMonoGeneratorService.namesFlux_concatmap(stringLength);

        StepVerifier.create(namesFlux)
                .expectNext("A", "L", "E", "X", "C", "H", "L", "O", "E")
                //.expectNextCount(9)
                .verifyComplete();
    }

    @Test
    public void namesMono_flatMap() {
        int stringLength = 3;
        var namesMono = fluxAndMonoGeneratorService.namesMono_flatMap(stringLength);

        StepVerifier.create(namesMono)
                .expectNext(List.of("A", "L", "E", "X"))
                .verifyComplete();
    }

    @Test
    public void namesMono_flatMapMany() {
        int stringLength = 3;
        var namesMono = fluxAndMonoGeneratorService.namesMono_flatMapMany(stringLength);

        StepVerifier.create(namesMono)
                .expectNext("A", "L", "E", "X")
                .verifyComplete();
    }

    @Test
    public void namesFlux_transform() {
        int stringLength = 3;
        var namesFlux = fluxAndMonoGeneratorService.namesFlux_transform(stringLength);

        StepVerifier.create(namesFlux)
                .expectNext("A", "L", "E", "X", "C", "H", "L", "O", "E")
                .verifyComplete();
    }

    @Test
    public void namesFlux_transform_1() {
        int stringLength = 6;
        var namesFlux = fluxAndMonoGeneratorService.namesFlux_transform(stringLength);

        StepVerifier.create(namesFlux)
                .expectNext("default")
                .verifyComplete();
    }

    @Test
    public void namesFlux_transform_switchifEmpty() {
        int stringLength = 6;
        var namesFlux = fluxAndMonoGeneratorService.namesFlux_transform_switchifEmpty(stringLength);

        StepVerifier.create(namesFlux)
                .expectNext("D", "E", "F", "A", "U", "L", "T")
                .verifyComplete();
    }

    @Test
    public void explore_concat() {
        var namesFlux = fluxAndMonoGeneratorService.explore_concat();

        StepVerifier.create(namesFlux)
                .expectNext("A", "B", "C", "D", "E", "F")
                .verifyComplete();
    }

    @Test
    public void explore_concatWith() {
        var namesFlux = fluxAndMonoGeneratorService.explore_concatWith();

        StepVerifier.create(namesFlux)
                .expectNext("A", "B", "C", "D", "E", "F")
                .verifyComplete();
    }

    @Test
    public void explore_concatWith_mono() {
        var namesFlux = fluxAndMonoGeneratorService.explore_concatWith_mono();

        StepVerifier.create(namesFlux)
                .expectNext("A", "B")
                .verifyComplete();
    }

    @Test
    public void explore_merge() {
        var namesFlux = fluxAndMonoGeneratorService.explore_merge();

        StepVerifier.create(namesFlux)
                .expectNext("A", "D", "B", "E", "C", "F")
                .verifyComplete();
    }

    @Test
    public void explore_mergeWith() {
        var namesFlux = fluxAndMonoGeneratorService.explore_mergeWith();

        StepVerifier.create(namesFlux)
                .expectNext("A", "D", "B", "E", "C", "F")
                .verifyComplete();
    }

    @Test
    public void explore_mergeWith_mono() {
        var namesFlux = fluxAndMonoGeneratorService.explore_mergeWith_mono();

        StepVerifier.create(namesFlux)
                .expectNext("A","B")
                .verifyComplete();
    }

    @Test
    public void explore_merge_sequential() {
        var namesFlux = fluxAndMonoGeneratorService.explore_merge_sequential();

        StepVerifier.create(namesFlux)
                .expectNext("A", "B", "C", "D", "E", "F")
                .verifyComplete();
    }

    @Test
    public void explore_zip() {
        var namesFlux = fluxAndMonoGeneratorService.explore_zip();

        StepVerifier.create(namesFlux)
                .expectNext("AD", "BE", "CF")
                .verifyComplete();
    }

    @Test
    public void explore_zip_1() {
        var namesFlux = fluxAndMonoGeneratorService.explore_zip_1();

        StepVerifier.create(namesFlux)
                .expectNext("AD14", "BE25", "CF36")
                .verifyComplete();
    }

    @Test
    public void explore_zipWith() {
        var namesFlux = fluxAndMonoGeneratorService.explore_zipWith();

        StepVerifier.create(namesFlux)
                .expectNext("AD", "BE", "CF")
                .verifyComplete();
    }

    @Test
    public void explore_zipWith_mono() {
        var namesMono = fluxAndMonoGeneratorService.explore_zipWith_mono();

        StepVerifier.create(namesMono)
                .expectNext("AB")
                .verifyComplete();
    }

}
