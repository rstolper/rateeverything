package com.romanstolper.rateeverything.item.persistence;

import com.romanstolper.rateeverything.item.domain.Item;
import com.romanstolper.rateeverything.item.domain.ItemId;
import com.romanstolper.rateeverything.user.domain.UserId;

import java.util.Collection;

public interface ItemPersistence {
    Item getItem(UserId userId, ItemId itemId);

    Collection<Item> getAllItems();

    Collection<Item> getItemsForUser(UserId userId);

    Item insertItem(Item item);

    Item updateItem(Item item);

    Item deleteItem(UserId userId, ItemId itemId);
}
