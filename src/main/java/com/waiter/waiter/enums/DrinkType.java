package com.waiter.waiter.enums;

public enum DrinkType {
    HOT("Топли"),
    NONALCOHOLIC("Безалкохолни"),
    ALCOHOLIC("Алкохолни"),
    COCKTAIL("Коктейли");

    private final String value;

    private DrinkType(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
