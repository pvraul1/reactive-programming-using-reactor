package com.learnreactiveprogramming.service;

import java.time.Duration;
import java.util.List;
import java.util.Random;
import java.util.function.Function;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.util.function.Tuple2;

/**
 * FluxAndMonoGeneratorService
 * <p>
 * Created by IntelliJ, Spring Framework Guru.
 *
 * @author architecture - pvraul
 * @version 08/03/2025 - 15:25
 * @since 1.17
 */
@Slf4j
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

    public Flux<String> namesFlux_immutability() {
        var namesFlux = Flux.fromIterable(List.of("adam", "anna", "jack", "jenny"));
        namesFlux.map(String::toUpperCase);

        return namesFlux;
    }

    public Flux<String> namesFlux_map(int stringLength) {
        return Flux.fromIterable(List.of("adam", "anna", "jack", "jenny"))
                .map(String::toUpperCase)
                .filter(string -> string.length() > stringLength)
                .map(s -> s.length() + "-" + s)
                .doOnNext(name -> log.info("Name is: {}", name))
                .doOnSubscribe(subscription -> log.info("Subscription is: {}", subscription))
                .doOnComplete(() -> log.info("Inside the complete callback"))
                .doFinally(signalType -> log.info("Inside the doFinally callback: {}", signalType))
                .log();
    }

    public Mono<String> namesMono_map_filter(int stringLength) {
        return Mono.just("alex")
                .map(String::toUpperCase)
                .filter(string -> string.length() > stringLength)
                .defaultIfEmpty("default")
                .log();
    }

    public Mono<String> namesMono_map_filter_switchIfEmpty(int stringLength) {
        Mono<String> defaultMono = Mono.just("default");

        return Mono.just("alex")
                .map(String::toUpperCase)
                .filter(string -> string.length() > stringLength)
                .switchIfEmpty(defaultMono)
                .log();
    }

    public Mono<List<String>> namesMono_flatMap(int stringLength) {
        return Mono.just("alex")
                .map(String::toUpperCase)
                .filter(s -> s.length() > stringLength)
                .flatMap(this::splitStringMono)
                .log();
    }

    public Flux<String> namesMono_flatMapMany(int stringLength) {
        return Mono.just("alex")
                .map(String::toUpperCase)
                .filter(s -> s.length() > stringLength)
                .flatMapMany(this::splitString)
                .log();
    }

    private Mono<List<String>> splitStringMono(String name) {
        var charArray = name.split("");
        var charList = List.of(charArray);

        return Mono.just(charList);
    }

    public Flux<String> namesFlux_flatmap(int stringLength) {
        return Flux.fromIterable(List.of("alex", "ben", "chloe"))
                .map(String::toUpperCase)
                .filter(s -> s.length() > stringLength)
                .flatMap(this::splitString)
                .log();
    }

    public Flux<String> namesFlux_flatmap_async(int stringLength) {
        return Flux.fromIterable(List.of("alex", "ben", "chloe"))
                .map(String::toUpperCase)
                .filter(s -> s.length() > stringLength)
                .flatMap(this::splitString_withDelay)
                .log();
    }

    public Flux<String> namesFlux_concatmap(int stringLength) {
        return Flux.fromIterable(List.of("alex", "ben", "chloe"))
                .map(String::toUpperCase)
                .filter(s -> s.length() > stringLength)
                .concatMap(this::splitString_withDelay)
                .log();
    }

    public Flux<String> namesFlux_transform(int stringLength) {
        Function<Flux<String>, Flux<String>> filterMap = name -> name
                .map(String::toUpperCase)
                .filter(s -> s.length() > stringLength);

        return Flux.fromIterable(List.of("alex", "ben", "chloe"))
                .transform(filterMap)
                .flatMap(this::splitString)
                .defaultIfEmpty("default")
                .log();
    }

    public Flux<String> namesFlux_transform_switchifEmpty(int stringLength) {
        Function<Flux<String>, Flux<String>> filterMap = name -> name
                .map(String::toUpperCase)
                .filter(s -> s.length() > stringLength)
                .flatMap(this::splitString);

        var defaultFlux = Flux.just("default")
                .transform(filterMap); // "D", "E", "F", "A", "U", "L", "T"

        return Flux.fromIterable(List.of("alex", "ben", "chloe"))
                .transform(filterMap) // "A", "L", "E", "X", "C", "H", "L", "O", "E"
                .switchIfEmpty(defaultFlux)
                .log();
    }

    public Flux<String> explore_concat() {
        var abcFlux = Flux.just("A", "B", "C");
        var defFlux = Flux.just("D", "E", "F");

        return Flux.concat(abcFlux, defFlux).log();
    }

    public Flux<String> explore_concatWith() {
        var abcFlux = Flux.just("A", "B", "C");
        var defFlux = Flux.just("D", "E", "F");

        return abcFlux.concatWith(defFlux).log();
    }

    public Flux<String> explore_concatWith_mono() {
        var aMono = Mono.just("A");
        var bMono = Mono.just("B");

        return aMono.concatWith(bMono).log();
    }

    public Flux<String> explore_merge() {
        var abcFlux = Flux.just("A", "B", "C")
                .delayElements(Duration.ofMillis(100));
        var defFlux = Flux.just("D", "E", "F")
                .delayElements(Duration.ofMillis(125));

        return Flux.merge(abcFlux, defFlux).log(); // "A", "D", "B", "E", "C", "F"
    }

    public Flux<String> explore_mergeWith() {
        var abcFlux = Flux.just("A", "B", "C")
                .delayElements(Duration.ofMillis(100));
        var defFlux = Flux.just("D", "E", "F")
                .delayElements(Duration.ofMillis(125));

        return abcFlux.mergeWith(defFlux).log(); // "A", "D", "B", "E", "C", "F"
    }

    public Flux<String> explore_mergeWith_mono() {
        var aMono = Mono.just("A");
        var bMono = Mono.just("B");

        return aMono.mergeWith(bMono).log(); // "A","B"
    }

    public Flux<String> explore_merge_sequential() {
        var abcFlux = Flux.just("A", "B", "C")
                .delayElements(Duration.ofMillis(100));
        var defFlux = Flux.just("D", "E", "F")
                .delayElements(Duration.ofMillis(125));

        return Flux.mergeSequential(abcFlux, defFlux).log(); // "A", "B", "C", "D", "E", "F"
    }

    public Flux<String> explore_zip() {
        var abcFlux = Flux.just("A", "B", "C");
        var defFlux = Flux.just("D", "E", "F");

        return Flux.zip(abcFlux, defFlux, (first, second) -> first + second).log(); // AD, BE, CF
    }

    public Flux<String> explore_zip_1() {
        var abcFlux = Flux.just("A", "B", "C");
        var defFlux = Flux.just("D", "E", "F");
        var _123Flux = Flux.just("1", "2", "3");
        var _456Flux = Flux.just("4", "5", "6");

        return Flux.zip(abcFlux, defFlux,_123Flux, _456Flux)
                .map(t4 -> t4.getT1() + t4.getT2() + t4.getT3() + t4.getT4())
                .log(); // AD14, BE25, CF36
    }

    public Flux<String> explore_zipWith() {
        var abcFlux = Flux.just("A", "B", "C");
        var defFlux = Flux.just("D", "E", "F");

        return abcFlux.zipWith(defFlux, (first, second) -> first + second).log(); // AD, BE, CF
    }

    public Mono<String> explore_zipWith_mono() {
        var aMono = Mono.just("A");
        var bMono = Mono.just("B");

        return aMono.zipWith(bMono)
                .map(t2 -> t2.getT1() + t2.getT2())
                .log(); // "AB"
    }

    public Flux<String> exception_flux() {
        return Flux.just("A", "B", "C")
                .concatWith(Flux.error(new RuntimeException("Exception occurred")))
                .concatWith(Flux.just("D"))
                .log();
    }

    private Flux<String> splitString(String name) {
        var charArray = name.split("");


        return Flux.fromArray(charArray);
    }

    private Flux<String> splitString_withDelay(String name) {
        var charArray = name.split("");
        var delay = new Random().nextInt(1000);

        return Flux.fromArray(charArray)
                .delayElements(Duration.ofMillis(delay));
    }

    public static void main(String[] args) {
        System.out.println("FluxAndMonoGeneratorService.main");

        FluxAndMonoGeneratorService fluxAndMonoGeneratorService = new FluxAndMonoGeneratorService();
        fluxAndMonoGeneratorService.namesFlux()
                .subscribe(name -> System.out.println("Name is: " + name));

        fluxAndMonoGeneratorService.nameMono()
                .subscribe(name -> System.out.println("Mono name is: " + name));

    }

    public Flux<String> explore_OnErrorReturn() {
        return Flux.just("A", "B", "C")
                .concatWith(Flux.error(new IllegalStateException("Exception occurred")))
                .onErrorReturn("default")
                .log();
    }

}
