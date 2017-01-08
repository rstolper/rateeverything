package com.romanstolper.rateeverything.user.persistence;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTypeConverter;
import com.romanstolper.rateeverything.user.domain.UserId;

public class DynamoDBUserIdConverter implements DynamoDBTypeConverter<String, UserId> {
    @Override
    public String convert(UserId id) {
        return id == null ? null : id.getValue();
    }

    @Override
    public UserId unconvert(String value) {
        return new UserId(value);
    }
}
