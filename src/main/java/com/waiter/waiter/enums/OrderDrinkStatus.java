package com.waiter.waiter.enums;

public enum OrderDrinkStatus {
    TAKEN("taken"),//взета
    SERVED("served");//сервирана
    private final String value;

    private OrderDrinkStatus(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
