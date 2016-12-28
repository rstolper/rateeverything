package com.romanstolper.rateeverything.users.domain;

import java.util.UUID;

/**
 * Created by roman on 11/14/2015.
 */
public class User {
    private UUID id;
    private String username;
    private String email;

    public User() {

    }

    public User(UUID id, String username, String email) {
        this.id = id;
        this.username = username;
        this.email = email;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
