package com.romanstolper.rateeverything.user.persistence;

import com.romanstolper.rateeverything.user.domain.UserId;

import java.util.UUID;

/**
 * Helper for creating new user ids
 */
public class UserIdGen {
    public static UserId newId() {
        return new UserId(UUID.randomUUID().toString());
    }
}
