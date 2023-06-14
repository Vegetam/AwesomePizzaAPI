package com.fm.awesomePizza.test.service;

import java.util.Map;

import com.fm.awesomePizza.test.model.Order;


public interface OrderService {
    String placeOrder();
    void updateOrderStatus(String orderId, String status);
	String takeNextOrder();
	void completeOrder(String orderId);
	String placeOrder(String pizzaName, boolean isVegan, boolean isGluteenFree);

}