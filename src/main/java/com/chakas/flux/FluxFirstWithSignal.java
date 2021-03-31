package com.chakas.flux;

import reactor.core.publisher.Flux;

import java.time.Duration;

public class FluxFirstWithSignal {
    public static void main(String[] args) throws InterruptedException {
        Flux<Integer> firstPublisher =
                Flux.just(1, 2, 3)
                        .doOnSubscribe(subscription -> System.out.println("First publisher subscribed"))
                        .doOnCancel(() -> System.out.println("First publisher subscription cancelled"))
                        .delayElements(Duration.ofMillis(100));

        Flux<Integer> secondPublisher =
                Flux.just(4, 5, 6)
                        .doOnSubscribe(subscription -> System.out.println("Second publisher subscribed"))
                        .doOnCancel(() -> System.out.println("Second publisher subscription cancelled"))
                        .delayElements(Duration.ofMillis(200));

        Flux.first(firstPublisher, secondPublisher)
                .doOnNext(integer -> System.out.println("Integer : " + integer))
                .subscribe();

        Thread.sleep(1000);
    }
}
