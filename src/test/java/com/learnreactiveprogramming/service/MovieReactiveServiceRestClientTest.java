package com.learnreactiveprogramming.service;

import org.junit.jupiter.api.Test;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.test.StepVerifier;

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

    @Test
    void getMovieById_RestClient() {
        var movieId = 1L;
        var movieMono = movieReactiveService.getMovieById_RestClient(movieId);

        StepVerifier.create(movieMono)
                .expectNextCount(1)
                .verifyComplete();
    }

}