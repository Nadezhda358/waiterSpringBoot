package com.waiter.waiter.enums;

public enum Gender {
    MALE("Мъж"),
    FEMALE("Жена"),
    OTHER("Друг");

    private final String value;

    private Gender(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
