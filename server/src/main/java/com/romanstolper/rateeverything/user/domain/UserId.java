package com.romanstolper.rateeverything.user.domain;

/**
 * Typed user id
 */
public class UserId {

    private final String value;

    public UserId(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    @Override
    public String toString() {
        return getValue();
    }
}
