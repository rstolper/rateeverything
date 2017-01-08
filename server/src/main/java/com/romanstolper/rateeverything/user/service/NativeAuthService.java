package com.romanstolper.rateeverything.user.service;

import com.romanstolper.rateeverything.user.domain.UserId;

import java.time.Instant;
import java.util.UUID;

/**
 * Service for helping with google auth
 */
public class NativeAuthService {

    public String generateAuthToken(UserId userId, Instant expiry) {
        return UUID.randomUUID().toString();
    }

    public UserId parse(String nativeAuthToken) {
        // parse auth token
        // it should contain a user id
        // find and return the user id
        return null;
    }

    public void validateUsername(String username) throws IllegalArgumentException {
        if (username == null) {
            throw new IllegalArgumentException("Username cannot be null");
        }
        if (username.trim().length() != username.length()) {
            throw new IllegalArgumentException("Username cannot have spaces at beginning or end");
        }
        if (username.trim().length() < 3) {
            throw new IllegalArgumentException("Username must be at least 3 characters long");
        }
        if (username.trim().length() > 30) {
            throw new IllegalArgumentException("Username cannot be more than 30 characters long");
        }
    }

    public void validatePassword(String password) throws IllegalArgumentException {
        if (password == null) {
            throw new IllegalArgumentException("Password cannot be null");
        }
        if (password.trim().isEmpty()) {
            throw new IllegalArgumentException("Password can contain spaces, but it cannot be all spaces");
        }
        if (password.length() < 3) {
            throw new IllegalArgumentException("Password must be at least 3 characters long");
        }
        if (password.length() > 60) {
            throw new IllegalArgumentException("Password cannot be more than 60 characters long");
        }
    }
}
