package com.cscb07.taamapp.itemSorting;

import androidx.annotation.NonNull;

import com.cscb07.taamapp.Item;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * A wrapper around a {@link List} of {@link Item}s that can
 * filter its content based on a set query.
 * <p>Note: it is initially <b>empty</b>. Use {@link ItemFilterList#queryKeyword} to set items.
 */
public class ItemFilterList extends ArrayList<Item> {
    private final ItemRanker ranker = new ItemRanker();
    @NonNull
    private final List<Item> source;
    @NonNull
    private String query = "";
    public ItemFilterList(@NonNull List<Item> source) {
        super(source.size());
        this.source = source;
        queryKeyword("");
    }

    private static boolean contains(String source, String keyword) {
        return source.toLowerCase(Locale.ROOT).contains(keyword);
    }

    /**
     * Changes the items directly exposed/accessible by {@link List}
     * methods according to the keyword.
     * @param keyword the keyword to filter by. If empty, adds the entire source collection.
     */
    public void queryKeyword(@NonNull String keyword) {
        query = keyword.toLowerCase(Locale.ROOT);
        requery();
    }

    public void requery() {
        this.clear();
        if (query.isEmpty()) {
            this.addAll(source);
            return;
        }
        for(Item item : source) {
            if (ranker.artifactHasKeyword(item, query)) {
                this.add(item);
            }
        }
    }

}
