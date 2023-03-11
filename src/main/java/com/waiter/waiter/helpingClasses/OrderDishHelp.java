package com.waiter.waiter.helpingClasses;

import com.waiter.waiter.entities.Dish;
import com.waiter.waiter.entities.Order;

import java.util.List;

public class OrderDishHelp {
    private Order order;
    private List<Dish> dishes;

    public OrderDishHelp(Order order) {this.order = order;}

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    public List<Dish> getDishes() {
        return dishes;
    }

    public void setDishes(List<Dish> dishes) {
        this.dishes = dishes;
    }

}
