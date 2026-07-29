package com.cscb07.taamapp.itemSorting;

import androidx.annotation.NonNull;

import com.cscb07.taamapp.Item;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;

/**
 * Represents an ordering of items based on similarity to a target item.
 */
public class ItemOrdering {
    @NonNull
    private List<ItemRanking> ranking;

    /**
     * Creates a ranking of the specified collection of items based on some target item.
     * <p>
     * Will skip adding the target item to the ranking.
     */
    public ItemOrdering(@NonNull Item target, @NonNull Collection<Item> items) {
        int capacity = 12;
        if (items instanceof List) {
            List<Item> list = (List<Item>)items;
            capacity = list.size();
        }

        ItemRanker ranker = new ItemRanker();
        ranking = new ArrayList<>();
        for (Item item : items) {
            if (target.equals(item)) {
                continue;
            }
            ranking.add(new ItemRanking(item, ranker.rankSimilarity(target, item)));
        }
        ranking.sort(ItemRanking.COMPARATOR);
    }

    /** Gets the number of items in the ranking */
    public int size() { return ranking.size(); }
    /** Gets the {@link Item} at the specified index. */
    public Item get(int i) { return ranking.get(i).getItem(); }

    /** Internal class to represent the ranking of an item. */
    private static final class ItemRanking {
        public static final Comparator<ItemRanking> COMPARATOR = (t1, t2) ->  t1.getRank() - t2.getRank();
        @NonNull
        private final Item item;
        private final int rank;

        public ItemRanking(@NonNull Item item, int rank) {
            this.item = item;
            this.rank = rank;
        }

        @NonNull
        public Item getItem() {
            return item;
        }

        public int getRank() {
            return rank;
        }
    }
}
