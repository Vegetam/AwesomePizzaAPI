package com.fm.awesomePizza.test.service;

import com.fm.awesomePizza.test.model.Order;
import com.fm.awesomePizza.test.model.OrderStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.Map;
import java.util.HashMap;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class OrderServiceImplTest {

    private OrderServiceImpl orderService;
    private Map<String, Order> orders;

    @BeforeEach
    public void setUp() {
    	   orderService = new OrderServiceImpl();
    	    orders = new HashMap<>();
    	    orderService.setOrders(orders);
    }

    @Test
    public void testPlaceOrder() {
        String orderId = orderService.placeOrder();

        assertNotNull(orderId);
        assertTrue(orders.containsKey(orderId));

        Order order = orders.get(orderId);
        assertEquals(OrderStatus.PENDING, order.getStatus());
        assertNull(order.getPizza());
        assertFalse(order.isVegan());
        assertFalse(order.isGlutenFree());
    }

    @Test
    public void testPlaceOrder_WithPizzaDetails() {
        String pizzaName = "Margherita";
        boolean isVegan = true;
        boolean isGlutenFree = false;

        String orderId = orderService.placeOrder(pizzaName, isVegan, isGlutenFree);

        assertNotNull(orderId);
        assertTrue(orders.containsKey(orderId));

        Order order = orders.get(orderId);
        assertEquals(OrderStatus.PENDING, order.getStatus());
        assertEquals(pizzaName, order.getPizza());
        assertEquals(isVegan, order.isVegan());
        assertEquals(isGlutenFree, order.isGlutenFree());
    }

    @Test
    public void testUpdateOrderStatus_OrderExists() {
        String orderId = "ORD-1";
        Order order = new Order(orderId, OrderStatus.PENDING, "Margherita", false, false);
        orders.put(orderId, order);

        orderService.updateOrderStatus(orderId, OrderStatus.IN_PROGRESS.name());

        assertEquals(OrderStatus.IN_PROGRESS, order.getStatus());
    }

    @Test
    public void testUpdateOrderStatus_OrderNotFound() {
        String orderId = "invalidOrderId";
        String status = OrderStatus.IN_PROGRESS.name();

        orderService.updateOrderStatus(orderId, status);

        // Verify that no changes were made to the orders map
        assertFalse(orders.containsKey(orderId));
    }

    @Test
    public void testTakeNextOrder_OrderAvailable() {
        Order order1 = new Order("ORD-1", OrderStatus.PENDING, "Margherita", false, false);
        Order order2 = new Order("ORD-2", OrderStatus.PENDING, "Pepperoni", true, true);
        orders.put(order1.getOrderId(), order1);
        orders.put(order2.getOrderId(), order2);

        String orderId = orderService.takeNextOrder();

        assertNotNull(orderId);
        assertEquals(OrderStatus.IN_PROGRESS, order2.getStatus());
        assertEquals(order2.getOrderId(), orderId);
    }

    @Test
    public void testTakeNextOrder_NoOrderAvailable() {
        String orderId = orderService.takeNextOrder();

        assertNull(orderId);

        // Verify that no changes were made to the orders map
        assertTrue(orders.isEmpty());
    }

    @Test
    public void testCompleteOrder_OrderExistsAndInProgress() {
        String orderId = "ORD-1";
        Order order = new Order(orderId, OrderStatus.IN_PROGRESS, "Margherita", false, false);
        orders.put(orderId, order);

        orderService.completeOrder(orderId);

        assertEquals(OrderStatus.COMPLETED, order.getStatus());
    }

    @Test
    public void testCompleteOrder_OrderNotFound() {
        String orderId = "invalidOrderId";

        orderService.completeOrder(orderId);

        // Verify that no changes were made to the orders map
        assertFalse(orders.containsKey(orderId));
    }
}
