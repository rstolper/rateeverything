package rateeverything.item;

/**
 * Created by roman on 1/2/15.
 */
public class Item {

    private ItemId id;
    private String name;

    public static Item DEFAULT = new Item(new ItemId(0), "DEFAULT");

    public Item(ItemId id, String name) {
        this.id = id;
        this.name = name;
    }

    public Item(ItemId id, Item copyItem) {
        this.id = id;
        this.name = copyItem.getName();
    }

    public Item() { }

    public ItemId getId() {
        return id;
    }

    public void setId(ItemId id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        System.out.println("Calling setter");
        this.name = name;
    }
}
