package com.romanstolper.rateeverything.item.persistence;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;
import com.amazonaws.services.dynamodbv2.document.*;
import com.romanstolper.rateeverything.item.domain.Item;
import com.romanstolper.rateeverything.item.domain.ItemId;
import com.romanstolper.rateeverything.item.domain.Rating;
import com.romanstolper.rateeverything.user.domain.UserId;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Collection;

public class DynamoDbItemPersistence implements ItemPersistence {

    private final DynamoDB dynamoDB;
    private final Table table;

    public static final String PK_USERID = "UserId";
    public static final String PK_ITEMID = "ItemId";

    public static final String F_NAME = "Name";
    public static final String F_CATEGORY = "Category";
    public static final String F_RATING = "Rating";
    public static final String F_CREATED = "CreatedDate";

    public DynamoDbItemPersistence() {
        dynamoDB = new DynamoDB(new AmazonDynamoDBClient());
        table = dynamoDB.getTable("Items");
    }

    @Override
    public Item getItem(UserId userId, ItemId itemId) {
        Item retrievedItem = mapFromDynamo(table.getItem(pk(userId, itemId)));
        return retrievedItem;
    }

    @Override
    public Collection<Item> getAllItems() {
        return null;
    }

    @Override
    public Collection<Item> getItemsForUser(UserId userId) {
        ItemCollection<QueryOutcome> dynamoItems = table.query(PK_USERID, userId.getValue());
        Collection<Item> itemsForUser = new ArrayList<>();
        for (com.amazonaws.services.dynamodbv2.document.Item dynamoItem : dynamoItems) {
            itemsForUser.add(mapFromDynamo(dynamoItem));
        }
        return itemsForUser;
    }

    @Override
    public Item insertItem(Item item) {
        ItemId newItemId = ItemIdGen.newId();
        com.amazonaws.services.dynamodbv2.document.Item dynamoItem =
                new com.amazonaws.services.dynamodbv2.document.Item();
        dynamoItem.withPrimaryKey(pk(item.getUserId(), newItemId));
        dynamoItem.withString(F_NAME, item.getName());
        dynamoItem.withString(F_CATEGORY,item.getCategory());
        dynamoItem.withString(F_RATING,item.getRating().toString());
        dynamoItem.withString(F_CREATED, item.getCreationDate().toString());
        table.putItem(dynamoItem);
        return getItem(item.getUserId(), newItemId);
    }

    @Override
    public Item updateItem(Item item) {
        return null;
    }

    @Override
    public Item deleteItem(UserId userId, ItemId itemId) {
        Item beforeDelete = getItem(userId, itemId);
        if (beforeDelete != null) {
            table.deleteItem(pk(userId, itemId));
            return beforeDelete;
        }
        return null;
    }

    private Item mapFromDynamo(com.amazonaws.services.dynamodbv2.document.Item dynamoItem) {
        return new Item(
                new ItemId(dynamoItem.getString(PK_ITEMID)),
                new UserId(dynamoItem.getString(PK_USERID)),
                dynamoItem.getString(F_NAME),
                dynamoItem.getString(F_CATEGORY),
                Rating.valueOf(dynamoItem.getString(F_RATING)),
                Instant.parse(dynamoItem.getString(F_CREATED))
        );
    }

    private PrimaryKey pk(UserId userId, ItemId itemId) {
        return new PrimaryKey(PK_USERID, userId.getValue(), PK_ITEMID, itemId.getValue());
    }

}
