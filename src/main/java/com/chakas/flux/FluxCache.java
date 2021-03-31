package com.chakas.flux;

import reactor.core.publisher.Flux;
import reactor.util.function.Tuple2;

import java.time.Duration;

public class FluxCache {
    public static void main(String[] args) throws InterruptedException {
        /**************************** Without Cache ********************************************/
        Flux<Tuple2<Long, Integer>> withoutCachedFlux = Flux.range(1, 10)
                .delayElements(Duration.ofMillis(500))
                .elapsed();

        withoutCachedFlux.subscribe(data -> {
            System.out.println("Subscriber 1: Elapsed=" + data.getT1() + " ,data="+ data.getT2());
        });
        withoutCachedFlux.subscribe(data -> {
            System.out.println("Subscriber 2: Elapsed=" + data.getT1() + " ,data="+ data.getT2());
        });
        Thread.sleep(11000);
        /**************************** With Cache ********************************************/
        Flux<Tuple2<Long, Integer>> cachedFlux = Flux.range(1, 10)
                .delayElements(Duration.ofMillis(500))
                .cache()
                .elapsed();

        cachedFlux.subscribe(data -> {
            System.out.println("Subscriber 3: Elapsed=" + data.getT1() + " ,data="+ data.getT2());
        });
        Thread.sleep(5000);
        cachedFlux.subscribe(data -> {
            System.out.println("Subscriber 4: Elapsed=" + data.getT1() + " ,data="+ data.getT2());
        });
        Thread.sleep(15000);
    }
}
