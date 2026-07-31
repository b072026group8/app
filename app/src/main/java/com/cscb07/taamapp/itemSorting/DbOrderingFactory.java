package com.cscb07.taamapp.itemSorting;

import com.cscb07.taamapp.Item;
import com.cscb07.taamapp.util.Provider;

import java.util.List;
import java.util.Map;

/**
 * Provides an ordering of items, fetching items from a database
 */
public class DbOrderingFactory implements OrderingFactory {
   private final Provider<Map<String, Item>>  provider;

    public DbOrderingFactory(Provider<Map<String, Item>> provider) {
        this.provider = provider;
    }

    @Override
    public Provider<List<Item>> getOrdering(Item item) {
        return new ItemOrdering(item, provider);
    }
}
