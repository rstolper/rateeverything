package rateeverything.item.domain;

import java.util.Date;

/**
 * Created by roman on 1/2/15.
 */
public class Item {
    private long id;
    private String name;
    private String category;
    private String owner;
    private Rating rating;
    private Date creationDate;

    public static final Item DEFAULT = new Item(0, "DEFAULT", "DEFAULT", "NOBODY", Rating.UNRATED);

    public Item(long id, String name, String category, String owner, Rating rating) {
        this.id = id;
        this.name = name;
        this.category = category;
        this.owner = owner;
        this.rating = rating;
    }

    public Item(long id, String name, String category, String owner, Rating rating, Date creationDate) {
        this(id, name, category, owner, rating);
        this.creationDate = creationDate;
    }

    public Item(long id, Item copyItem) {
        this(id, copyItem.getName(), copyItem.getCategory(), copyItem.getOwner(), copyItem.getRating(), copyItem.getCreationDate());
    }

    public Item() { }

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
        System.out.println("Calling setter");
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

    public Rating getRating() {
        return rating;
    }

    public void setRating(Rating rating) {
        this.rating = rating;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }
}
