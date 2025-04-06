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

    @Test
    void hotPublisherTest_autoConnect() {
        var flux = Flux.range(1, 10)
                .delayElements(Duration.ofSeconds(1));

        var hotSouurce = flux.publish().autoConnect(2);

        hotSouurce.subscribe(i -> System.out.println("Subscriber 1: " + i));
        delay(2000);
        hotSouurce.subscribe(i -> System.out.println("Subscriber 2: " + i));
        System.out.println("Two subscribers are connected");
        delay(2000);
        hotSouurce.subscribe( i -> System.out.println("Subscriber 3: " + i));
        delay(10000);
    }

    @Test
    void hotPublisherTest_refCount() {
        var flux = Flux.range(1, 10)
                .delayElements(Duration.ofSeconds(1))
                .doOnCancel(() -> System.out.println("Received Cancel Signal"));

        var hotSouurce = flux.publish().refCount(2);

        var disposible = hotSouurce.subscribe(i -> System.out.println("Subscriber 1: " + i));
        delay(2000);
        var disposible1 = hotSouurce.subscribe(i -> System.out.println("Subscriber 2: " + i));
        System.out.println("Two subscribers are connected");
        delay(2000);
        disposible.dispose();
        System.out.println("Subscriber 1 is disposed");
        disposible1.dispose();
        System.out.println("Subscriber 2 is disposed");
        hotSouurce.subscribe( i -> System.out.println("Subscriber 3: " + i));
        delay(2000);
        hotSouurce.subscribe(i -> System.out.println("Subscriber 4: " + i));
        delay(10000);
    }

}
