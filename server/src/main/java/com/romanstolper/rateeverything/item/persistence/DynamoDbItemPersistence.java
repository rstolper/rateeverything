package com.romanstolper.rateeverything.item.persistence;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBQueryExpression;
import com.romanstolper.rateeverything.item.domain.Item;
import com.romanstolper.rateeverything.item.domain.ItemId;
import com.romanstolper.rateeverything.user.domain.UserId;

import java.util.Collection;

public class DynamoDbItemPersistence implements ItemPersistence {

    private final DynamoDBMapper mapper;

    public DynamoDbItemPersistence(AmazonDynamoDB client) {
        mapper = new DynamoDBMapper(client);
    }

    @Override
    public Item getItem(UserId userId, ItemId itemId) {
        return mapper.load(Item.class, userId, itemId);
    }

    @Override
    public Collection<Item> getAllItems() {
        throw new RuntimeException("not implemented");
    }

    @Override
    public Collection<Item> getItemsForUser(UserId userId) {
        return mapper.query(Item.class, new DynamoDBQueryExpression<Item>()
                .withHashKeyValues(new Item(userId))
        );
    }

    @Override
    public Item insertItem(Item item) {
        item.setItemId(ItemIdGen.newId());
        mapper.save(item);
        return item;
    }

    @Override
    public Item updateItem(Item item) {
        mapper.save(item);
        return item;
    }

    @Override
    public Item deleteItem(UserId userId, ItemId itemId) {
        Item item = getItem(userId, itemId);
        mapper.delete(item);
        return item;
    }
}
