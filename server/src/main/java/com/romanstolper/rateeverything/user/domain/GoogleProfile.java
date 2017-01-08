package com.romanstolper.rateeverything.user.domain;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBDocument;

/**
 * Google profile for a user
 */
@DynamoDBDocument
public class GoogleProfile {

    @DynamoDBAttribute(attributeName = "Name")
    private String name;

    @DynamoDBAttribute(attributeName = "GivenName")
    private String givenName;

    @DynamoDBAttribute(attributeName = "FamilyName")
    private String familyName;

    @DynamoDBAttribute(attributeName = "Email")
    private String email;

    @DynamoDBAttribute(attributeName = "EmailVerified")
    private boolean emailVerified;

    @DynamoDBAttribute(attributeName = "ImageUrl")
    private String imageUrl;

    @DynamoDBAttribute(attributeName = "Locale")
    private String locale;

    public GoogleProfile() {
    }

    public GoogleProfile(String name,
                         String givenName,
                         String familyName,
                         String email,
                         boolean emailVerified,
                         String imageUrl,
                         String locale) {
        this.name = name;
        this.givenName = givenName;
        this.familyName = familyName;
        this.email = email;
        this.emailVerified = emailVerified;
        this.imageUrl = imageUrl;
        this.locale = locale;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getGivenName() {
        return givenName;
    }

    public void setGivenName(String givenName) {
        this.givenName = givenName;
    }

    public String getFamilyName() {
        return familyName;
    }

    public void setFamilyName(String familyName) {
        this.familyName = familyName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public boolean isEmailVerified() {
        return emailVerified;
    }

    public void setEmailVerified(boolean emailVerified) {
        this.emailVerified = emailVerified;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getLocale() {
        return locale;
    }

    public void setLocale(String locale) {
        this.locale = locale;
    }
}
