package com.romanstolper.rateeverything.user.persistence;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.document.DynamoDB;
import com.amazonaws.services.dynamodbv2.document.Index;
import com.amazonaws.services.dynamodbv2.document.ItemCollection;
import com.amazonaws.services.dynamodbv2.document.PrimaryKey;
import com.amazonaws.services.dynamodbv2.document.QueryOutcome;
import com.amazonaws.services.dynamodbv2.document.Table;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.romanstolper.rateeverything.user.domain.GoogleId;
import com.romanstolper.rateeverything.user.domain.NativeAuthDetails;
import com.romanstolper.rateeverything.user.domain.User;
import com.romanstolper.rateeverything.user.domain.UserId;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;

public class DynamoDbUserPersistence implements UserPersistence {

    private final DynamoDB dynamoDB;
    private final Table table;
    private final Index byGoogleId;
    private final Index byUsername;

    private final DynamoDBMapper mapper;

    public static final String PK_USERID = "UserId";
    public static final String F_GOOGLE_ID = "GoogleId";
    public static final String F_USERNAME = "NativeAuthUsername";

    public static final String F_NATIVE_AUTH_DETAILS = "NativeAuthDetails";

    public static final String F_NATIVE_USERNAME = "NativeAuthDetails.Username";
    public static final String F_NATIVE_PASSWORD = "NativeAuthDetails.Password";
    public static final String F_NATIVE_EMAIL = "NativeAuthDetails.Email";
    public static final String F_NATIVE_AUTH_TOKEN = "NativeAuthDetails.AuthToken";
    public static final String F_NATIVE_AUTH_TOKEN_EXPIRY = "NativeAuthDetails.AuthTokenExpiry";

    private static final ObjectMapper objectMapper = new ObjectMapper();

    public DynamoDbUserPersistence(AmazonDynamoDB client) {
        mapper = new DynamoDBMapper(client);
        dynamoDB = new DynamoDB(client);
        table = dynamoDB.getTable("Users");
        byGoogleId = table.getIndex("GoogleId-index");
        byUsername = table.getIndex("NativeAuthUsername-index");
    }

    @Override
    public User getUser(UserId userId) {
        return mapFromDynamo(table.getItem(pk(userId)));

//        return mapper.load(User.class, userId);
    }

    @Override
    public Collection<User> getAllUsers() {
        return null;
    }

    @Override
    public User insertUser(User newUser) {
        UserId newUserId = UserIdGen.newId();
        newUser.setUserId(newUserId);
        table.putItem(mapToDynamo(newUser));
        return newUser;

//        newUser.setUserId(UserIdGen.newId());
//        mapper.save(newUser);
//        return newUser;
    }

    @Override
    public User updateUser(User user) {
        UserId userId = user.getUserId();
        if (userId == null) {
            throw new RuntimeException("UserId cannot be null when updating user");
        }
        table.putItem(mapToDynamo(user));

//        mapper.save(user);

        return user;
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

    @Override
    public User getUserByUsername(String username) {
        ItemCollection<QueryOutcome> dynamoItems = byUsername.query(F_USERNAME, username);
        Collection<User> users = new ArrayList<>();
        for (com.amazonaws.services.dynamodbv2.document.Item dynamoItem : dynamoItems) {
            users.add(mapFromDynamo(dynamoItem));
        }
        if (users.size() == 0) {
            return null;
        } else if (users.size() == 1) {
            return users.iterator().next();
        } else {
            throw new RuntimeException("Found more than one user for username: " + username);
        }
    }

    private User mapFromDynamo(com.amazonaws.services.dynamodbv2.document.Item dynamoItem) {
        User user = new User(new UserId(dynamoItem.getString(PK_USERID)));
        if (dynamoItem.getString(F_GOOGLE_ID) != null) {
            user.setGoogleId(new GoogleId(dynamoItem.getString(F_GOOGLE_ID)));
        }
        if (dynamoItem.getString(F_USERNAME) != null) {
            user.setNativeAuthUsername(dynamoItem.getString(F_USERNAME));
        }
        String nativeAuthDetailsJSON = dynamoItem.getJSON(F_NATIVE_AUTH_DETAILS);
        if (nativeAuthDetailsJSON != null) {
            try {
                NativeAuthDetails nativeAuthDetails = objectMapper.readValue(nativeAuthDetailsJSON, NativeAuthDetails.class);
                user.setNativeAuthDetails(nativeAuthDetails);
            } catch (IOException e) {
                throw new RuntimeException("Failed to map json back to native auth details");
            }
        }

//        NativeAuthDetails nativeAuthDetails = new NativeAuthDetails();
//        nativeAuthDetails.setUsername(dynamoItem.getString(F_NATIVE_USERNAME));
//        nativeAuthDetails.setPassword(dynamoItem.getString(F_NATIVE_PASSWORD));
//        nativeAuthDetails.setEmail(dynamoItem.getString(F_NATIVE_EMAIL));
//        nativeAuthDetails.setAuthToken(dynamoItem.getString(F_NATIVE_AUTH_TOKEN));
//        if (dynamoItem.getString(F_NATIVE_AUTH_TOKEN_EXPIRY) != null) {
//            nativeAuthDetails.setAuthTokenExpiry(dynamoItem.getString(F_NATIVE_AUTH_TOKEN_EXPIRY));
//        }
//        user.setNativeAuthDetails(nativeAuthDetails);
        return user;
    }

    private com.amazonaws.services.dynamodbv2.document.Item mapToDynamo(User user) {
        com.amazonaws.services.dynamodbv2.document.Item dynamoItem =
                new com.amazonaws.services.dynamodbv2.document.Item();
        dynamoItem.withPrimaryKey(pk(user.getUserId()));
        if (user.getGoogleId() != null) {
            dynamoItem.withString(F_GOOGLE_ID, user.getGoogleId().getValue());
        }
        if (user.getNativeAuthUsername() != null) {
            dynamoItem.withString(F_USERNAME, user.getNativeAuthUsername());
        }
        if (user.getNativeAuthDetails() != null) {
            NativeAuthDetails nativeAuthDetails = user.getNativeAuthDetails();
            try {
                String json = objectMapper.writeValueAsString(nativeAuthDetails);
                dynamoItem.withJSON(F_NATIVE_AUTH_DETAILS, json);
            } catch (JsonProcessingException e) {
                throw new RuntimeException("Failed to map native auth details to json");
            }
//            if (nativeAuthDetails.getUsername() != null) {
//                dynamoItem.withString(F_NATIVE_USERNAME, nativeAuthDetails.getUsername());
//            }
//            if (nativeAuthDetails.getPassword() != null) {
//                dynamoItem.withString(F_NATIVE_PASSWORD, nativeAuthDetails.getPassword());
//            }
//            if (nativeAuthDetails.getEmail() != null) {
//                dynamoItem.withString(F_NATIVE_EMAIL, nativeAuthDetails.getEmail());
//            }
//            if (nativeAuthDetails.getAuthToken() != null) {
//                dynamoItem.withString(F_NATIVE_AUTH_TOKEN, nativeAuthDetails.getAuthToken());
//            }
//            if (nativeAuthDetails.getAuthTokenExpiry() != null) {
//                dynamoItem.withString(F_NATIVE_AUTH_TOKEN_EXPIRY, nativeAuthDetails.getAuthTokenExpiry().toString());
//            }
        }
        return dynamoItem;
    }

    private PrimaryKey pk(UserId userId) {
        return new PrimaryKey(PK_USERID, userId.getValue());
    }
}
