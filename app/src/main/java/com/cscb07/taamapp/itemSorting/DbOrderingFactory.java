package com.cscb07.taamapp.itemSorting;

import androidx.annotation.Nullable;

import com.cscb07.taamapp.Item;
import com.cscb07.taamapp.util.Provider;

import java.util.Map;

/**
 * Provides an ordering of items, fetching items from a database
 */
public class DbOrderingFactory implements OrderingFactory {
    private final Provider<Map<String, Item>> provider;
    @Nullable
    private final ItemOrdering.FilterSettings settings;

    public DbOrderingFactory(Provider<Map<String, Item>> provider) {
        this.provider = provider;
        settings = null;
    }
    public DbOrderingFactory(Provider<Map<String, Item>> provider, int minDisplayed, int maxDisplayed, int minRanking) {
        this(provider, new ItemOrdering.FilterSettings());
        settings.setMinDisplayed(minDisplayed);
        settings.setMaxDisplayed(maxDisplayed);
        settings.setMinRanking(minRanking);
    }
    public DbOrderingFactory(Provider<Map<String, Item>> provider, @Nullable ItemOrdering.FilterSettings settings) {
        this.provider = provider;
        this.settings = settings;
    }

    @Override
    public ItemOrdering getOrdering(Item item) {
        return new ItemOrdering(item, provider, settings);
    }
}
