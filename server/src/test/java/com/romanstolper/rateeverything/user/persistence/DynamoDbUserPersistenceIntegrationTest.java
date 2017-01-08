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

    @BeforeClass
    public void beforeClass() throws Exception {
//        client = new AmazonDynamoDBClient();
//        client.withEndpoint("http://localhost:8000");
//        dynamoDB = new DynamoDB(client);

        System.out.println("Deleting Users if it exists..");
        TableUtils.deleteTableIfExists(client, new DeleteTableRequest().withTableName(tableName));

        ArrayList<KeySchemaElement> keySchema = new ArrayList<KeySchemaElement>();
        keySchema.add(new KeySchemaElement()
                .withAttributeName("UserId")
                .withKeyType(KeyType.HASH));

        ArrayList<AttributeDefinition> attributeDefinitions = new ArrayList<AttributeDefinition>();
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

        System.out.println("Waiting for " + tableName + " to be created...this may take a while...");
        table.waitForActive();

        getTableInformation();
    }

    @After
    public void tearDown() throws Exception {

    }

    @Test
    public void getUser() throws Exception {

    }

    @Test
    public void getAllUsers() throws Exception {

    }

    @Test
    public void insertUser() throws Exception {

    }

    @Test
    public void updateUser() throws Exception {

    }

    @Test
    public void getUserByGoogleId() throws Exception {

    }

    @Test
    public void getUserByUsername() throws Exception {

    }

    private void getTableInformation() {

        System.out.println("Describing " + tableName);

        TableDescription tableDescription = dynamoDB.getTable(tableName).describe();
        System.out.format("Name: %s:\n" + "Status: %s \n"
                        + "Provisioned Throughput (read capacity units/sec): %d \n"
                        + "Provisioned Throughput (write capacity units/sec): %d \n",
                tableDescription.getTableName(),
                tableDescription.getTableStatus(),
                tableDescription.getProvisionedThroughput().getReadCapacityUnits(),
                tableDescription.getProvisionedThroughput().getWriteCapacityUnits());
    }

}