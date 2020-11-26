package com.chakas.flux;

import reactor.core.publisher.Flux;

import java.time.Duration;

public class FluxShare {
    public static void main(String[] args) throws InterruptedException {

        Flux<Long> sharedFlux = Flux.interval(Duration.ofMillis(200)).share();

        sharedFlux.subscribe(integer -> System.out.println("Sub 1 " + integer));
        Thread.sleep(1000);
        sharedFlux.subscribe(integer -> System.out.println("Sub 2 " + integer));

        Thread.sleep(5000);
    }
}
