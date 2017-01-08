package com.romanstolper.rateeverything.user.persistence;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;
import com.amazonaws.services.dynamodbv2.document.DynamoDB;
import com.amazonaws.services.dynamodbv2.document.Table;
import com.amazonaws.services.dynamodbv2.model.AttributeDefinition;
import com.amazonaws.services.dynamodbv2.model.CreateTableRequest;
import com.amazonaws.services.dynamodbv2.model.DeleteTableRequest;
import com.amazonaws.services.dynamodbv2.model.KeySchemaElement;
import com.amazonaws.services.dynamodbv2.model.KeyType;
import com.amazonaws.services.dynamodbv2.model.ProvisionedThroughput;
import com.amazonaws.services.dynamodbv2.model.TableDescription;
import com.amazonaws.services.dynamodbv2.util.TableUtils;
import com.romanstolper.rateeverything.user.domain.GoogleId;
import com.romanstolper.rateeverything.user.domain.User;
import junit.framework.Assert;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.*;

/**
 * Note: This test requires a local instance of dynamodb running at localhost:8000
 * Preferably with -inMemory, so that it's a clean slate..
 */
public class DynamoDbUserPersistenceIntegrationTest {
    AmazonDynamoDBClient client = new AmazonDynamoDBClient().withEndpoint("http://localhost:8000");
    DynamoDB dynamoDB = new DynamoDB(client);
    String tableName = "Users";

    private DynamoDbUserPersistence userPersistence = new DynamoDbUserPersistence(client);

    @Before
    public void setUp() throws Exception {
        System.out.println("Deleting Users if it exists..");
        TableUtils.deleteTableIfExists(client, new DeleteTableRequest().withTableName(tableName));

        ArrayList<KeySchemaElement> keySchema = new ArrayList<>();
        keySchema.add(new KeySchemaElement()
                .withAttributeName("UserId")
                .withKeyType(KeyType.HASH));

        ArrayList<AttributeDefinition> attributeDefinitions = new ArrayList<>();
        attributeDefinitions.add(new AttributeDefinition()
                .withAttributeName("UserId")
                .withAttributeType("S"));


        CreateTableRequest request = new CreateTableRequest()
                .withTableName(tableName)
                .withKeySchema(keySchema)
                .withAttributeDefinitions(attributeDefinitions)
                .withProvisionedThroughput(new ProvisionedThroughput()
                        .withReadCapacityUnits(1L)
                        .withWriteCapacityUnits(1L));

        System.out.println("Issuing CreateTable request for " + tableName);
        Table table = dynamoDB.createTable(request);

//        System.out.println("Waiting for " + tableName + " to be created");
        table.waitForActive();

//        getTableInformation();
    }

    @Test
    public void getUser() throws Exception {

    }

    @Test
    public void getAllUsers() throws Exception {

    }

    @Test
    public void insertUser() throws Exception {
        User user1 = new User();
        user1.setUserId(UserIdGen.newId());
        user1.setGoogleId(new GoogleId("123"));
        userPersistence.insertUser(user1);

        User firstUser = userPersistence.getUser(user1.getUserId());
        assertEquals(firstUser.getGoogleId().getValue(), user1.getGoogleId().getValue());
    }

    @Test
    public void updateUser() throws Exception {
        User user1 = new User();
        user1.setUserId(UserIdGen.newId());
        user1.setGoogleId(new GoogleId("123"));

        User user2 = new User();
        user2.setUserId(UserIdGen.newId());
        user2.setGoogleId(new GoogleId("456"));

        userPersistence.insertUser(user1);
        userPersistence.insertUser(user2);

        user1.setGoogleId(new GoogleId("789"));

        userPersistence.updateUser(user1);

        User firstUser = userPersistence.getUser(user1.getUserId());

        assertEquals("789", firstUser.getGoogleId().getValue());
    }

    @Test
    public void getUserByGoogleId() throws Exception {
        User user1 = new User();
        user1.setUserId(UserIdGen.newId());
        user1.setGoogleId(new GoogleId("123"));

        User user2 = new User();
        user2.setUserId(UserIdGen.newId());
        user2.setGoogleId(new GoogleId("456"));

        userPersistence.insertUser(user1);
        userPersistence.insertUser(user2);

        User firstUser = userPersistence.getUserByGoogleId(new GoogleId("123"));
        assertEquals(firstUser.getUserId().getValue(), user1.getUserId().getValue());
    }

    @Test
    public void getUserByUsername() throws Exception {

    }

//    private void getTableInformation() {
//
//        System.out.println("Describing " + tableName);
//
//        TableDescription tableDescription = dynamoDB.getTable(tableName).describe();
//        System.out.format("Name: %s:\n" + "Status: %s \n"
//                        + "Provisioned Throughput (read capacity units/sec): %d \n"
//                        + "Provisioned Throughput (write capacity units/sec): %d \n",
//                tableDescription.getTableName(),
//                tableDescription.getTableStatus(),
//                tableDescription.getProvisionedThroughput().getReadCapacityUnits(),
//                tableDescription.getProvisionedThroughput().getWriteCapacityUnits());
//    }

}