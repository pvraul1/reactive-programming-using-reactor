package com.learnreactiveprogramming.service;

import java.util.List;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * FluxAndMonoGeneratorService
 * <p>
 * Created by IntelliJ, Spring Framework Guru.
 *
 * @author architecture - pvraul
 * @version 08/03/2025 - 15:25
 * @since 1.17
 */
public class FluxAndMonoGeneratorService {

    public Flux<String> namesFlux() {
        return Flux.fromIterable(List.of("adam", "anna", "jack", "jenny")).log();
    }

    public Mono<String> nameMono() {
        return Mono.just("adam");
    }

    public Flux<String> namesFlux_map() {
        return Flux.fromIterable(List.of("adam", "anna", "jack", "jenny"))
                .map(String::toUpperCase)
                .log();
    }

    public static void main(String[] args) {
        System.out.println("FluxAndMonoGeneratorService.main");

        FluxAndMonoGeneratorService fluxAndMonoGeneratorService = new FluxAndMonoGeneratorService();
        fluxAndMonoGeneratorService.namesFlux()
                .subscribe(name -> System.out.println("Name is: " + name));

        fluxAndMonoGeneratorService.nameMono()
                .subscribe(name -> System.out.println("Mono name is: " + name));

    }

}
