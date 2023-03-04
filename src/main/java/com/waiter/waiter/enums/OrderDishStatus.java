package com.waiter.waiter.enums;

public enum OrderDishStatus {
    TAKEN("taken"),//взета
    COOKING("cooking"),//готви се
    COOKED("cooked"),//сготвена
    SERVED("served");//сервирана
    private final String value;

    private OrderDishStatus(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
