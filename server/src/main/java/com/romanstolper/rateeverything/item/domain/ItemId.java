package com.romanstolper.rateeverything.item.domain;

/**
 * Typed item id
 */
public class ItemId {
    private final String value;
    public ItemId(String value) {
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
