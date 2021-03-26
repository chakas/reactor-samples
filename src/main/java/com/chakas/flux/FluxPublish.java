package com.chakas.flux;

import reactor.core.publisher.Flux;
import java.io.Serializable;

public class FluxPublish {
    public static void main(String[] args) {
        Flux<? extends Serializable> just = Flux.just("a", 1, "b", 2, "c", 3)
                    .doOnSubscribe(subscription -> {
                        System.out.println("Someone subscribed : "+ subscription.toString());
                    });
        
        // replace publish with compose or transform operator to see the side-effect
        just.publish(flux -> Flux.concat(
                flux.ofType(Integer.class).compose(integerFlux -> integerFlux.map(i -> i + 1)),
                flux.ofType(String.class).compose(integerFlux -> integerFlux.map(i -> i.toUpperCase())),
                flux.ofType(Float.class).compose(integerFlux -> integerFlux.map(i -> i))
        ))
        .subscribe(serializable -> {
            System.out.println(serializable);
        });
    }
}
