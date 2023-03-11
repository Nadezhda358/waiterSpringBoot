package com.waiter.waiter.helpingClasses;

import com.waiter.waiter.entities.Drink;
import com.waiter.waiter.entities.Order;

import java.util.List;

public class OrderDrinkHelp {
    private Order order;
    private List<Drink> drinks;

    public OrderDrinkHelp(Order order) {
        this.order = order;
    }

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    public List<Drink> getDrinks() {
        return drinks;
    }

    public void setDrinks(List<Drink> drinks) {
        this.drinks = drinks;
    }

    @Override
    public String toString() {
        return "OrderDrinkHelp{" +
                "order=" + order +
                ", drinks=" + drinks +
                '}';
    }
}
