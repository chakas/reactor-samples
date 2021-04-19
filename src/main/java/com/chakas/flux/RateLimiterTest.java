package com.chakas;


import org.reactivestreams.Publisher;
import org.reactivestreams.Subscriber;
import reactor.core.publisher.Mono;
import reactor.core.publisher.Operators;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Function;

class RateLimiter {
    private AtomicInteger myCounter;

    public RateLimiter(AtomicInteger myCounter) {
        this.myCounter = myCounter;
    }

    public AtomicInteger getMyCounter() {
        return myCounter;
    }

    public void setMyCounter(AtomicInteger myCounter) {
        this.myCounter = myCounter;
    }
}

class RateLimiterOperator {
    private static class RateLimiterPub<Object> implements Publisher<Object> {

        private final Mono<Object> source;
        private final RateLimiter rateLimiter;

        public RateLimiterPub(Mono<Object> source, RateLimiter rateLimiter) {
            this.rateLimiter = rateLimiter;
            this.source = source;
        }

        @Override
        public void subscribe(Subscriber<? super Object> s) {
            if (rateLimiter.getMyCounter().decrementAndGet() > -1) {
                source.subscribe(s);
            } else {
                Operators.error(s, new RuntimeException("boom"));
            }
        }
    }

    static <V> Function<Mono<Object>, Publisher<V>> of(RateLimiter rateLimiter) {
        return (objectMono) -> new RateLimiterPub(objectMono, rateLimiter);
    }
}

public class RateLimiterTest {
    public static void main(String[] args) throws InterruptedException {
        RateLimiter rateLimiter = new RateLimiter(new AtomicInteger(5));
        Mono<Object> i_am_invoked = Mono.fromRunnable(() -> System.out.println("i am invoked")).compose(RateLimiterOperator.of(rateLimiter));


        for (int i = 0; i < 10; i++) {
            i_am_invoked.subscribe(System.out::println, throwable -> System.out.println("Got some error"));
        }
        Thread.sleep(5000);
    }
}
