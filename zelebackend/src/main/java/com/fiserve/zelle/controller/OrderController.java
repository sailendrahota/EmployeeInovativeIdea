package com.fiserve.zelle.controller;

import com.fiserve.zelle.model.OrderEvent;
import org.apache.camel.ProducerTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class OrderController {

    @Autowired
    private ProducerTemplate producerTemplate;

    @PostMapping("/checkout")
    public ResponseEntity<String> checkout(@RequestBody OrderEvent order) {
        // We fire-and-forget to the Camel route, which handles the Kafka complexity
        producerTemplate.sendBody("direct:startCheckout", order);

        return ResponseEntity.accepted().body("Order is being processed!");
    }
}
