package com.romanstolper.rateeverything.item.persistence;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;
import com.amazonaws.services.dynamodbv2.document.DynamoDB;
import com.amazonaws.services.dynamodbv2.document.Table;
import com.amazonaws.services.dynamodbv2.model.AttributeDefinition;
import com.amazonaws.services.dynamodbv2.model.CreateTableRequest;
import com.amazonaws.services.dynamodbv2.model.DeleteTableRequest;
import com.amazonaws.services.dynamodbv2.model.KeySchemaElement;
import com.amazonaws.services.dynamodbv2.model.KeyType;
import com.amazonaws.services.dynamodbv2.model.ProvisionedThroughput;
import com.amazonaws.services.dynamodbv2.util.TableUtils;
import com.romanstolper.rateeverything.item.domain.Item;
import com.romanstolper.rateeverything.item.domain.Rating;
import com.romanstolper.rateeverything.user.persistence.DynamoDbUserPersistence;
import com.romanstolper.rateeverything.user.persistence.UserIdGen;
import org.junit.Before;
import org.junit.Test;

import java.time.Instant;
import java.util.ArrayList;

import static org.junit.Assert.*;

/**
 * Note: This test requires a local instance of dynamodb running at localhost:8000
 * Preferably with -inMemory, so that it's a clean slate..
 */
public class DynamoDbItemPersistenceTest {
    AmazonDynamoDBClient client = new AmazonDynamoDBClient().withEndpoint("http://localhost:8000");
    DynamoDB dynamoDB = new DynamoDB(client);
    String tableName = "Items";

    private DynamoDbItemPersistence itemPersistence = new DynamoDbItemPersistence(client);

    @Before
    public void setUp() throws Exception {
        System.out.println("Deleting Items if it exists..");
        TableUtils.deleteTableIfExists(client, new DeleteTableRequest().withTableName(tableName));

        ArrayList<KeySchemaElement> keySchema = new ArrayList<>();
        keySchema.add(new KeySchemaElement()
                .withAttributeName("UserId")
                .withKeyType(KeyType.HASH));
        keySchema.add(new KeySchemaElement()
                .withAttributeName("ItemId")
                .withKeyType(KeyType.RANGE));

        ArrayList<AttributeDefinition> attributeDefinitions = new ArrayList<>();
        attributeDefinitions.add(new AttributeDefinition()
                .withAttributeName("UserId")
                .withAttributeType("S"));
        attributeDefinitions.add(new AttributeDefinition()
                .withAttributeName("ItemId")
                .withAttributeType("S"));

        CreateTableRequest request = new CreateTableRequest()
                .withTableName(tableName)
                .withKeySchema(keySchema)
                .withAttributeDefinitions(attributeDefinitions)
                .withProvisionedThroughput(new ProvisionedThroughput()
                        .withReadCapacityUnits(1L)
                        .withWriteCapacityUnits(1L));

        System.out.println("Issuing CreateTable request for " + tableName);
        dynamoDB.createTable(request).waitForActive();
    }

    @Test
    public void getItemsForUser() throws Exception {

    }

    @Test
    public void insertItem() throws Exception {
        Item newItem = new Item();
        newItem.setUserId(UserIdGen.newId());
        newItem.setItemId(ItemIdGen.newId());
        newItem.setCategory("cat1");
        newItem.setCreatedDate(Instant.now());
        newItem.setName("item1");
        newItem.setRating(Rating.YES);

        itemPersistence.insertItem(newItem);

        Item gotItem = itemPersistence.getItem(newItem.getUserId(), newItem.getItemId());
        assertEquals(newItem.getCategory(), gotItem.getCategory());
        assertEquals(newItem.getRating(), gotItem.getRating());
        assertEquals(newItem.getName(), gotItem.getName());
        assertEquals(newItem.getCreatedDate(), gotItem.getCreatedDate());
        assertEquals(newItem.getItemId().getValue(), gotItem.getItemId().getValue());
        assertEquals(newItem.getUserId().getValue(), gotItem.getUserId().getValue());
    }

    @Test
    public void updateItem() throws Exception {

    }

    @Test
    public void deleteItem() throws Exception {

    }

}