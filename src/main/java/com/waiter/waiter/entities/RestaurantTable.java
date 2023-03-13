package com.waiter.waiter.entities;

import javax.persistence.*;
import javax.validation.constraints.Min;
@Entity
@Table(name = "tables")
public class RestaurantTable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, columnDefinition = "INT(11) UNSIGNED")
    private Integer id;
    @Min(1)
    @Column(unique = true,name="number")//todo validation in html for unique name
    private int number;//register input done
    private boolean hasOrder = false;

    public RestaurantTable() {

    }

    public boolean isHasOrder() {
        return hasOrder;
    }

    public void setHasOrder(boolean hasOrder) {
        this.hasOrder = hasOrder;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }
}
