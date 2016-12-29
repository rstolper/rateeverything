package com.romanstolper.rateeverything.item.external.dto;

import com.romanstolper.rateeverything.item.domain.Item;

import java.net.URI;
import java.util.Date;

/**
 * Item object for serialized transfer
 */
public class ItemDTO {
    private URI url;
    private String itemId;
    private String userId;
    private String name;
    private String category;
    private String rating;
    private long creationDate;

    public ItemDTO() {}

    public ItemDTO(Item item) {
        this.setItemId(item.getItemId().getValue());
        this.setUserId(item.getUserId().getValue());
        this.setName(item.getName());
        this.setCategory(item.getCategory());
        this.setRating(item.getRating().getDisplayText());
        this.setCreationDate(item.getCreationDate().getTime());
    }

    public URI getUrl() {
        return url;
    }

    public void setUrl(URI url) {
        this.url = url;
    }

    public String getItemId() {
        return itemId;
    }

    public void setItemId(String itemId) {
        this.itemId = itemId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
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

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public long getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(long creationDate) {
        this.creationDate = creationDate;
    }
}
