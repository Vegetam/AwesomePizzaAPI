package com.fm.awesomePizza.test.controller;

import com.fm.awesomePizza.test.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.fm.awesomePizza.test.model.Order;
import com.fm.awesomePizza.test.model.OrderRequest;
import com.fm.awesomePizza.test.model.OrderStatus;
import com.fm.awesomePizza.test.service.OrderService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.*;

@RestController
@RequestMapping("/orders")
public class OrderController {


    private final OrderService orderService;
    private final OrderRepository orderRepository;
    private final Map<String, Order> orderMap;
    private final List<Order> orderQueue;

    @Autowired
    public OrderController(OrderService orderService, OrderRepository orderRepository) {
        this.orderService = orderService;
        this.orderRepository = orderRepository;
        this.orderMap = new HashMap<>();
        this.orderQueue = new ArrayList<>();
    }

    public void setOrderQueue(List<Order> orderQueue) {
        this.orderQueue.clear();
        this.orderQueue.addAll(orderQueue);
    }

    @Operation(summary = "Effettua un nuovo ordine", tags = "User")
    @PostMapping("/user/order")
    public ResponseEntity<String> placeOrder(
            @RequestBody
            @Parameter(description = "Dettagli dell'ordine", required = true)
                    OrderRequest orderRequest) {
        String orderId = orderService.placeOrder();
        Order order = new Order(orderId, OrderStatus.PENDING, orderRequest.getPizzaName(), orderRequest.isVegan(), orderRequest.isGluteenFree());
        Order savedOrder = orderRepository.save(order);
        orderQueue.add(savedOrder);
        return ResponseEntity.ok(savedOrder.getOrderId());
    }

    @Operation(summary = "Segui lo stato dell'ordine", tags = "User")
    @GetMapping("/user/order/{orderId}")
    public ResponseEntity<String> getOrderStatus(@PathVariable String orderId) {
        Order order = orderRepository.findByOrderId(orderId).orElse(null);
        if (order != null) {
            return ResponseEntity.ok("Order " + orderId + " status: " + order.getStatus().toString());
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @Operation(summary = "Ordini arrivati", tags = "Chef")
    @GetMapping("/chef/orders")
    public ResponseEntity<List<Order>> getAllChefOrders() {
        List<Order> orders = orderRepository.findAll();
        return ResponseEntity.ok(orders);
    }

    @Operation(summary = "Prendi il prossimo ordine in coda", tags = "Chef")
    @PostMapping("/chef/takeNext")
    public ResponseEntity<String> takeNextChefOrder() {
        ResponseEntity<String> response = null;
        for (Order order : orderQueue) {
            if (order.getStatus() == OrderStatus.PENDING) {
                order.setStatus(OrderStatus.IN_PROGRESS);
                response = ResponseEntity.ok("Order " + order.getOrderId() + " taken by the pizza chef.");
                return response;
            } else if (order.getStatus() == OrderStatus.IN_PROGRESS) {
                response = ResponseEntity.ok("Order " + order.getOrderId() + " is already in progress.");
                return response;
            }
        }

        response = ResponseEntity.ok("No pending orders available.");
        return response;
    }

    @PutMapping("/chef/{orderId}")
    public ResponseEntity<String> updateChefOrderStatus(
            @PathVariable("orderId")
            @Parameter(description = "ID dell'ordine da aggiornare", required = true)
                    String orderId,
            @RequestParam("status")
            @Parameter(description = "Nuovo stato dell'ordine", required = true, schema = @Schema(allowableValues = {"PENDING", "IN_PROGRESS", "COMPLETED"}))
                    OrderStatus status) {
        Order order = orderRepository.findByOrderId(orderId).orElse(null);
        if (order != null) {
            order.setStatus(status);
            orderRepository.save(order);

            // Aggiorna l'ordine nella coda
            for (Order queuedOrder : orderQueue) {
                if (queuedOrder.getOrderId().equals(order.getOrderId())) {
                    queuedOrder.setStatus(status);
                    break;
                }
            }

            return ResponseEntity.ok("Order " + order.getOrderId() + " status updated to " + status.toString());
        } else {
            return ResponseEntity.notFound().build();
        }
    }



    public Map<String, Order> getOrderMap() {
        return orderMap;
    }

    public List<Order> getOrderQueue() {
        return orderQueue;
    }
}
