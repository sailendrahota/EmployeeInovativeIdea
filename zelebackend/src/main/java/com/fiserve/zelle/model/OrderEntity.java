package com.fiserve.zelle.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table(name = "orders")
@Data
public class OrderEntity {

    @Id
    private String orderId;
    private String status;
    private double amount;
    private String customerEmail;

    // Default constructor required by JPA
    public OrderEntity() {
    }

    public OrderEntity(String orderId, String status, double amount, String customerEmail) {
        this.orderId = orderId;
        this.status = status;
        this.amount = amount;
        this.customerEmail = customerEmail;
    }
}
