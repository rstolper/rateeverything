package com.romanstolper.rateeverything.item.domain;

import com.romanstolper.rateeverything.user.domain.UserId;

import java.time.Instant;

/**
 * A RateEverything Item
 */
public class Item {
    private ItemId itemId;
    private UserId userId;
    private String name;
    private String category;
    private Rating rating;
    private Instant creationDate;

    public static final Item DEFAULT = new Item(new ItemId("0"), new UserId("NOBODY"),"DEFAULT", "DEFAULT", Rating.UNRATED);

    public Item(ItemId itemId, UserId userId, String name, String category, Rating rating) {
        this.itemId = itemId;
        this.userId = userId;
        this.name = name;
        this.category = category;
        this.rating = rating;
    }

    public Item(ItemId itemId, UserId userId, String name, String category, Rating rating, Instant creationDate) {
        this(itemId, userId, name, category, rating);
        this.creationDate = creationDate;
    }

    public Item(ItemId itemId, Item copyItem) {
        this(itemId, copyItem.getUserId(), copyItem.getName(), copyItem.getCategory(), copyItem.getRating(), copyItem.getCreationDate());
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
        System.out.println("Calling setter");
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

    public Instant getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Instant creationDate) {
        this.creationDate = creationDate;
    }
}
