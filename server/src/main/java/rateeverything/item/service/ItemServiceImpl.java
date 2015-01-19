package rateeverything.item.service;

import rateeverything.item.domain.Item;
import rateeverything.item.persistence.H2ItemPersistence;
import rateeverything.item.persistence.ItemPersistence;
import rateeverything.item.persistence.PrePopulatedInMemoryItemPersistence;

import javax.inject.Singleton;
import java.util.Collection;

/**
 * Created by roman on 1/3/15.
 */
@Singleton
public class ItemServiceImpl implements ItemService {

    //private ItemPersistence itemPersistence = new PrePopulatedInMemoryItemPersistence();
    private ItemPersistence itemPersistence = new H2ItemPersistence();

    @Override
    public Collection<Item> getAllItems() {
        return itemPersistence.selectItems();
    }

    @Override
    public Collection<Item> getAllItemsByOwner(String owner) {
        return itemPersistence.selectItemsByOwner(owner);
    }

    @Override
    public Item getItem(long itemId) {
        return itemPersistence.selectItem(itemId);
    }

    @Override
    public boolean existsItem(long itemId) {
        Item item = itemPersistence.selectItem(itemId);
        return (item != null) && (item != Item.DEFAULT);
    }

    @Override
    public Item createItem(Item item) {
        return itemPersistence.insertItem(item);
    }

    @Override
    public Item updateItem(Item item) {
        return itemPersistence.updateItem(item);
    }

    @Override
    public Item deleteItem(long itemId) {
        return itemPersistence.deleteItem(itemId);
    }
}
