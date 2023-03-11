package com.waiter.waiter.entities;

import com.waiter.waiter.enums.OrderStatus;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name="orders")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, columnDefinition = "INT(11) UNSIGNED")
    private Integer id;
    @DateTimeFormat(pattern = "yyyy-MM-dd-HH-mm")
    private LocalDateTime createdOn;
    @ManyToOne
    @JoinColumn(name = "table_id")
    private RestaurantTable table;
    @ManyToOne
    @JoinColumn(name = "waiter_id")
    private User waiter;

    @ManyToOne
    @JoinColumn(name = "cook_id")
    private User cook;
    private boolean isPaid=false;
    @Enumerated(EnumType.STRING)
    private OrderStatus orderStatus;
    @DateTimeFormat(pattern = "yyyy-mm-dd")
    private LocalDateTime finishDate;
    private double totalCost;

    public Order(RestaurantTable table, User user) {
        this.table = table;
        this.waiter=user;
        this.createdOn=LocalDateTime.now();
        this.orderStatus=OrderStatus.TAKING;
    }
    public Order(){}

    public Order(Integer id, LocalDateTime createdOn, RestaurantTable table, User waiter, User cook, boolean isPaid, OrderStatus orderStatus, LocalDateTime finishDate, double totalCost) {
        this.id = id;
        this.createdOn = createdOn;
        this.table = table;
        this.waiter = waiter;
        this.cook = cook;
        this.isPaid = isPaid;
        this.orderStatus = orderStatus;
        this.finishDate = finishDate;
        this.totalCost = totalCost;
    }

    public User getCook() {
        return cook;
    }

    public void setCook(User cook) {
        this.cook = cook;
    }

    public OrderStatus getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(OrderStatus orderStatus) {
        this.orderStatus = orderStatus;
    }

    public User getUser() {
        return waiter;
    }

    public void setUser(User user) {
        this.waiter = user;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public LocalDateTime getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(LocalDateTime createdOn) {
        this.createdOn = createdOn;
    }

    public RestaurantTable getTable() {
        return table;
    }

    public void setTable(RestaurantTable table) {
        this.table = table;
    }

    public boolean isPaid() {
        return isPaid;
    }

    public void setPaid(boolean paid) {
        isPaid = paid;
    }

    public LocalDateTime getFinishDate() {
        return finishDate;
    }

    public void setFinishDate(LocalDateTime finishDate) {
        this.finishDate = finishDate;
    }

    public double getTotalCost() {
        return totalCost;
    }

    public void setTotalCost(double totalCost) {
        this.totalCost = totalCost;
    }

    public User getWaiter() {
        return waiter;
    }

    public void setWaiter(User waiter) {
        this.waiter = waiter;
    }
    //in service orderCreation
    //DateTimeFormatter dtf;
    //        LocalDateTime currentDateTime= LocalDateTime.of(LocalDate.now(),LocalTime.now());
    //        dtf=DateTimeFormatter.ofPattern("HH:mm d-MM-yy");

    public Order(Integer id) {
        this.id = id;
    }
}
