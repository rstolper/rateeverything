package com.romanstolper.rateeverything.item.service;

import com.romanstolper.rateeverything.item.domain.Item;
import com.romanstolper.rateeverything.item.domain.ItemId;
import com.romanstolper.rateeverything.user.domain.UserId;

import java.util.Collection;

/**
 * Item CRUD interactions
 */
public interface ItemService {
    Collection<Item> getAllItems(UserId userId);

    Item getItem(UserId userId, ItemId itemId);

    boolean existsItem(UserId userId, ItemId itemId);

    Item createItem(Item item);

    Item updateItem(Item item);

    Item deleteItem(UserId userId, ItemId itemId);
}
