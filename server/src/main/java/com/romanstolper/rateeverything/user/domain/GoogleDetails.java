package com.romanstolper.rateeverything.user.domain;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBDocument;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTypeConverted;
import com.romanstolper.rateeverything.user.persistence.DynamoDBGoogleIdConverter;

/**
 * Google details for a user
 */
@DynamoDBDocument
public class GoogleDetails {

    @DynamoDBAttribute(attributeName = "GoogleId")
    @DynamoDBTypeConverted(converter = DynamoDBGoogleIdConverter.class)
    private GoogleId googleId;

    @DynamoDBAttribute(attributeName = "GoogleProfile")
    private GoogleProfile googleProfile;

    public GoogleDetails() {
    }

    public GoogleDetails(GoogleId googleId, GoogleProfile googleProfile) {
        this.googleId = googleId;
        this.googleProfile = googleProfile;
    }

    public GoogleId getGoogleId() {
        return googleId;
    }

    public void setGoogleId(GoogleId googleId) {
        this.googleId = googleId;
    }

    public GoogleProfile getGoogleProfile() {
        return googleProfile;
    }

    public void setGoogleProfile(GoogleProfile googleProfile) {
        this.googleProfile = googleProfile;
    }
}
