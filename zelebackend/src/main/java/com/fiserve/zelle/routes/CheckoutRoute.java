package com.fiserve.zelle.routes;

import org.apache.camel.builder.RouteBuilder;

public class CheckoutRoute extends RouteBuilder {
    @Override
    public void configure() throws Exception {
        // This listens for internal Spring events or REST calls
        // and routes them to our Kafka topic
        from("direct:startCheckout")
                .log("Received order: ${body}")
                .marshal().json() // Converts the Java object to JSON
                .to("kafka:orders-topic?brokers=kafka.event-streaming.svc.cluster.local:9092")
                .log("Order sent to Kafka successfully!");
    }

}
