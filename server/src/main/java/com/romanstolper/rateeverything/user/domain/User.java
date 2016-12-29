package com.romanstolper.rateeverything.user.domain;

public class User {
    private UserId id;
    private String username;
    private String email;

    public User() {

    }

    public User(UserId id, String username, String email) {
        this.id = id;
        this.username = username;
        this.email = email;
    }

    public UserId getId() {
        return id;
    }

    public void setId(UserId id) {
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
