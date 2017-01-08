package com.romanstolper.rateeverything.item.persistence;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTypeConverter;
import com.romanstolper.rateeverything.item.domain.ItemId;

public class DynamoDBItemIdConverter implements DynamoDBTypeConverter<String, ItemId> {
    @Override
    public String convert(ItemId itemId) {
        return itemId == null ? null : itemId.getValue();
    }

    @Override
    public ItemId unconvert(String itemIdValue) {
        return new ItemId(itemIdValue);
    }
}
