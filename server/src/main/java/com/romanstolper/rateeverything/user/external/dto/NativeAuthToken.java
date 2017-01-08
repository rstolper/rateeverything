package com.romanstolper.rateeverything.user.external.dto;

/**
 * Holds a native auth token
 */
public class NativeAuthToken {
    private String authToken;

    public NativeAuthToken(String authToken) {
        this.authToken = authToken;
    }

    public String getAuthToken() {
        return authToken;
    }

    public void setAuthToken(String authToken) {
        this.authToken = authToken;
    }
}
