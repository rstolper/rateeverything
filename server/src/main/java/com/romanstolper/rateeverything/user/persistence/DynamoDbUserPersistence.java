package com.romanstolper.rateeverything.user.persistence;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;
import com.amazonaws.services.dynamodbv2.document.DynamoDB;
import com.amazonaws.services.dynamodbv2.document.Index;
import com.amazonaws.services.dynamodbv2.document.ItemCollection;
import com.amazonaws.services.dynamodbv2.document.PrimaryKey;
import com.amazonaws.services.dynamodbv2.document.QueryOutcome;
import com.amazonaws.services.dynamodbv2.document.Table;
import com.romanstolper.rateeverything.user.domain.GoogleId;
import com.romanstolper.rateeverything.user.domain.User;
import com.romanstolper.rateeverything.user.domain.UserId;

import java.util.ArrayList;
import java.util.Collection;

public class DynamoDbUserPersistence implements UserPersistence {

    private final DynamoDB dynamoDB;
    private final Table table;
    private final Index byGoogleId;

    public static final String PK_USERID = "UserId";
    public static final String F_GOOGLE_ID = "GoogleId";
//    public static final String F_GOOGLE_DETAILS = "GoogleDetails";
//    public static final String F_GOOGLE_PF_NAME = "Google.Profile.Name";
//    public static final String F_GOOGLE_PF_EMAIL = "Google.Profile.Email";

    public DynamoDbUserPersistence() {
        dynamoDB = new DynamoDB(new AmazonDynamoDBClient());
        table = dynamoDB.getTable("Users");
        byGoogleId = table.getIndex("GoogleId-index");
    }

    @Override
    public User getUser(UserId userId) {
        return mapFromDynamo(table.getItem(pk(userId)));
    }

    @Override
    public Collection<User> getAllUsers() {
        return null;
    }

    @Override
    public User insertUser(User newUser) {
        UserId newUserId = UserIdGen.newId();
        com.amazonaws.services.dynamodbv2.document.Item dynamoItem =
                new com.amazonaws.services.dynamodbv2.document.Item();
        dynamoItem.withPrimaryKey(pk(newUserId));
        dynamoItem.withString(F_GOOGLE_ID, newUser.getGoogleId().getValue());
        table.putItem(dynamoItem);
        return getUser(newUserId);
    }

    @Override
    public User getUserByGoogleId(GoogleId googleId) {
        ItemCollection<QueryOutcome> dynamoItems = byGoogleId.query(F_GOOGLE_ID, googleId.getValue());
        Collection<User> users = new ArrayList<>();
        for (com.amazonaws.services.dynamodbv2.document.Item dynamoItem : dynamoItems) {
            users.add(mapFromDynamo(dynamoItem));
        }
        if (users.size() != 1) {
            throw new RuntimeException("Could not find exactly one user for Google Id: " + googleId);
        }
        return users.iterator().next();
    }

    private User mapFromDynamo(com.amazonaws.services.dynamodbv2.document.Item dynamoItem) {
        User user = new User(new UserId(dynamoItem.getString(PK_USERID)));
        GoogleId googleId = new GoogleId(dynamoItem.getString(F_GOOGLE_ID));
        user.setGoogleId(googleId);
        return user;
    }

    private PrimaryKey pk(UserId userId) {
        return new PrimaryKey(PK_USERID, userId.getValue());
    }

    private PrimaryKey pkGoogleId(GoogleId googleId) {
        return new PrimaryKey(F_GOOGLE_ID, googleId.getValue());
    }
}
