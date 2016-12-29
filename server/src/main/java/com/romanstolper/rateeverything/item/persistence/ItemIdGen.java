package com.romanstolper.rateeverything.item.persistence;

import com.romanstolper.rateeverything.item.domain.ItemId;

import java.util.UUID;

/**
 * Helper for creating new item ids
 */
public class ItemIdGen {
    public static ItemId newId() {
        return new ItemId(UUID.randomUUID().toString());
    }
}
