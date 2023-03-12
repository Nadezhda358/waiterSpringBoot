package com.waiter.waiter.enums;

public enum DrinkType {
    HOT("Топли напитки"),
    NONALCOHOLIC("Безалкохолни напитки"),
    ALCOHOLIC("Алкохолни напитки"),
    COCKTAIL("Коктейли");

    private final String value;

    private DrinkType(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
