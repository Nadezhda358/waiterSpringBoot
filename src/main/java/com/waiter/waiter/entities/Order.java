package com.waiter.waiter.entities;

import com.waiter.waiter.enums.OrderStatus;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name="orders")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, columnDefinition = "INT(11) UNSIGNED")
    private Integer id;
    @DateTimeFormat(pattern = "yyyy-mm-dd")
    private LocalDate createdOn;
    @ManyToOne
    @JoinColumn(name = "table_id")
    private RestaurantTable table;
    @ManyToOne
    @JoinColumn(name = "waiter_id")
    private User waiter;
    @Enumerated(EnumType.STRING)
    private OrderStatus status;
    @DateTimeFormat(pattern = "yyyy-mm-dd")
    private LocalDate finishDate;
    private double totalCost;

    public Order() {
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

    public LocalDate getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(LocalDate createdOn) {
        this.createdOn = createdOn;
    }

    public RestaurantTable getTable() {
        return table;
    }

    public void setTable(RestaurantTable table) {
        this.table = table;
    }

    public OrderStatus getStatus() {
        return status;
    }

    public void setStatus(OrderStatus status) {
        this.status = status;
    }

    public LocalDate getFinishDate() {
        return finishDate;
    }

    public void setFinishDate(LocalDate finishDate) {
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
}
