package com.learnreactiveprogramming.service;

import com.learnreactiveprogramming.exception.MovieException;
import com.learnreactiveprogramming.exception.NetworkException;
import com.learnreactiveprogramming.exception.ServiceException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.test.StepVerifier;

import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

/**
 * MovieReactiveServiceMockTest
 * <p>
 * Created by IntelliJ, Spring Framework Guru.
 *
 * @author architecture - pvraul
 * @version 01/04/2025 - 21:12
 * @since 1.17
 */
@ExtendWith(MockitoExtension.class)
public class MovieReactiveServiceMockTest {

    @Mock
    private MovieInfoService movieInfoService;

    @Mock
    private ReviewService reviewService;

    @InjectMocks
    MovieReactiveService reactiveMovieService;

    @Test
    void getAllMovies() {

        Mockito.when(movieInfoService.retrieveMoviesFlux())
                .thenCallRealMethod();

        Mockito.when(reviewService.retrieveReviewsFlux(Mockito.anyLong()))
                .thenCallRealMethod();

        var moviesFlux = reactiveMovieService.getAllMovies();

        StepVerifier.create(moviesFlux)
                .expectNextCount(3)
                .verifyComplete();
    }

    @Test
    void getAllMovies_1() {
        var errorMessage = "Exception ocurred in ReviewService";
        Mockito.when(movieInfoService.retrieveMoviesFlux())
                .thenCallRealMethod();

        Mockito.when(reviewService.retrieveReviewsFlux(Mockito.anyLong()))
                .thenThrow(new RuntimeException(errorMessage));

        var moviesFlux = reactiveMovieService.getAllMovies();

        StepVerifier.create(moviesFlux)
                //.expectError(MovieException.class)
                .expectErrorMessage(errorMessage)
                .verify();
    }

    @Test
    void getAllMovies_retry() {
        Mockito.when(movieInfoService.retrieveMoviesFlux())
                .thenCallRealMethod();

        Mockito.when(reviewService.retrieveReviewsFlux(Mockito.anyLong()))
                .thenThrow(new RuntimeException("Exception ocurred in ReviewService"));

        var moviesFlux = reactiveMovieService.getAllMovies_retry();

        StepVerifier.create(moviesFlux)
                .expectError(MovieException.class)
                .verify();

        verify(reviewService, times(4))
                .retrieveReviewsFlux(isA(Long.class));

    }

    @Test
    void getAllMovies_retryWhen() {
        Mockito.when(movieInfoService.retrieveMoviesFlux())
                .thenCallRealMethod();

        Mockito.when(reviewService.retrieveReviewsFlux(Mockito.anyLong()))
                .thenThrow(new NetworkException("Exception ocurred in ReviewService"));

        var moviesFlux = reactiveMovieService.getAllMovies_retryWhen();

        StepVerifier.create(moviesFlux)
                .expectError(MovieException.class)
                .verify();

        verify(reviewService, times(4))
                .retrieveReviewsFlux(isA(Long.class));

    }

    @Test
    void getAllMovies_retryWhen_1() {
        var errorMessage = "Exception ocurred in ReviewService";
        Mockito.when(movieInfoService.retrieveMoviesFlux())
                .thenCallRealMethod();

        Mockito.when(reviewService.retrieveReviewsFlux(Mockito.anyLong()))
                .thenThrow(new ServiceException(errorMessage));

        var moviesFlux = reactiveMovieService.getAllMovies_retryWhen();

        StepVerifier.create(moviesFlux)
                //.expectError(MovieException.class)
                .expectErrorMessage(errorMessage)
                .verify();

        verify(reviewService, times(1))
                .retrieveReviewsFlux(isA(Long.class));
    }

}
