package com.waiter.waiter.entities;

import com.waiter.waiter.enums.DrinkQuantityType;

import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.Size;

@Entity
@Table(name = "drinks")
public class Drink {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Size(min=2, max=30)
    private String name;
    @Size(max=150)
    private String description;
    @Min(0)
    private double price;
    @Min(1)
    private int quantity;
    private DrinkQuantityType drinkQuantityType;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public DrinkQuantityType getDrinkQuantityType() {
        return drinkQuantityType;
    }

    public void setDrinkQuantityType(DrinkQuantityType drinkQuantityType) {
        this.drinkQuantityType = drinkQuantityType;
    }
}
