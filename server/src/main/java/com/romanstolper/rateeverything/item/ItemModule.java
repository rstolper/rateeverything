package com.romanstolper.rateeverything.item;

import com.google.inject.AbstractModule;
import com.romanstolper.rateeverything.item.service.ItemService;
import com.romanstolper.rateeverything.item.service.ItemServiceImpl;

/**
 * Created by roman on 1/18/2015.
 */
public class ItemModule extends AbstractModule {
    @Override
    protected void configure() {
        bind(ItemService.class).to(ItemServiceImpl.class); //.in(Singleton.class);
    }
}
