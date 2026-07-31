package com.cscb07.taamapp.itemSorting;

import com.cscb07.taamapp.Item;
import com.cscb07.taamapp.util.Provider;

import java.util.List;

/**
 * Provides an ordering of items, based on some source item.
 */
public interface OrderingFactory {
    /** Provides an ordering of items, based on some source item. */
    Provider<List<Item>> getOrdering(Item item);
}
