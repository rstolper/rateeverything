package com.romanstolper.rateeverything.user.external.dto;

/**
 * Input payload for creating a new user via native auth support
 */
public class NativeAuthCredsDTO {
    private String username;
    private String password;

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
}
