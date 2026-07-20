package com.fiserve.zelle.routes;

import org.apache.camel.builder.RouteBuilder;
import org.springframework.stereotype.Component;

@Component
public class CheckoutRoute extends RouteBuilder {
    @Override
    public void configure() throws Exception {
        // This listens for internal Spring events or REST calls
        // and routes them to our Kafka topic
        from("direct:startCheckout")
                .log("Received order: ${body}")
                .marshal().json() // Converts the Java object to JSON
                // This tells Camel: "Use the 'kafka.brokers' property, but if it doesn't exist, default to the Kubernetes URL"
                .to("kafka:orders-topic?brokers={{kafka.brokers:kafka.event-streaming.svc.cluster.local:9092}}")
                .log("Order sent to Kafka successfully!");
    }

}
