package rateeverything.item;

import java.util.Collection;

/**
 * Created by roman on 1/3/15.
 */
public class ItemService {

    private ItemPersistence itemPersistence = new ItemPersistence();

    public Collection<Item> GetAllItems() {
        return itemPersistence.selectItems();
    }

    public Item GetItem(ItemId itemId) {
        return itemPersistence.selectItem(itemId);
    }

    public Item CreateItem(Item item) {
        return itemPersistence.insertItem(item);
    }

    public Item UpdateItem(Item item) {
        return itemPersistence.updateItem(item);
    }

    public Item DeleteItem(Item item) {
        return itemPersistence.deleteItem(item);
    }
}
