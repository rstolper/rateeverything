package com.romanstolper.rateeverything.user.service;

import com.romanstolper.rateeverything.item.domain.Item;
import com.romanstolper.rateeverything.user.domain.GoogleId;
import com.romanstolper.rateeverything.user.domain.User;
import com.romanstolper.rateeverything.user.domain.UserId;

import java.util.Collection;

/**
 * TODO
 */
public interface UserService {
    User getUser(UserId userId);
    User getUserViaGoogle(GoogleId googleId);
    User createUser(User newUser);
}
