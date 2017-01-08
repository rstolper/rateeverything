package com.romanstolper.rateeverything.user.domain;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBDocument;

@DynamoDBDocument
public class NativeAuthDetails {

    @DynamoDBAttribute(attributeName = "Username")
    private String username;

    @DynamoDBAttribute(attributeName = "Password")
    private String password;

    @DynamoDBAttribute(attributeName = "AuthToken")
    private String authToken;

    /**
     * ISO-8601 formatted string
     */
    @DynamoDBAttribute(attributeName = "AuthTokenExpiry")
    private String authTokenExpiry;

    @DynamoDBAttribute(attributeName = "Email")
    private String email;

    @DynamoDBAttribute(attributeName = "Name")
    private String name;

    @DynamoDBAttribute(attributeName = "ImageUrl")
    private String imageUrl;

    public NativeAuthDetails() {
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getAuthToken() {
        return authToken;
    }

    public void setAuthToken(String authToken) {
        this.authToken = authToken;
    }

    public String getAuthTokenExpiry() {
        return authTokenExpiry;
    }

    public void setAuthTokenExpiry(String authTokenExpiry) {
        this.authTokenExpiry = authTokenExpiry;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}
