package com.romanstolper.rateeverything.user.domain;

public class User {
    private UserId userId;
    private GoogleId googleId;
    private String nativeAuthUsername;

    private GoogleDetails googleDetails;
    private NativeAuthDetails nativeAuthDetails;

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
