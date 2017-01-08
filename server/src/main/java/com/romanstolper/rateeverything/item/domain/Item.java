package com.romanstolper.rateeverything.item.domain;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBRangeKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTypeConverted;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTypeConvertedEnum;
import com.romanstolper.rateeverything.dynamodb.DynamoDBInstantConverter;
import com.romanstolper.rateeverything.item.persistence.DynamoDBItemIdConverter;
import com.romanstolper.rateeverything.user.domain.UserId;
import com.romanstolper.rateeverything.user.persistence.DynamoDBUserIdConverter;

import java.time.Instant;

/**
 * A RateEverything Item
 */
@DynamoDBTable(tableName = "Items")
public class Item {
    private ItemId itemId;
    @DynamoDBTypeConverted(converter = DynamoDBUserIdConverter.class)
    private UserId userId;
    private String name;
    private String category;
    private Rating rating;
    private Instant createdDate;

    public static final Item DEFAULT = new Item(new ItemId("0"), new UserId("NOBODY"),"DEFAULT", "DEFAULT", Rating.UNRATED);

    // TODO: make this constructor call the other constructor with date as now().
    public Item(ItemId itemId, UserId userId, String name, String category, Rating rating) {
        this.itemId = itemId;
        this.userId = userId;
        this.name = name;
        this.category = category;
        this.rating = rating;
    }

    // TODO: make this constructor set all fields, including date
    public Item(ItemId itemId, UserId userId, String name, String category, Rating rating, Instant createdDate) {
        this(itemId, userId, name, category, rating);
        this.createdDate = createdDate;
    }

    public Item(ItemId itemId, Item copyItem) {
        this(itemId, copyItem.getUserId(), copyItem.getName(), copyItem.getCategory(), copyItem.getRating(), copyItem.getCreatedDate());
    }

    public Item(UserId userId) {
        this.userId = userId;
    }

    public Item() { }

    @DynamoDBTypeConverted(converter = DynamoDBItemIdConverter.class)
    @DynamoDBRangeKey(attributeName = "ItemId")
    public ItemId getItemId() {
        return itemId;
    }
    public void setItemId(ItemId itemId) {
        this.itemId = itemId;
    }

//    @DynamoDBTypeConverted(converter = DynamoDBUserIdConverter.class)
    @DynamoDBHashKey(attributeName = "UserId")
    public UserId getUserId() {
        return userId;
    }
//    @DynamoDBTypeConverted(converter = DynamoDBUserIdConverter.class)
    public void setUserId(UserId userId) {
        this.userId = userId;
    }

    @DynamoDBAttribute(attributeName = "Name")
    public String getName() {
        return name;
    }
    public void setName(String name) {
        System.out.println("Calling setter");
        this.name = name;
    }

    @DynamoDBAttribute(attributeName = "Category")
    public String getCategory() {
        return category;
    }
    public void setCategory(String category) {
        this.category = category;
    }

    @DynamoDBAttribute(attributeName = "Rating")
    @DynamoDBTypeConvertedEnum()
    public Rating getRating() {
        return rating;
    }
    public void setRating(Rating rating) {
        this.rating = rating;
    }

    @DynamoDBTypeConverted(converter = DynamoDBInstantConverter.class)
    @DynamoDBAttribute(attributeName = "CreatedDate")
    public Instant getCreatedDate() {
        return createdDate;
    }
    public void setCreatedDate(Instant createdDate) {
        this.createdDate = createdDate;
    }
}
