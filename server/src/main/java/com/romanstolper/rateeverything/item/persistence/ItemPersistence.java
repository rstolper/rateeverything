package com.romanstolper.rateeverything.item.persistence;

import com.romanstolper.rateeverything.item.domain.Item;

import java.util.Collection;

public interface ItemPersistence {
    Item selectItem(long itemId);

    Collection<Item> selectItems();

    Collection<Item> selectItemsByOwner(String owner);

    Item insertItem(Item item);

    Item updateItem(Item item);

    Item deleteItem(long itemId);
}
