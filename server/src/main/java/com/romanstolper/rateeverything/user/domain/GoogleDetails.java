package com.romanstolper.rateeverything.user.domain;

/**
 * Google details for a user
 */
public class GoogleDetails {
    private final GoogleId googleId;
    private final GoogleProfile googleProfile;

    public GoogleDetails(GoogleId googleId, GoogleProfile googleProfile) {
        this.googleId = googleId;
        this.googleProfile = googleProfile;
    }

    public GoogleId getGoogleId() {
        return googleId;
    }

    public GoogleProfile getGoogleProfile() {
        return googleProfile;
    }
}
