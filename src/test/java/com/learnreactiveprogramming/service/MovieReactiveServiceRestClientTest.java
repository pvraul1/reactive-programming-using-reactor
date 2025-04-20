package com.learnreactiveprogramming.service;

import org.junit.jupiter.api.Test;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.test.StepVerifier;

import static org.junit.jupiter.api.Assertions.*;

class MovieReactiveServiceRestClientTest {

    WebClient webClient = WebClient.builder()
            .baseUrl("http://localhost:8080/movies")
            .build();

    ReviewService reviewService = new ReviewService(webClient);
    MovieInfoService movieInfoService = new MovieInfoService(webClient);

    MovieReactiveService movieReactiveService = new MovieReactiveService(movieInfoService, reviewService, null);

    @Test
    void getAllMovies_restClient() {

        var moviesFlux = movieReactiveService.getAllMovies_restClient();

        StepVerifier.create(moviesFlux)
                .expectNextCount(7)
                .verifyComplete();
    }

}