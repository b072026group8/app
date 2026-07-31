package com.cscb07.taamapp.itemSorting;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.cscb07.taamapp.Item;
import com.cscb07.taamapp.util.Provider;
import com.cscb07.taamapp.util.ReadonlyList;
import com.cscb07.taamapp.util.UpdateListener;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;

/**
 * Provides an ordering of items based on similarity to a target item, and limits what items
 * are displayed.
 */
public class ItemOrdering extends Provider<List<Item>> implements ReadonlyList<Item> {
    @NonNull
    private List<ItemRanking> ranking;
    @NonNull
    private final ItemRanker ranker = new ItemRanker();
    @NonNull
    private Item targetItem;
    @NonNull
    private final Provider<Map<String, Item>> itemProvider;
    private int maxDisplayed = Integer.MAX_VALUE;
    private int minRanking = Integer.MIN_VALUE;

    /** Get the max number of items displayed. */ public int getMaxDisplayed() { return maxDisplayed; }
    /** Set the max number of items displayed. */ public void setMaxDisplayed(int maxDisplayed) {
        if (maxDisplayed <= 0) throw new IllegalArgumentException("maxDisplayed must be positive");
        this.maxDisplayed = maxDisplayed;
    }
    /** Get the min ranking value required for an item to appear. */ public int getMinRanking() { return minRanking; }
    /** Set the min ranking value required for an item to appear. */ public void setMinRanking(int minRanking) { this.minRanking = minRanking; }


    /**
     * Creates a ranking of the specified collection of items based on some target item.
     * <p>
     * Will skip adding the target item to the ranking.
     */
    public ItemOrdering(@NonNull Item target, @NonNull Provider<Map<String, Item>> itemProvider) {
        this.targetItem = target;
        this.itemProvider = itemProvider;
        int capacity = itemProvider.getValue().size();
        if (capacity <= 12) {
            capacity = 12;
        }

        ranking = new ArrayList<>(capacity);
        itemProvider.registerObserver(onCollectionChange);
    }
    @Override
    protected void finalize() {
        itemProvider.unregisterObserver(onCollectionChange);
    }

    private final UpdateListener<Map<String, Item>> onCollectionChange = (items) -> {
        for (Item item : items.values()) {
            if (targetItem.equals(item)) {
                continue;
            }
            int itemRanking = ranker.rankSimilarity(targetItem, item);
            if (itemRanking > minRanking) {
                ranking.add(new ItemRanking(item, itemRanking));
            }
        }
        ranking.sort(ItemRanking.COMPARATOR);
        updateValue();
    };

    /** Gets the number of items in the ranking */
    @Override
    public int size() { return Integer.min(getMaxDisplayed(), ranking.size()); }

    @Override
    public boolean isEmpty() {
        return size() == 0;
    }

    @Override
    public boolean contains(@Nullable Object o) {
        return indexOf(o) != -1;
    }

    @NonNull
    @Override
    public Iterator<Item> iterator() {
        return new Iterator<Item>() {
            int index = -1;
            @Override
            public boolean hasNext() {
                return index + 1 < size();
            }

            @Override
            public Item next() {
                index++;
                return ranking.get(index).getItem();
            }
        };
    }

    @NonNull
    @Override
    public Object[] toArray() {
        Object[] arr = new Object[size()];
        for (int i = 0; i < size(); i++) {
            arr[i] = ranking.get(i).getItem();
        }
        return arr;
    }

    @NonNull
    @Override
    public <T> T[] toArray(@NonNull T[] ts) {
        if (ts.length < size()) {
            return (T[])toArray();
        }
        for (int i = 0; i < size(); i++) {
            ts[i] = (T) ranking.get(i).getItem();
        }
        return ts;
    }

    @Override
    public boolean containsAll(@NonNull Collection<?> collection) {
        for (Object o : collection) {
            if (!contains(o)) {
                return false;
            }
        }
        return true;
    }

    /** Gets the {@link Item} at the specified index. */
    @Override
    public Item get(int i) { return ranking.get(i).getItem(); }

    @Override
    public int indexOf(@Nullable Object o) {
        for (int i = 0; i < size(); i++) {
            if (ranking.get(i).getItem().equals(o)) {
                return i;
            }
        }
        return -1;
    }

    @Override
    public int lastIndexOf(@Nullable Object o) {
        for (int i = size() - 1; i >= 0; i--) {
            if (ranking.get(i).getItem().equals(o)) {
                return i;
            }
        }
        return -1;
    }

    @Override
    public List<Item> getValue() {
        return this;
    }


    private class OrderingIterator implements ListIterator<Item> {
        private int index;
        private OrderingIterator(int index) {
            this.index = index;
        }
        @Override
        public boolean hasNext() {
            return index + 1 < size();
        }
        @Override
        public Item next() {
            return ranking.get(++index).getItem();
        }
        @Override
        public boolean hasPrevious() {
            return index > 0;
        }
        @Override
        public Item previous() {
            return ranking.get(--index).getItem();
        }
        @Override
        public int nextIndex() {
            return index + 1;
        }
        @Override
        public int previousIndex() {
            return index - 1;
        }
        @Override
        public void remove() {
            throw new UnsupportedOperationException();
        }
        @Override
        public void set(Item item) {
            throw new UnsupportedOperationException();
        }
        @Override
        public void add(Item item) {
            throw new UnsupportedOperationException();
        }
    }

    @NonNull
    @Override
    public ListIterator<Item> listIterator() {
        return new OrderingIterator(0);
    }

    @NonNull
    @Override
    public ListIterator<Item> listIterator(int i) {
        return new OrderingIterator(i);
    }

    @NonNull
    @Override
    public List<Item> subList(int i, int i1) {
        throw new UnsupportedOperationException();
    }

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
