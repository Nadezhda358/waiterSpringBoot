package com.waiter.waiter.enums;

//public enum Role {
//    WAITER,
//    COOK,
//    ADMIN
//}

public enum Role {
    WAITER("Сервитьор"),
    COOK("Готвач");

    private final String value;

    private Role(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}