package com.learnreactiveprogramming;


import java.time.Duration;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.ConnectableFlux;
import reactor.core.publisher.Flux;

import static com.learnreactiveprogramming.util.CommonUtil.delay;

public class ColdAndHotPublisherTest {

    @Test
    void coldPublisherTest() {
        var flux = Flux.range(1, 10);

        flux.subscribe(i -> System.out.println("Subscriber 1: " + i));

        flux.subscribe(i -> System.out.println("Subscriber 2: " + i));
    }

    @Test
    void hotPublisherTest() {
        var flux = Flux.range(1, 10)
                .delayElements(Duration.ofSeconds(1));

        ConnectableFlux<Integer> connectableFlux = flux.publish();
        connectableFlux.connect();

        connectableFlux.subscribe(i -> System.out.println("Subscriber 1: " + i));
        delay(4000);

        connectableFlux.subscribe(i -> System.out.println("Subscriber 2: " + i));
        delay(2000);
    }

}
