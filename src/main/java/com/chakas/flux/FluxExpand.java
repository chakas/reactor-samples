package com.chakas.flux;

import org.reactivestreams.Publisher;
import reactor.core.publisher.Flux;

import java.util.List;

public class FluxExpand {
    static class Node {
        String element;
        List<Node> child;

        public List<Node> getChild() {
            return child;
        }

        public void setChild(List<Node> child) {
            this.child = child;
        }

        public String getElement() {
            return element;
        }

        public Node(String element) {
            this.element = element;
        }

        @Override
        public String toString() {
            return "Node{" +
                    "element='" + element + '\'' +
                    '}';
        }
    }


    public static void main(String[] args) {
       /* Recursive data
        A
                - AA
                - aa1
        */

        List<Node> anodes = List.of(new Node("AA"), new Node("aa1"));
        Node aNode = new Node("A");
        aNode.setChild(anodes);

        Flux.just(aNode)
                .doOnNext(node -> System.out.println("node = " + node))
                .subscribe();

        Flux.just(aNode)
                .expand(FluxExpand::expandNode)
                .doOnNext(node -> System.out.println("node = " + node))
                .subscribe();
    }

    private static Publisher<? extends Node> expandNode(Node node) {
        return (node != null && node.child != null) && node.getChild().size() > 0
                ? Flux.fromIterable(node.child) : Flux.empty();
    }
}
