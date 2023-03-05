package com.waiter.waiter.entities;

import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.time.LocalDate;
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
    private boolean isPaid=false;
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
