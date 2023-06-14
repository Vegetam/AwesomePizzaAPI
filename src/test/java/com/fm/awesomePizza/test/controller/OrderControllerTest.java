package com.fm.awesomePizza.test.controller;

import com.fm.awesomePizza.test.model.Order;
import com.fm.awesomePizza.test.model.OrderRequest;
import com.fm.awesomePizza.test.model.OrderStatus;
import com.fm.awesomePizza.test.repository.OrderRepository;
import com.fm.awesomePizza.test.service.OrderService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class OrderControllerTest {
    @Mock
    private OrderService orderService;

    @InjectMocks
    private OrderController orderController;

    @Mock
    private OrderRepository orderRepository;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testPlaceOrder() {
        // Create a sample order
        String orderId = "123456";
        OrderStatus orderStatus = OrderStatus.PENDING;
        String pizzaName = "Margherita";
        boolean isVegan = false;
        boolean isGlutenFree = true;

        OrderRequest orderRequest = new OrderRequest();
        orderRequest.setPizzaName(pizzaName);
        orderRequest.setVegan(isVegan);
        orderRequest.setGluteenFree(isGlutenFree);

        Order mockOrder = new Order(orderId, orderStatus, pizzaName, isVegan, isGlutenFree);
        when(orderService.placeOrder()).thenReturn(orderId);
        when(orderRepository.save(any(Order.class))).thenReturn(mockOrder);

        // Call the controller method
        ResponseEntity<String> response = orderController.placeOrder(orderRequest);

        // Verify the response
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(orderId, response.getBody());

        // Verify that the orderService and orderRepository were called
        verify(orderService, times(1)).placeOrder();
        verify(orderRepository, times(1)).save(any(Order.class));
    }


    @Test
    public void testGetOrderStatus() {
        // Create a sample order
        String orderId = "123456";
        List<Order> orderQueue = new ArrayList<>();
        orderQueue.add(new Order(orderId, OrderStatus.PENDING, "Margherita", false, true));

        // Create a mock of orderController
        OrderController orderControllerMock = mock(OrderController.class);

        // Set the orderQueue in the mock controller
        when(orderControllerMock.getAllChefOrders()).thenReturn(new ResponseEntity<>(orderQueue, HttpStatus.OK));

        // Call the controller method
        ResponseEntity<List<Order>> response = orderControllerMock.getAllChefOrders();

        // Verify the response
        assertEquals(HttpStatus.OK, response.getStatusCode());
        //assertEquals("Order " + orderId + " status: " + OrderStatus.PENDING.toString(), response.getBody());
        assertEquals(orderQueue, response.getBody());

        // Verify that the orderService was not called
        verifyZeroInteractions(orderService);

    }

    @Test
    public void testGetOrderStatus_OrderNotFound() {
        // Call the controller method with an invalid orderId
        ResponseEntity<String> response = orderController.getOrderStatus("invalidOrderId");

        // Verify the response
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());

        // Verify that the orderService was not called
        verifyZeroInteractions(orderService);
    }

    @Test
    public void testGetAllChefOrders() {
        // Create a sample order queue
        List<Order> orderQueue = new ArrayList<>();
        orderQueue.add(new Order("1", OrderStatus.PENDING, "Margherita", false, true));
        orderQueue.add(new Order("2", OrderStatus.IN_PROGRESS, "Pepperoni", true, false));
        orderQueue.add(new Order("3", OrderStatus.COMPLETED, "Hawaiian", false, false));

        // Create a mock of orderController
        OrderController orderControllerMock = mock(OrderController.class);

        // Set the orderQueue in the mock controller
        when(orderControllerMock.getAllChefOrders()).thenReturn(new ResponseEntity<>(orderQueue, HttpStatus.OK));

        // Call the controller method
        ResponseEntity<List<Order>> response = orderControllerMock.getAllChefOrders();

        // Verify the response
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(orderQueue, response.getBody());

        // Verify that the orderService was not called
        verifyZeroInteractions(orderService);
    }

    @Test
    public void testTakeNextChefOrder_OrderPending() {
        // Create a sample order queue with a pending order
        List<Order> orderQueue = new ArrayList<>();
        orderQueue.add(new Order("1", OrderStatus.PENDING, "Margherita", false, true));
        orderQueue.add(new Order("2", OrderStatus.IN_PROGRESS, "Pepperoni", true, false));
        orderQueue.add(new Order("3", OrderStatus.COMPLETED, "Hawaiian", false, false));
        setOrderQueueInController(orderQueue);


        // Call the controller method
        ResponseEntity<String> response = orderController.takeNextChefOrder();

        // Verify the response
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Order 1 taken by the pizza chef.", response.getBody());

        // Verify that the order status was updated
        List<Order> updatedOrderQueue = getOrderQueueFromController();
        assertEquals(OrderStatus.IN_PROGRESS, updatedOrderQueue.get(0).getStatus());

        // Verify that the orderService was not called
        verifyNoMoreInteractions(orderService);
    }

    @Test
    public void testTakeNextChefOrder_NoPending() {
        // Create a sample order queue with a pending order
        List<Order> orderQueue = new ArrayList<>();
        orderQueue.add(new Order("1", OrderStatus.COMPLETED, "Margherita", false, true));
        orderQueue.add(new Order("2", OrderStatus.COMPLETED, "Pepperoni", true, false));
        orderQueue.add(new Order("3", OrderStatus.COMPLETED, "Hawaiian", false, false));
        setOrderQueueInController(orderQueue);


        // Call the controller method
        ResponseEntity<String> response = orderController.takeNextChefOrder();

        // Verify the response
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("No pending orders available.", response.getBody());

        // Verify that the order status was updated
        List<Order> updatedOrderQueue = getOrderQueueFromController();
        assertEquals(OrderStatus.COMPLETED, updatedOrderQueue.get(0).getStatus());

        // Verify that the orderService was not called
        verifyNoMoreInteractions(orderService);
    }

    @Test
    public void testTakeNextChefOrder_PendingOrder() {
        // Create a sample order queue without any pending order
        List<Order> orderQueue = new ArrayList<>();
        orderQueue.add(new Order("1", OrderStatus.COMPLETED, "Margherita", false, true));
        orderQueue.add(new Order("2", OrderStatus.IN_PROGRESS, "Pepperoni", true, false));
        orderQueue.add(new Order("3", OrderStatus.COMPLETED, "Hawaiian", false, false));
        orderController.setOrderQueue(orderQueue);

        // Call the controller method
        ResponseEntity<String> response = orderController.takeNextChefOrder();

        // Verify the response
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Order 2 is already in progress.", response.getBody());

        // Verify that the order queue was not modified
        List<Order> updatedOrderQueue = orderController.getOrderQueue();
        assertEquals(orderQueue, updatedOrderQueue);
    }

    @Test
    public void testUpdateChefOrderStatus_OrderInProgress() {
        // Create a sample order queue
        List<Order> orderQueue = new ArrayList<>();
        orderQueue.add(new Order("1", OrderStatus.PENDING, "Margherita", false, true));
        orderQueue.add(new Order("2", OrderStatus.IN_PROGRESS, "Pepperoni", true, false));
        orderQueue.add(new Order("3", OrderStatus.COMPLETED, "Hawaiian", false, false));
        orderController.setOrderQueue(orderQueue);

        // Mock the behavior of orderRepository.findByOrderId()
        Order orderToUpdate = new Order("2", OrderStatus.IN_PROGRESS, "Pepperoni", true, false);
        when(orderRepository.findByOrderId("2")).thenReturn(Optional.of(orderToUpdate));

        // Call the controller method to update the status of order "2"
        ResponseEntity<String> response = orderController.updateChefOrderStatus("2", OrderStatus.COMPLETED);

        // Verify the response
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Order 2 status updated to COMPLETED", response.getBody());

        // Verify that the order status was updated
        List<Order> updatedOrderQueue = orderController.getOrderQueue();
        assertEquals(OrderStatus.COMPLETED, updatedOrderQueue.get(1).getStatus());

        // Verify that the orderRepository and orderService were called
        verify(orderRepository, times(1)).findByOrderId("2");
        verify(orderRepository, times(1)).save(orderToUpdate);
    }

    @Test
    public void testUpdateChefOrderStatus_OrderNotFound() {
        // Create a sample order queue
        List<Order> orderQueue = new ArrayList<>();
        orderQueue.add(new Order("1", OrderStatus.PENDING, "Margherita", false, true));
        orderQueue.add(new Order("2", OrderStatus.IN_PROGRESS, "Pepperoni", true, false));
        orderQueue.add(new Order("3", OrderStatus.COMPLETED, "Hawaiian", false, false));
        setOrderQueueInController(orderQueue);

        ResponseEntity<String> response = orderController.updateChefOrderStatus("invalidOrderId", OrderStatus.COMPLETED);

        // Verify the response
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());

        // Verify that the order queue was not modified
        List<Order> updatedOrderQueue = getOrderQueueFromController();
        assertEquals(orderQueue, updatedOrderQueue);
    }

    private List<Order> getOrderQueueFromController() {
        return orderController.getOrderQueue();
    }

    private void setOrderQueueInController(List<Order> orderQueue) {
        orderController.setOrderQueue(orderQueue);
    }

    private void addOrderToController(Order order) {
        orderController.getOrderQueue().add(order);
        orderController.getOrderMap().put(order.getOrderId(), order);
    }

}
