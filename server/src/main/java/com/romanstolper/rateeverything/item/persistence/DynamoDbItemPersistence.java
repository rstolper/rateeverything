package com.romanstolper.rateeverything.item.persistence;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;
import com.amazonaws.services.dynamodbv2.document.DynamoDB;
import com.amazonaws.services.dynamodbv2.document.PrimaryKey;
import com.amazonaws.services.dynamodbv2.document.Table;
import com.romanstolper.rateeverything.item.domain.Item;
import com.romanstolper.rateeverything.item.domain.ItemId;
import com.romanstolper.rateeverything.user.domain.UserId;

import java.util.Collection;

public class DynamoDbItemPersistence implements ItemPersistence {

    private final DynamoDB dynamoDB;
    private final Table table;

    public DynamoDbItemPersistence() {
        dynamoDB = new DynamoDB(new AmazonDynamoDBClient());
        table = dynamoDB.getTable("Items");
    }

    @Override
    public Item getItem(UserId userId, ItemId itemId) {
        return null;
    }

    @Override
    public Collection<Item> getAllItems() {
        return null;
    }

    @Override
    public Collection<Item> getItemsForUser(UserId userId) {
        return null;
    }

    @Override
    public Item insertItem(Item item) {
        return null;
    }

    @Override
    public Item updateItem(Item item) {
        return null;
    }

    @Override
    public Item deleteItem(UserId userId, ItemId itemId) {
        return null;
    }
}
