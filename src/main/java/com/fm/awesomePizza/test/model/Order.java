package com.fm.awesomePizza.test.model;
import javax.persistence.Table;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.UUID;

@Entity
@Table(name = "pizza_orders")
public class Order {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private UUID id;
	private String orderId;
	private OrderStatus status;
	private String pizza;
	private boolean vegan;
	private boolean glutenFree;

	// Costruttori, getter e setter

	public Order() {
		// Costruttore vuoto richiesto da JPA
	}

	public Order(String orderId, OrderStatus status, String pizzaName, boolean vegan, boolean glutenFree) {
		this.orderId = orderId;
		this.status = status;
		this.pizza = pizzaName;
		this.vegan = vegan;
		this.glutenFree = glutenFree;
	}

	public UUID getId() {
		return id;
	}

	public void setId(UUID id) {
		this.id = id;
	}

	// Rimuovi gli annotation getter/setter per l'orderId, poiché non corrisponde a una colonna nel database
	public String getOrderId() {
		return orderId;
	}

	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}

	// Rimuovi gli annotation getter/setter per lo status, poiché sarà mappato implicitamente come una colonna nel database
	public OrderStatus getStatus() {
		return status;
	}

	public void setStatus(OrderStatus status) {
		this.status = status;
	}

	// Rimuovi gli annotation getter/setter per pizza, vegan e glutenFree, poiché saranno mappati implicitamente come colonne nel database
	public String getPizza() {
		return pizza;
	}

	public void setPizza(String pizza) {
		this.pizza = pizza;
	}

	public boolean isVegan() {
		return vegan;
	}

	public void setVegan(boolean vegan) {
		this.vegan = vegan;
	}

	public boolean isGlutenFree() {
		return glutenFree;
	}

	public void setGlutenFree(boolean glutenFree) {
		this.glutenFree = glutenFree;
	}
}
