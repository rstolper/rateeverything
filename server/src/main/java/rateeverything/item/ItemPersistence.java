package rateeverything.item;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by roman on 1/3/15.
 */
public class ItemPersistence {

    private Map<ItemId, Item> items;
    private int nextItemId = 1;

    public ItemPersistence() {
        this.items = new HashMap<>();
    }

    public Item selectItem(ItemId itemId) {
        return items.getOrDefault(itemId, Item.DEFAULT);
    }

    public Collection<Item> selectItems() {
        return items.values();
    }

    public Item insertItem(Item item) {
        ItemId itemId = getNewId();
        return items.put(itemId, new Item(itemId, item));
    }

    public Item updateItem(Item item) {
        return items.put(item.getId(), item);
    }

    public Item deleteItem(Item item) {
        return items.remove(item.getId());
    }

    private ItemId getNewId() {
        return new ItemId(nextItemId++);
    }
}
