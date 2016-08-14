package rateeverything.users.persistence;

import rateeverything.item.domain.Item;

import java.util.Collection;

/**
 * Created by roman on 1/18/2015.
 */
public interface UserPersistence {
    Item selectItem(long itemId);

    Collection<Item> selectItems();

    Collection<Item> selectItemsByOwner(String owner);

    Item insertItem(Item item);

    Item updateItem(Item item);

    Item deleteItem(long itemId);
}
