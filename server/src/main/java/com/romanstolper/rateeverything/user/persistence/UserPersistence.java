package com.romanstolper.rateeverything.user.persistence;

import com.romanstolper.rateeverything.user.domain.GoogleId;
import com.romanstolper.rateeverything.user.domain.User;
import com.romanstolper.rateeverything.user.domain.UserId;

import java.util.Collection;

/**
 * TODO
 */
public interface UserPersistence {
    User getUser(UserId userId);
    Collection<User> getAllUsers();
    User insertUser(User newUser);
    User updateUser(User user);

    User getUserByGoogleId(GoogleId googleId);
    User getUserByUsername(String username);
 }
