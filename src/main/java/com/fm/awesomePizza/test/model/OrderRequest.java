package com.fm.awesomePizza.test.model;

public class OrderRequest {
    private String pizzaName;
    private boolean vegan;
    private boolean gluteenFree;

    // Getters and setters

    public String getPizzaName() {
        return pizzaName;
    }

    public void setPizzaName(String pizzaName) {
        this.pizzaName = pizzaName;
    }

    public boolean isVegan() {
        return vegan;
    }

    public void setVegan(boolean vegan) {
        this.vegan = vegan;
    }
    
    public boolean isGluteenFree() {
    	return gluteenFree;
    }
    
    public void setGluteenFree(boolean gluteenFree){
    	this.gluteenFree = gluteenFree;
    }
}
