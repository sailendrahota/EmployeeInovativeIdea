package com.fiserve.zelle.model;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;


public record OrderEvent(String orderId, String status, BigDecimal amount,
                         String customerEmail) implements Serializable {
}

