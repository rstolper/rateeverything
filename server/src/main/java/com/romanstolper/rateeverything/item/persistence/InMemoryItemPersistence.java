package com.romanstolper.rateeverything.item.persistence;

import com.romanstolper.rateeverything.item.domain.Item;
import com.romanstolper.rateeverything.item.domain.ItemId;
import com.romanstolper.rateeverything.user.domain.UserId;

import java.util.*;
import java.util.concurrent.ArrayBlockingQueue;

/**
 * In-memory storage
 */
public class InMemoryItemPersistence implements ItemPersistence {

    private Map<UserId, Map<ItemId, Item>> items;

    public InMemoryItemPersistence() {
        this.items = new HashMap<>();
    }

    @Override
    public Item getItem(UserId userId, ItemId itemId) {
        return items.get(userId).get(itemId);
    }

    @Override
    public Collection<Item> getAllItems() {
        Collection<Item> allItems = new ArrayList<>();
        for (Map<ItemId, Item> itemCollection : items.values()) {
            allItems.addAll(itemCollection.values());
        }
        return allItems;
    }

    @Override
    public Collection<Item> getItemsForUser(UserId userId) {
        return items.get(userId).values();
    }

    @Override
    public Item insertItem(Item item) {
        Item newItem = new Item(ItemIdGen.newId(), item);
        newItem.setCreationDate(new Date());
        items.putIfAbsent(item.getUserId(), new HashMap<>());
        items.get(item.getUserId()).put(newItem.getItemId(), item);
        return newItem;
    }

    @Override
    public Item updateItem(Item item) {
        return items.get(item.getUserId()).put(item.getItemId(), item);
    }

    @Override
    public Item deleteItem(UserId userId, ItemId itemId) {
        return items.get(userId).remove(itemId);
    }
}
