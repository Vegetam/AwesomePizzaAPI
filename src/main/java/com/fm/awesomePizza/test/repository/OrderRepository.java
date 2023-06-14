package com.fm.awesomePizza.test.repository;

import com.fm.awesomePizza.test.model.Order;
import com.fm.awesomePizza.test.model.OrderStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface OrderRepository extends JpaRepository<Order, UUID> {
    Optional<Order> findById(UUID id);
    Optional<Order> findFirstByStatus(OrderStatus status);
    Optional<Order> findByOrderId(String orderId);

}