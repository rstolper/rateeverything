package com.romanstolper.rateeverything.item.service;

import com.romanstolper.rateeverything.item.domain.Item;
import com.romanstolper.rateeverything.item.domain.Rating;

import java.util.Comparator;

/**
 * Sorts items in descending order by
 */
public class ItemsByRatingDesc implements Comparator<Item> {
    @Override
    public int compare(Item item1, Item item2) {
        if (!item1.getCategory().equals(item2.getCategory())) {
            return item1.getCategory().compareTo(item2.getCategory());
        } else if (item1.getRating() != item2.getRating()) {
            if (item1.getRating() == Rating.YES) {
                return 1;
            } else if (item1.getRating() == Rating.MAYBE && item2.getRating() == Rating.NO) {
                return 1;
            } else {
                return -1;
            }
        } else {
            return item1.getName().compareTo(item2.getName());
        }
    }
}
