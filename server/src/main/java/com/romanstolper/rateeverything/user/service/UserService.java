package com.romanstolper.rateeverything.user.service;

import com.romanstolper.rateeverything.item.domain.Item;

import java.util.Collection;

/**
 * Created by roman on 1/18/2015.
 */
public interface UserService {
    Collection<Item> getAllItems();

    Collection<Item> getAllItemsByOwner(String owner);

    Item getItem(long itemId);

    boolean existsItem(long itemId);

    Item createItem(Item item);

    Item updateItem(Item item);

    Item deleteItem(long itemId);
}
