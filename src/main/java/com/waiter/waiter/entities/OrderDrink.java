package com.waiter.waiter.entities;

import com.waiter.waiter.enums.OrderDishStatus;
import com.waiter.waiter.enums.OrderDrinkStatus;

import javax.persistence.*;

@Entity
@Table(name="orders_drinks")
public class OrderDrink {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, columnDefinition = "INT(11) UNSIGNED")
    private Integer id;
    @ManyToOne
    @JoinColumn(name = "order_id")
    private Order order;
    @ManyToOne
    @JoinColumn(name = "drink_id")
    private Drink drink;
    private int quantity;
    private double currentPrice;//за записа
    @Enumerated(EnumType.STRING)
    private OrderDrinkStatus status;
    public Drink getDrink() {
        return drink;
    }

    public void setDrink(Drink drink) {
        this.drink = drink;
    }

    public OrderDrink() {
    }

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public double getCurrentPrice() {
        return currentPrice;
    }

    public void setCurrentPrice(double currentPrice) {
        this.currentPrice = currentPrice;
    }

    public OrderDrinkStatus getStatus() {
        return status;
    }

    public void setStatus(OrderDrinkStatus status) {
        this.status = status;
    }
}
