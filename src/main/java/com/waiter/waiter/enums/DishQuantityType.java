package com.waiter.waiter.enums;

public enum DishQuantityType {
    KG("кг."),
    BR("бр."),
    GR("гр.");

    private final String value;

    private DishQuantityType(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

}
