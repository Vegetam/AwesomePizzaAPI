package com.fm.awesomePizza.test.model;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class OrderRequestTest {

    @Test
    public void testOrderRequestSettersAndGetters() {
        // Create an OrderRequest instance
        OrderRequest orderRequest = new OrderRequest();

        // Set values using the setters
        orderRequest.setPizzaName("Margherita");
        orderRequest.setVegan(true);
        orderRequest.setGluteenFree(false);

        // Verify the values using the getters
        assertEquals("Margherita", orderRequest.getPizzaName());
        assertTrue(orderRequest.isVegan());
        assertFalse(orderRequest.isGluteenFree());
    }
}