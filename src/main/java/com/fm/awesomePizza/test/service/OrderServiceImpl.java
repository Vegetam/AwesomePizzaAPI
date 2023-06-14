package com.fm.awesomePizza.test.service;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;


import com.fm.awesomePizza.test.model.Order;
import com.fm.awesomePizza.test.model.OrderStatus;

import org.springframework.stereotype.Service;

@Service
public class OrderServiceImpl implements OrderService {
    private Map<String, Order> orders = new HashMap<>();
    private AtomicInteger orderIdCounter = new AtomicInteger(1); // Aggiungiamo un contatore atomico per generare l'ID



    public void setOrders(Map<String, Order> orders) {
        this.orders = orders;
    }

    @Override
    public String placeOrder() {
        String orderId = generateOrderId();
        Order order = new Order(orderId, OrderStatus.PENDING, null, false, false);
        orders.put(orderId, order);
        return orderId;
    }

    @Override
    public String placeOrder(String pizzaName, boolean isVegan, boolean isGluteenFree) {
        String orderId = generateOrderId();
        Order order = new Order(orderId, OrderStatus.PENDING, pizzaName, isVegan, isGluteenFree);
        orders.put(orderId, order);
        return orderId;
    }

    @Override
    public void updateOrderStatus(String orderId, String status) {
        Order order = orders.get(orderId);
        if (order != null) {
            order.setStatus(OrderStatus.valueOf(status));
        }
    }

	private String generateOrderId() {
        return "ORD-" + orderIdCounter.getAndIncrement(); // Generiamo un ID univoco utilizzando il contatore atomico
    }

	public String takeNextOrder() {
        // Trova il primo ordine in stato "PENDING" nella coda
        Optional<Order> nextOrder = orders.values().stream()
                .filter(order -> order.getStatus() == OrderStatus.PENDING)
                .findFirst();

        if (nextOrder.isPresent()) {
            Order order = nextOrder.get();
            order.setStatus(OrderStatus.IN_PROGRESS);
            return order.getOrderId();
        }

        return null; // Nessun ordine disponibile
    }

	public void completeOrder(String orderId) {
        Order order = orders.get(orderId);
        if (order != null && order.getStatus() == OrderStatus.IN_PROGRESS) {
            order.setStatus(OrderStatus.COMPLETED);
        }
    }
}
