package com.romanstolper.rateeverything.item.persistence;

import com.romanstolper.rateeverything.item.domain.Item;

import java.util.*;

/**
 * Created by roman on 1/3/15.
 */
public class InMemoryItemPersistence implements ItemPersistence {

    private Map<Long, Item> items;
    private long nextItemId = 1;

    public InMemoryItemPersistence() {
        this.items = new HashMap<>();
    }

    @Override
    public Item selectItem(long itemId) {
        return items.get(itemId);
    }

    @Override
    public Collection<Item> selectItems() {
        return items.values();
    }

    @Override
    public Collection<Item> selectItemsByOwner(String owner) {
        Collection<Item> itemsByOwner = new ArrayList<>();
        for (Item item : items.values()) {
            if (item.getOwner().equals(owner)) {
                itemsByOwner.add(item);
            }
        }
        return itemsByOwner;
    }

    @Override
    public Item insertItem(Item item) {
        Item newItem = new Item(getNewId(), item);
        newItem.setCreationDate(new Date());
        items.put(newItem.getId(), newItem);
        return newItem;
    }

    @Override
    public Item updateItem(Item item) {
        return items.put(item.getId(), item);
    }

    @Override
    public Item deleteItem(long itemId) {
        return items.remove(itemId);
    }

    private long getNewId() {
        return nextItemId++;
    }
}
