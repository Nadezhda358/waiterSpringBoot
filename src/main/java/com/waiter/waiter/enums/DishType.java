package com.waiter.waiter.enums;

public enum DishType {
    SALAD("Салати"),
    STARTERS("Предястия"),
    MAIN_COURSES("Основно"),
    GRILL("Барбекю/Грил"),
    SEAFOOD("Морски дарове"),
    DESSERT("Десерти"),
    SOUP("Супи");

    private final String value;

    private DishType(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
