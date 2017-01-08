package com.romanstolper.rateeverything.user.persistence;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTypeConverter;
import com.romanstolper.rateeverything.user.domain.GoogleId;

public class DynamoDBGoogleIdConverter implements DynamoDBTypeConverter<String, GoogleId> {
    @Override
    public String convert(GoogleId id) {
        return id == null ? null : id.getValue();
    }

    @Override
    public GoogleId unconvert(String value) {
        return new GoogleId(value);
    }
}
