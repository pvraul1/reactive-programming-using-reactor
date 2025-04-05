package com.learnreactiveprogramming.service;

import com.learnreactiveprogramming.domain.Movie;
import com.learnreactiveprogramming.domain.Review;
import com.learnreactiveprogramming.exception.MovieException;
import com.learnreactiveprogramming.exception.NetworkException;
import com.learnreactiveprogramming.exception.ServiceException;
import java.time.Duration;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.Exceptions;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;
import reactor.util.retry.Retry;
import reactor.util.retry.RetryBackoffSpec;

/**
 * MovieReactiveService
 * <p>
 * Created by IntelliJ, Spring Framework Guru.
 *
 * @author architecture - pvraul
 * @version 28/03/2025 - 15:41
 * @since 1.17
 */
@RequiredArgsConstructor
@Slf4j
public class MovieReactiveService {

    private final MovieInfoService movieInfoService;
    private final ReviewService reviewService;
    private final RevenueService revenueService;

    public Flux<Movie> getAllMovies() {

        var moviesInfoFlux = movieInfoService.retrieveMoviesFlux();
        return moviesInfoFlux
                .flatMap(movieInfo -> {
                    Mono<List<Review>> reviewsMono = reviewService.retrieveReviewsFlux(movieInfo.getMovieInfoId())
                            .collectList();

                    return reviewsMono
                            .map(reviews -> new Movie(movieInfo, reviews));
                })
                .onErrorMap((exception) -> {
                    log.error("Exception is: {}", String.valueOf(exception));
                    throw new MovieException(exception.getMessage());
                })
                .log();
    }

    public Flux<Movie> getAllMovies_retry() {

        var moviesInfoFlux = movieInfoService.retrieveMoviesFlux();
        return moviesInfoFlux
                .flatMap(movieInfo -> {
                    Mono<List<Review>> reviewsMono = reviewService.retrieveReviewsFlux(movieInfo.getMovieInfoId())
                            .collectList();

                    return reviewsMono
                            .map(reviews -> new Movie(movieInfo, reviews));
                })
                .onErrorMap((exception) -> {
                    log.error("Exception is: {}", String.valueOf(exception));
                    throw new MovieException(exception.getMessage());
                })
                .retry(3)
                .log();
    }

    public Flux<Movie> getAllMovies_retryWhen() {
        var moviesInfoFlux = movieInfoService.retrieveMoviesFlux();
        return moviesInfoFlux
                .flatMap(movieInfo -> {
                    Mono<List<Review>> reviewsMono = reviewService.retrieveReviewsFlux(movieInfo.getMovieInfoId())
                            .collectList();

                    return reviewsMono
                            .map(reviews -> new Movie(movieInfo, reviews));
                })
                .onErrorMap((exception) -> {
                    log.error("Exception is: {}", String.valueOf(exception));
                    if (exception instanceof NetworkException) {
                        throw new MovieException(exception.getMessage());
                    } else {
                        throw new ServiceException(exception.getMessage());
                    }
                })
                .retryWhen(getRetryBackoffSpec())
                .log();
    }

    public Flux<Movie> getAllMovies_repeat() {
        var moviesInfoFlux = movieInfoService.retrieveMoviesFlux();
        return moviesInfoFlux
                .flatMap(movieInfo -> {
                    Mono<List<Review>> reviewsMono = reviewService.retrieveReviewsFlux(movieInfo.getMovieInfoId())
                            .collectList();

                    return reviewsMono
                            .map(reviews -> new Movie(movieInfo, reviews));
                })
                .onErrorMap((exception) -> {
                    log.error("Exception is: {}", String.valueOf(exception));
                    if (exception instanceof NetworkException) {
                        throw new MovieException(exception.getMessage());
                    } else {
                        throw new ServiceException(exception.getMessage());
                    }
                })
                .retryWhen(getRetryBackoffSpec())
                .repeat()
                .log();
    }

    public Flux<Movie> getAllMovies_repeat_n(long n) {
        var moviesInfoFlux = movieInfoService.retrieveMoviesFlux();
        return moviesInfoFlux
                .flatMap(movieInfo -> {
                    Mono<List<Review>> reviewsMono = reviewService.retrieveReviewsFlux(movieInfo.getMovieInfoId())
                            .collectList();

                    return reviewsMono
                            .map(reviews -> new Movie(movieInfo, reviews));
                })
                .onErrorMap((exception) -> {
                    log.error("Exception is: {}", String.valueOf(exception));
                    if (exception instanceof NetworkException) {
                        throw new MovieException(exception.getMessage());
                    } else {
                        throw new ServiceException(exception.getMessage());
                    }
                })
                .retryWhen(getRetryBackoffSpec())
                .repeat(n)
                .log();
    }

    private static RetryBackoffSpec getRetryBackoffSpec() {
        var retryWhen = Retry.backoff(3, Duration.ofMillis(500))
                .filter(exception -> exception instanceof MovieException)
                .onRetryExhaustedThrow((retryBackoffSpec, retrySignal) ->
                    Exceptions.propagate(retrySignal.failure())
                );
        return retryWhen;
    }

    public Mono<Movie> getMovieById(Long movieId) {
        var movieInfoMono = movieInfoService.retrieveMovieInfoMonoUsingId(movieId);
        var reviewsMono = reviewService.retrieveReviewsFlux(movieId).collectList();

        return movieInfoMono.zipWith(reviewsMono, (movieInfo, reviews) -> new Movie(movieInfo, reviews))
                .log();
    }

    public Mono<Movie> getMovieById_withRevenue(Long movieId) {
        var movieInfoMono = movieInfoService.retrieveMovieInfoMonoUsingId(movieId);
        var reviewsFlux = reviewService.retrieveReviewsFlux(movieId).collectList();

        var revenueMono = Mono.fromCallable(() -> revenueService.getRevenue(movieId))
                .subscribeOn(Schedulers.boundedElastic());

        return movieInfoMono
                .zipWith(reviewsFlux, (movieInfo, reviews) -> new Movie(movieInfo, reviews))
                .zipWith(revenueMono, (movie, revenue) -> {
                    movie.setRevenue(revenue);
                    return movie;
                })
                .log();
    }

    public Mono<Movie> getMovieById_flatMap(Long movieId) {
        var movieInfoMono = movieInfoService.retrieveMovieInfoMonoUsingId(movieId);

        return movieInfoMono
                .flatMap(movieInfo -> {
                    Mono<List<Review>> reviewsMono = reviewService.retrieveReviewsFlux(movieId)
                            .collectList();

                    return reviewsMono
                            .map(reviews -> new Movie(movieInfo, reviews));
                })
                .log();
    }

}
