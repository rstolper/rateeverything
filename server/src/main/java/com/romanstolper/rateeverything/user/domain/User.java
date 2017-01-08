package com.romanstolper.rateeverything.user.domain;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBIndexHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTypeConverted;
import com.romanstolper.rateeverything.user.persistence.DynamoDBGoogleIdConverter;
import com.romanstolper.rateeverything.user.persistence.DynamoDBUserIdConverter;

@DynamoDBTable(tableName = "Users")
public class User {
    @DynamoDBHashKey(attributeName = "UserId")
    @DynamoDBTypeConverted(converter = DynamoDBUserIdConverter.class)
    private UserId userId;

    @DynamoDBIndexHashKey(attributeName = "GoogleId", globalSecondaryIndexName = "GoogleId-index")
    @DynamoDBTypeConverted(converter = DynamoDBGoogleIdConverter.class)
    private GoogleId googleId;

    @DynamoDBIndexHashKey(attributeName = "NativeAuthUsername", globalSecondaryIndexName = "NativeAuthUsername-index")
    private String nativeAuthUsername;

    private GoogleDetails googleDetails;
    private NativeAuthDetails nativeAuthDetails;

    public User() {
    }

    public User(UserId userId) {
        this.userId = userId;
    }

    public void setUserId(UserId userId) {
        this.userId = userId;
    }

    public UserId getUserId() {
        return userId;
    }

    public GoogleId getGoogleId() {
        return googleId;
    }

    public void setGoogleId(GoogleId googleId) {
        this.googleId = googleId;
    }

    public String getNativeAuthUsername() {
        return nativeAuthUsername;
    }

    public void setNativeAuthUsername(String nativeAuthUsername) {
        this.nativeAuthUsername = nativeAuthUsername;
    }

    public GoogleDetails getGoogleDetails() {
        return googleDetails;
    }

    public void setGoogleDetails(GoogleDetails googleDetails) {
        this.googleDetails = googleDetails;
    }

    public NativeAuthDetails getNativeAuthDetails() {
        return nativeAuthDetails;
    }

    public void setNativeAuthDetails(NativeAuthDetails nativeAuthDetails) {
        this.nativeAuthDetails = nativeAuthDetails;
    }
}
