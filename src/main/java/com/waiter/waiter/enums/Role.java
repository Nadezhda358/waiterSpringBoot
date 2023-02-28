package com.waiter.waiter.enums;

//public enum Role {
//    WAITER,
//    COOK,
//    ADMIN
//}

public enum Role {
    ROLE_USER("User"),
    ROLE_ADMIN("Admin");

    private final String value;

    private Role(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}