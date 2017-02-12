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

    @DynamoDBRangeKey(attributeName = "ItemId")
    @DynamoDBTypeConverted(converter = DynamoDBItemIdConverter.class)
    private ItemId itemId;

    @DynamoDBHashKey(attributeName = "UserId")
    @DynamoDBTypeConverted(converter = DynamoDBUserIdConverter.class)
    private UserId userId;

    @DynamoDBAttribute(attributeName = "Name")
    private String name;

    @DynamoDBAttribute(attributeName = "Category")
    private String category;

    @DynamoDBAttribute(attributeName = "Rating")
    @DynamoDBTypeConvertedEnum()
    private Rating rating;

    @DynamoDBAttribute(attributeName = "Notes")
    private String notes;

    @DynamoDBAttribute(attributeName = "CreatedDate")
    @DynamoDBTypeConverted(converter = DynamoDBInstantConverter.class)
    private Instant createdDate;

    public static final Item DEFAULT = new Item(new ItemId("0"), new UserId("NOBODY"),"DEFAULT", "DEFAULT", Rating.UNRATED);

    public Item(ItemId itemId, UserId userId, String name, String category, Rating rating, String notes, Instant createdDate) {
        this.itemId = itemId;
        this.userId = userId;
        this.name = name;
        this.category = category;
        this.rating = rating;
        this.notes = notes;
        this.createdDate = createdDate;
    }

    public Item(ItemId itemId, UserId userId, String name, String category, Rating rating) {
        this(itemId, userId, name, category, rating, null, Instant.now());
    }

    public Item(ItemId itemId, Item copyItem) {
        this(
                itemId,
                copyItem.getUserId(),
                copyItem.getName(),
                copyItem.getCategory(),
                copyItem.getRating(),
                copyItem.getNotes(),
                copyItem.getCreatedDate()
        );
    }

    public Item(UserId userId) {
        this.userId = userId;
    }

    public Item() { }

    public ItemId getItemId() {
        return itemId;
    }

    public void setItemId(ItemId itemId) {
        this.itemId = itemId;
    }

    public UserId getUserId() {
        return userId;
    }

    public void setUserId(UserId userId) {
        this.userId = userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public Rating getRating() {
        return rating;
    }

    public void setRating(Rating rating) {
        this.rating = rating;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public Instant getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Instant createdDate) {
        this.createdDate = createdDate;
    }
}
