package com.learnreactiveprogramming;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.reactivestreams.Subscription;
import reactor.core.publisher.BaseSubscriber;
import reactor.core.publisher.Flux;

import static org.junit.jupiter.api.Assertions.assertTrue;

@Slf4j
public class BackpressureTest {

    @Test
    void testBackpressure() {
        var numberRange = Flux.range(1, 100 ).log();

        numberRange
                .subscribe(new BaseSubscriber<Integer>() {

                    @Override
                    protected void hookOnSubscribe(Subscription subscription) {
                        request(2); // Request 2 items at a time
                    }

                    @Override
                    protected void hookOnNext(Integer value) {
                        //super.hookOnNext(value);
                        log.info("hookOnNext : {}", value);
                        if (value == 2) {
                            cancel();
                        }
                    }

                    @Override
                    protected void hookOnComplete() {
                        super.hookOnComplete();
                    }

                    @Override
                    protected void hookOnError(Throwable throwable) {
                        super.hookOnError(throwable);
                    }

                    @Override
                    protected void hookOnCancel() {
                        //super.hookOnCancel();
                        log.info("Inside OnCancel");
                    }

                });
                /*
                .subscribe(num -> {
                    log.info("Number is: {}", num);
                });
                */

    }

    @Test
    void testBackpressure_1() throws InterruptedException {
        var numberRange = Flux.range(1, 100 ).log();

        CountDownLatch countDownLatch = new CountDownLatch(1);
        numberRange
                .subscribe(new BaseSubscriber<Integer>() {

                    @Override
                    protected void hookOnSubscribe(Subscription subscription) {
                        request(2); // Request 2 items at a time
                    }

                    @Override
                    protected void hookOnNext(Integer value) {
                        //super.hookOnNext(value);
                        log.info("hookOnNext : {}", value);
                        if (value % 2 == 0 || value < 50) {
                            request(2);
                        } else {
                            cancel();
                        }
                    }

                    @Override
                    protected void hookOnComplete() {
                        super.hookOnComplete();
                    }

                    @Override
                    protected void hookOnError(Throwable throwable) {
                        super.hookOnError(throwable);
                    }

                    @Override
                    protected void hookOnCancel() {
                        //super.hookOnCancel();
                        log.info("Inside OnCancel");
                        countDownLatch.countDown();
                    }

                });
                /*
                .subscribe(num -> {
                    log.info("Number is: {}", num);
                });
                */
        assertTrue(countDownLatch.await(5L, TimeUnit.SECONDS));
    }

}
