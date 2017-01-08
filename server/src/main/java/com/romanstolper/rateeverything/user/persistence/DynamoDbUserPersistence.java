package com.romanstolper.rateeverything.user.persistence;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBQueryExpression;
import com.romanstolper.rateeverything.user.domain.GoogleId;
import com.romanstolper.rateeverything.user.domain.User;
import com.romanstolper.rateeverything.user.domain.UserId;

import java.util.Collection;
import java.util.List;

public class DynamoDbUserPersistence implements UserPersistence {

    private final DynamoDBMapper mapper;

    public static final String IDX_GOOGLEID = "GoogleId-index";
    public static final String IDX_NATIVEAUTHUSERNAME = "NativeAuthUsername-index";

    public DynamoDbUserPersistence(AmazonDynamoDB client) {
        mapper = new DynamoDBMapper(client);
    }

    @Override
    public User getUser(UserId userId) {
        return mapper.load(User.class, userId);
    }

    @Override
    public Collection<User> getAllUsers() {
        return null;
    }

    @Override
    public User insertUser(User newUser) {
        newUser.setUserId(UserIdGen.newId());
        mapper.save(newUser);
        return newUser;
    }

    @Override
    public User updateUser(User user) {
        mapper.save(user);
        return user;
    }

    @Override
    public User getUserByGoogleId(GoogleId googleId) {
        List<User> users =  mapper.query(User.class, new DynamoDBQueryExpression<User>()
                .withIndexName(IDX_GOOGLEID)
                .withConsistentRead(false)
                .withHashKeyValues(new User(googleId)));

        // we expect 0 or 1
        if (users.size() > 1) {
            throw new RuntimeException("Found more than one user for Google Id: " + googleId);
        } else if (users.size() == 0) {
            return null;
        } else {
            return users.get(0);
        }
    }

    @Override
    public User getUserByUsername(String username) {
        User searchBy = new User();
        searchBy.setNativeAuthUsername(username);

        List<User> users =  mapper.query(User.class, new DynamoDBQueryExpression<User>()
                .withIndexName(IDX_NATIVEAUTHUSERNAME)
                .withConsistentRead(false)
                .withHashKeyValues(searchBy));

        // we expect 0 or 1
        if (users.size() > 1) {
            throw new RuntimeException("Found more than one user for username: " + username);
        } else if (users.size() == 0) {
            return null;
        } else {
            return users.get(0);
        }
    }
}
