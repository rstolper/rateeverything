package com.romanstolper.rateeverything.user.domain;

public class NativeAuthDetails {
    private String username;
    private String password;
    private String authToken;
    /**
     * ISO-8601 formatted string
     */
    private String authTokenExpiry;

    private String email;
    private String name;
    private String imageUrl;

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
