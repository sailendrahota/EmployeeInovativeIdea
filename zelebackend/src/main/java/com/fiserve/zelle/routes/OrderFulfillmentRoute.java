package com.fiserve.zelle.routes;

import com.fiserve.zelle.model.OrderEvent;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.model.dataformat.JsonLibrary;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class OrderFulfillmentRoute extends RouteBuilder {

    @Value("${kafka.brokers:kafka.event-streaming.svc.cluster.local:9092}")
    private String kafkaBrokers;

    @Override
    public void configure() throws Exception {

        from("kafka:orders-topic?brokers=" + kafkaBrokers + "&groupId=fulfillment-service-group")
                .log("📥 Picked up new order event from Kafka: ${body}")

                // Unmarshal directly into the Java Record
                .unmarshal().json(JsonLibrary.Jackson, OrderEvent.class)

                // Hand off the Record to the Spring Bean
                .bean("orderFulfillmentService", "processAndSaveOrder")

                .log("🎉 Order fulfillment processing complete!");
    }
}