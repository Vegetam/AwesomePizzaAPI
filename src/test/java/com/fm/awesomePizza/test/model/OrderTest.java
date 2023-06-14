package com.fm.awesomePizza.test.model;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class OrderTest {

    @Test
    public void testOrderConstructorAndGetters() {
        // Create an order using the constructor
        Order order = new Order("1", OrderStatus.PENDING, "Margherita", true, false);

        // Verify the values using the getters
        assertEquals("1", order.getOrderId());
        assertEquals(OrderStatus.PENDING, order.getStatus());
        assertEquals("Margherita", order.getPizza());
        assertTrue(order.isVegan());
        assertFalse(order.isGlutenFree());
    }

    @Test
    public void testOrderSetters() {
        // Create an order
        Order order = new Order("1", OrderStatus.PENDING, "Margherita", true, false);

        // Set new values using the setters
        order.setOrderId("2");
        order.setStatus(OrderStatus.IN_PROGRESS);
        order.setPizza("Pepperoni");
        order.setVegan(false);
        order.setGlutenFree(true);

        // Verify the new values using the getters
        assertEquals("2", order.getOrderId());
        assertEquals(OrderStatus.IN_PROGRESS, order.getStatus());
        assertEquals("Pepperoni", order.getPizza());
        assertFalse(order.isVegan());
        assertTrue(order.isGlutenFree());
    }
}