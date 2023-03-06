package com.waiter.waiter.enums;

public enum OrderStatus {
    TAKING("взима се"),
    TAKEN("взета"),//взета
    COOKING("готви се"),//готви се
    COOKED("сготвена"),//сготвена
    SERVED("сервирана"),//сервирана
    PAID("платена");
    private final String value;

    private OrderStatus(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
