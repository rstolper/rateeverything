package com.romanstolper.rateeverything.item.persistence;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTypeConverter;
import com.romanstolper.rateeverything.item.domain.ItemId;

public class DynamoDBItemIdConverter implements DynamoDBTypeConverter<String, ItemId> {
    @Override
    public String convert(ItemId id) {
        return id == null ? null : id.getValue();
    }

    @Override
    public ItemId unconvert(String value) {
        return new ItemId(value);
    }
}
