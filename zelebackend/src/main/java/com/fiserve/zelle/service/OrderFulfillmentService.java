package com.fiserve.zelle.service;

import com.fiserve.zelle.model.OrderEntity;
import com.fiserve.zelle.model.OrderEvent;
import com.fiserve.zelle.repository.OrderRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service("orderFulfillmentService")
public class OrderFulfillmentService {

    private final OrderRepository orderRepository;

    public OrderFulfillmentService(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    @Transactional
    public void processAndSaveOrder(OrderEvent event) {
        // Notice we access record components using event.orderId() instead of getOrderId()
        OrderEntity entity = new OrderEntity(
                event.orderId(),
                "PROCESSING", // Set the processing status for the DB
                event.amount().doubleValue(),
                event.customerEmail()
        );

        orderRepository.save(entity);
        System.out.println("✅ Order " + event.orderId() + " successfully persisted to MySQL!");
    }
}