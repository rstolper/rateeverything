package com.romanstolper.rateeverything.user.domain;

/**
 * Typed google id
 */
public class GoogleId {

    private final String value;

    public GoogleId(String value) {
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
