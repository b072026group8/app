package com.cscb07.taamapp.itemSorting;

import com.cscb07.taamapp.Item;
import com.cscb07.taamapp.ItemMapProvider;
import com.cscb07.taamapp.namedAlias.ItemMappingProvider;
import com.cscb07.taamapp.util.Provider;

import java.util.List;

/**
 * Provides an ordering of items, fetching items from a database
 */
public class DbOrderingFactory implements OrderingFactory {
    @Override
    public Provider<List<Item>> getOrdering(Item item) {
        ItemMappingProvider provider = ServiceProvider.getInstance().getService(ItemMapProvider.class);
        return new ItemOrdering(item, provider);
    }
}
