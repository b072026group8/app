package com.cscb07.taamapp.itemSorting;

import com.cscb07.taamapp.Item;
import com.cscb07.taamapp.ItemMapProvider;
import com.cscb07.taamapp.namedAlias.ItemMappingProvider;
import com.cscb07.taamapp.util.Provider;
import com.cscb07.taamapp.util.ServiceProvider;

import java.util.List;

/**
 * Provides an ordering of items, fetching items from a database
 * (specifically an instance of {@link ItemMappingProvider} provided as a service via {@link ServiceProvider}).
 */
public class DbOrderingFactory implements OrderingFactory {
    @Override
    public Provider<List<Item>> getOrdering(Item item) {
        ItemMappingProvider provider = ServiceProvider.getInstance().getService(ItemMapProvider.class);
        return new ItemOrdering(item, provider);
    }
}
