package com.romanstolper.rateeverything.user.domain;

public class User {
    private final UserId userId;
    private GoogleId googleId;
//    private GoogleDetails googleDetails;

    public User(UserId userId) {
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
//
//    public GoogleDetails getGoogleDetails() {
//        return googleDetails;
//    }
//
//    public void setGoogleDetails(GoogleDetails googleDetails) {
//        this.googleDetails = googleDetails;
//    }
}
