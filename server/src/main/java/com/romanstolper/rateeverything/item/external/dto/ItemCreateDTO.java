package com.romanstolper.rateeverything.item.external.dto;

/**
 * Input payload for creating a new item
 */
public class ItemCreateDTO {
    private String name;
    private String category;
    private String rating;

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
}
