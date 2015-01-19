package rateeverything.item;

import com.google.inject.AbstractModule;
import rateeverything.item.service.ItemService;
import rateeverything.item.service.ItemServiceImpl;

import javax.inject.Singleton;

/**
 * Created by roman on 1/18/2015.
 */
public class ItemModule extends AbstractModule {
    @Override
    protected void configure() {
        bind(ItemService.class).to(ItemServiceImpl.class); //.in(Singleton.class);
    }
}
