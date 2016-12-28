package com.romanstolper.rateeverything.item.external.dto;

import com.romanstolper.rateeverything.item.domain.Item;

import java.net.URI;
import java.util.Date;

/**
 * Created by roman on 1/18/2015.
 */
public class ItemDTO {
    private URI url;
    private long id;
    private String name;
    private String category;
    private String owner;
    private String rating;
    private long creationDate;

    public ItemDTO() {}

    public ItemDTO(Item item) {
        this.setId(item.getId());
        this.setName(item.getName());
        this.setCategory(item.getCategory());
        this.setOwner(item.getOwner());
        this.setRating(item.getRating().getDisplayText());
        this.setCreationDate(item.getCreationDate().getTime());
    }

    public URI getUrl() {
        return url;
    }

    public void setUrl(URI url) {
        this.url = url;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
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

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
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
