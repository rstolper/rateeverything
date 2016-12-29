package com.romanstolper.rateeverything.user.service;

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
}
