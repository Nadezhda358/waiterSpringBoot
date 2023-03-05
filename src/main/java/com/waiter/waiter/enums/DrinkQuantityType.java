package com.waiter.waiter.enums;

public enum DrinkQuantityType {
    ML("мл"),
    L("л");

    private final String value;

    private DrinkQuantityType(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    }
