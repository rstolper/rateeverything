package com.romanstolper.rateeverything.item.service;

import com.romanstolper.rateeverything.item.domain.Item;
import com.romanstolper.rateeverything.item.domain.ItemId;
import com.romanstolper.rateeverything.item.persistence.ItemPersistence;
import com.romanstolper.rateeverything.user.domain.UserId;

import javax.inject.Singleton;
import java.util.Collection;

@Singleton
public class ItemServiceImpl implements ItemService {

    private final ItemPersistence itemPersistence;

    public ItemServiceImpl(ItemPersistence itemPersistence) {
        this.itemPersistence = itemPersistence;
    }

    @Override
    public Collection<Item> getAllItems(UserId userId) {
        return itemPersistence.getItemsForUser(userId);
    }

    @Override
    public Item getItem(UserId userId, ItemId itemId) {
        return itemPersistence.getItem(userId, itemId);
    }

    @Override
    public boolean existsItem(UserId userId, ItemId itemId) {
        Item item = itemPersistence.getItem(userId, itemId);
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
    public Item deleteItem(UserId userId, ItemId itemId) {
        return itemPersistence.deleteItem(userId, itemId);
    }
}
