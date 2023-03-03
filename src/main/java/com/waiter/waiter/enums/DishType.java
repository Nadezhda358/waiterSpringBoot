package com.waiter.waiter.enums;

public enum DishType {
    SALAD("Салати"),
    STARTERS("Предястия"),
    MAIN_COURSES("Основно"),
    GRILL("Барбекю"),
    SEAFOOD("Морски дарове"),
    DESSERT("Десерти");

    private final String value;

    private DishType(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
