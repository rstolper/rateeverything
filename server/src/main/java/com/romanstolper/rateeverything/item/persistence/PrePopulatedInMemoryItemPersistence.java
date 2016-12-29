package com.romanstolper.rateeverything.item.persistence;

import com.romanstolper.rateeverything.item.domain.Item;
import com.romanstolper.rateeverything.item.domain.Rating;
import com.romanstolper.rateeverything.user.domain.UserId;
import com.romanstolper.rateeverything.user.persistence.UserIdGen;

/**
 * Created by roman on 1/18/2015.
 */
public class PrePopulatedInMemoryItemPersistence extends InMemoryItemPersistence {
    public PrePopulatedInMemoryItemPersistence() {
        super();
        UserId roman = UserIdGen.newId();
        UserId bob = UserIdGen.newId();
        this.insertItem(new Item(ItemIdGen.newId(), roman, "item1", "cat1", Rating.YES));
        this.insertItem(new Item(ItemIdGen.newId(), roman,"item2", "cat1", Rating.NO));
        this.insertItem(new Item(ItemIdGen.newId(), roman, "item3", "cat2", Rating.YES));
        this.insertItem(new Item(ItemIdGen.newId(), bob, "item4", "cat2", Rating.MAYBE));
    }
}
