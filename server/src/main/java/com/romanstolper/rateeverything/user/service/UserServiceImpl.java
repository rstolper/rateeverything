package com.romanstolper.rateeverything.user.service;

import com.romanstolper.rateeverything.user.domain.GoogleId;
import com.romanstolper.rateeverything.user.domain.User;
import com.romanstolper.rateeverything.user.domain.UserId;
import com.romanstolper.rateeverything.user.persistence.UserPersistence;

import javax.inject.Singleton;

/**
 * TODO
 */
@Singleton
public class UserServiceImpl implements UserService {

    private final UserPersistence userPersistence;

    public UserServiceImpl(UserPersistence userPersistence) {
        this.userPersistence = userPersistence;
    }

    @Override
    public User getUser(UserId userId) {
        return userPersistence.getUser(userId);
    }

    @Override
    public User getUserViaGoogle(GoogleId googleId) {
        return userPersistence.getUserByGoogleId(googleId);
    }

    @Override
    public User getUserViaUsername(String username) {
        return userPersistence.getUserByUsername(username);
    }

    @Override
    public User createUser(User newUser) {
        return userPersistence.insertUser(newUser);
    }

    @Override
    public User updateUser(User user) {
        return userPersistence.updateUser(user);
    }
}
