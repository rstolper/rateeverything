package com.romanstolper.rateeverything.user.service;

import com.romanstolper.rateeverything.item.domain.Item;
import com.romanstolper.rateeverything.item.persistence.H2ItemPersistence;
import com.romanstolper.rateeverything.item.persistence.ItemPersistence;

import javax.inject.Singleton;
import java.util.Collection;

@Singleton
public class UserServiceImpl implements UserService {

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
