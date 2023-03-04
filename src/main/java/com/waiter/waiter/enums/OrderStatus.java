package com.waiter.waiter.enums;

public enum OrderStatus {
    TAKING("taking"),//взима се все още
   /* COOKING("cooking"),//готви се
    COOKED("cooked"),//сготвена
    SERVED("served"),//сервирана*/
    PAID("paid");//завършена

    private final String value;

    private OrderStatus(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
