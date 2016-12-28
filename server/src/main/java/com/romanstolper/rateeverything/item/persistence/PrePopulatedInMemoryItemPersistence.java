package com.romanstolper.rateeverything.item.persistence;

import com.romanstolper.rateeverything.item.domain.Item;
import com.romanstolper.rateeverything.item.domain.Rating;

/**
 * Created by roman on 1/18/2015.
 */
public class PrePopulatedInMemoryItemPersistence extends InMemoryItemPersistence {
    public PrePopulatedInMemoryItemPersistence() {
        super();
        this.insertItem(new Item(1, "item1", "cat1", "roman", Rating.YES));
        this.insertItem(new Item(2, "item2", "cat1", "roman", Rating.NO));
        this.insertItem(new Item(3, "item3", "cat2", "roman", Rating.YES));
        this.insertItem(new Item(4, "item4", "cat2", "clark", Rating.MAYBE));
    }
}
