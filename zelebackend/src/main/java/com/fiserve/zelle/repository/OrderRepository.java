package com.fiserve.zelle.repository;

import com.fiserve.zelle.model.OrderEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<OrderEntity,String> {
}
