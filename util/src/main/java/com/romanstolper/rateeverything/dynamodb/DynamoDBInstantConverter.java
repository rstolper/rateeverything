package com.romanstolper.rateeverything.dynamodb;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTypeConverter;

import java.time.Instant;

public class DynamoDBInstantConverter implements DynamoDBTypeConverter<String, Instant> {
    @Override
    public String convert(Instant instant) {
        return instant.toString();
    }

    @Override
    public Instant unconvert(String iso8601date) {
        return Instant.parse(iso8601date);
    }
}
