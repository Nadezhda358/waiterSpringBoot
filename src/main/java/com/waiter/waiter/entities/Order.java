package com.waiter.waiter.entities;

import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
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

    //todo enums for status
    @NotNull
    @Size(min=1)
    private Integer idTable;
    /*@ManyToOne
    @JoinColumn(name = "user_id")
    private User user;*/
    private String status;
    @DateTimeFormat(pattern = "yyyy-mm-dd")
    private LocalDate finishDate;
    private double totalCost;
/*
    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }*/

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

    public Integer getIdTable() {
        return idTable;
    }

    public void setIdTable(Integer idTable) {
        this.idTable = idTable;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
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
//in service orderCreation
    //DateTimeFormatter dtf;
    //        LocalDateTime currentDateTime= LocalDateTime.of(LocalDate.now(),LocalTime.now());
    //        dtf=DateTimeFormatter.ofPattern("HH:mm d-MM-yy");
}
