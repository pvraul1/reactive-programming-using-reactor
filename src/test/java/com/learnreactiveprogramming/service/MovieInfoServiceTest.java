package com.learnreactiveprogramming.service;

import org.junit.jupiter.api.Test;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.test.StepVerifier;

class MovieInfoServiceTest {

    WebClient webClient = WebClient.builder()
            .baseUrl("http://localhost:8080/movies")
            .build();

    MovieInfoService movieInfoService = new MovieInfoService(webClient);

    @Test
    void retrieveAllMovieInfo_RestClient() {
        var movieInfoFlux = movieInfoService.retrieveAllMovieInfo_RestClient();

        StepVerifier.create(movieInfoFlux)
                .expectNextCount(7)
                .verifyComplete();
    }

    @Test
    void retrieveMovieInfoById_RestClient() {
        var movieInfoId = 1L;
        var movieInfoMono = movieInfoService.retrieveMovieInfoById_RestClient(movieInfoId);

        StepVerifier.create(movieInfoMono)
                .expectNextMatches(movieInfo ->
                        "Batman Begins".equals(movieInfo.getName()))
                .verifyComplete();
    }

}