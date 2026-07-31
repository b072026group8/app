package com.cscb07.taamapp.itemSorting;

import com.cscb07.taamapp.Item;
import com.cscb07.taamapp.testutil.CallbackCounter;
import com.cscb07.taamapp.util.Provider;

import org.junit.Test;
import org.junit.runner.RunWith;
import static org.junit.Assert.*;
import org.mockito.junit.MockitoJUnitRunner;

import androidx.annotation.NonNull;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.NoSuchElementException;

@RunWith(MockitoJUnitRunner.Strict.class)
public class ItemOrderingTest {
    @NonNull
    private static Item createSampleTarget() {
        return new Item("lot", "iron shield", "description", "category", "material", "dynasty", "", "", "", "", "", "", "", "", "");
    }

    @Test
    public void constructor_sampleProvider_IsSorted() {
        Item target = createSampleTarget();
        Item mostSimilar = new Item("lot", "iron", "shield","iron", "shield","iron", "shield","iron", "shield","iron", "shield","iron", "shield","iron", "");
        mostSimilar.setCategory(target.getCategory());
        mostSimilar.setDescription(target.getDescription());
        mostSimilar.setDynastyPeriod(target.getDynastyPeriod());
        Item leastSimilar = new Item();
        leastSimilar.setNotes("shield");
        HashMap<String, Item> map = new HashMap<>();
        map.put("1", leastSimilar);
        map.put("2", mostSimilar);
        Provider<Map<String, Item>> provider = new Provider<Map<String, Item>>() {
            @Override
            public Map<String, Item> getValue() {
                return map;
            }
        };

        ItemOrdering ordering = new ItemOrdering(target, provider);

        assertEquals(2, ordering.size());
        assertEquals(mostSimilar, ordering.get(0));
        assertEquals(leastSimilar, ordering.get(1));
    }

    @Test
    public void constructor_providerWithSameItem_FilteredOut() {
        Item target = createSampleTarget();
        Item sameTarget = createSampleTarget();
        Item different = new Item();
        different.setLotNumber("123");
        HashMap<String, Item> map = new HashMap<>();
        map.put("1", target);
        map.put("2", sameTarget);
        map.put("3", different);
        Provider<Map<String, Item>> provider = new Provider<Map<String, Item>>() {
            @Override
            public Map<String, Item> getValue() {
               return map;
            }
        };

        ItemOrdering ordering = new ItemOrdering(target, provider);

        assertEquals(1, ordering.size());
        assertEquals(different, ordering.get(0));
    }

    @Test
    public void providerCallback_sourceChanged_orderingUpdated() {
        Item target = createSampleTarget();
        Item other1 = new Item();
        Item other2 = new Item();
        HashMap<String, Item> map = new HashMap<>();
        Provider<Map<String, Item>> provider = new Provider<Map<String, Item>>() {
            @Override
            public Map<String, Item> getValue() {
                return map;
            }
        };
        ItemOrdering ordering = new ItemOrdering(target, provider);

        map.put("1", target);
        map.put("2", other1);
        map.put("3", other2);
        provider.updateValue();

        assertEquals(2, ordering.size());
    }

    @Test
    public void orderingCallback_sourceChanged_callbackCalled() {
        Item target = createSampleTarget();
        Item other1 = new Item();
        Item other2 = new Item();
        HashMap<String, Item> map = new HashMap<>();
        Provider<Map<String, Item>> provider = new Provider<Map<String, Item>>() {
            @Override
            public Map<String, Item> getValue() {
                return map;
            }
        };
        ItemOrdering ordering = new ItemOrdering(target, provider);
        CallbackCounter<List<Item>> counter = new CallbackCounter<>();
        ordering.registerObserver(counter::signal);
        // This is b/c registerObserver() automatically calls listeners initially.
        counter.reset();

        map.put("1", target);
        map.put("2", other1);
        map.put("3", other2);
        provider.updateValue();

        assertEquals(1, counter.getCallbackCount());
        assertEquals(2, counter.getReturnValues().get(0).size());
        assertSame(ordering.getValue(), counter.getReturnValues().get(0));
    }

    /** Returns a list of 4 items in order: a target, most similar, kinda similar, least similar. */
    public Item[] getSampleItems() {
        Item target = new Item("lot", "iron shield", "description", "category", "material", "dynasty", "", "", "", "", "", "", "", "", "");
        Item mostSimilar = new Item("lot", "iron", "shield","iron", "shield","iron", "shield","iron", "shield","iron", "shield","iron", "shield","iron", "");
        mostSimilar.setCategory(target.getCategory());
        mostSimilar.setDescription(target.getDescription());
        mostSimilar.setDynastyPeriod(target.getDynastyPeriod());
        Item mediumSimilar = new Item();
        mediumSimilar.setCategory(target.getCategory());
        mediumSimilar.setDescription(target.getDescription());
        mediumSimilar.setDynastyPeriod(target.getDynastyPeriod());
        Item leastSimilar = new Item();
        leastSimilar.setNotes("shield");
        return new Item[] {target, mostSimilar, mediumSimilar, leastSimilar};
    }

    @Test
    public void setMaxDisplayed_valueLessThanSize_limitsLowestValue() {
        Item[] itemPrep = getSampleItems();
        Item
                target = itemPrep[0],
                mostSimilar = itemPrep[1],
                kindaSimilar = itemPrep[2],
                leastSimilar = itemPrep[3];
        HashMap<String, Item> map = new HashMap<>();
        map.put("0", target);
        map.put("1", mostSimilar);
        map.put("2", kindaSimilar);
        map.put("3", leastSimilar);
        Provider<Map<String, Item>> provider = new Provider<Map<String, Item>>() {
            @Override
            public Map<String, Item> getValue() {
                return map;
            }
        };
        ItemOrdering ordering = new ItemOrdering(target, provider);
        CallbackCounter<List<Item>> counter = new CallbackCounter<>();
        ordering.registerObserver(counter::signal);
        // This is b/c registerObserver() automatically calls listeners initially.
        counter.reset();


        ordering.setMaxDisplayed(2);


        assertEquals(2, ordering.getValue().size());
        assertFalse(ordering.getValue().contains(leastSimilar));
        assertEquals(1, counter.getCallbackCount());
        assertEquals(2, counter.getReturnValues().get(0).size());
        assertFalse(counter.getReturnValues().get(0).contains(leastSimilar));
    }

    @Test
    public void setMinRanking_valueLessThanSize_limitsLowestValue() {
        Item[] itemPrep = getSampleItems();
        Item
                target = itemPrep[0],
                mostSimilar = itemPrep[1],
                kindaSimilar = itemPrep[2],
                leastSimilar = itemPrep[3];
        final int minRanking = new ItemRanker().rankSimilarity(target, kindaSimilar);
        HashMap<String, Item> map = new HashMap<>();
        map.put("0", target);
        map.put("1", mostSimilar);
        map.put("2", kindaSimilar);
        map.put("3", leastSimilar);
        Provider<Map<String, Item>> provider = new Provider<Map<String, Item>>() {
            @Override
            public Map<String, Item> getValue() {
                return map;
            }
        };
        ItemOrdering ordering = new ItemOrdering(target, provider);
        CallbackCounter<List<Item>> counter = new CallbackCounter<>();
        ordering.registerObserver(counter::signal);
        // This is b/c registerObserver() automatically calls listeners initially.
        counter.reset();


        ordering.setMinRanking(minRanking);


        assertEquals(2, ordering.getValue().size());
        assertFalse(ordering.getValue().contains(leastSimilar));
        assertEquals(1, counter.getCallbackCount());
        assertEquals(2, counter.getReturnValues().get(0).size());
        assertFalse(counter.getReturnValues().get(0).contains(leastSimilar));
    }

    @Test
    public void listMethods_sampleSource_positiveResult() {
        Item target = createSampleTarget();
        Item mostSimilar = new Item("lot", "iron", "shield","iron", "shield","iron", "shield","iron", "shield","iron", "shield","iron", "shield","iron", "");
        mostSimilar.setCategory(target.getCategory());
        mostSimilar.setDescription(target.getDescription());
        mostSimilar.setDynastyPeriod(target.getDynastyPeriod());
        Item leastSimilar = new Item();
        leastSimilar.setNotes("shield");
        HashMap<String, Item> map = new HashMap<>();
        map.put("1", leastSimilar);
        map.put("2", mostSimilar);
        Provider<Map<String, Item>> provider = new Provider<Map<String, Item>>() {
            @Override
            public Map<String, Item> getValue() {
                return map;
            }
        };

        ItemOrdering ordering = new ItemOrdering(target, provider);

        assertFalse(ordering.isEmpty());
        assertTrue(ordering.contains(mostSimilar));
        assertTrue(ordering.toArray().length != 0);
        assertTrue(ordering.lastIndexOf(mostSimilar) >= 0);
    }

    @Test
    public void listMethods_sampleSource_negativeResult() {
        Item target = createSampleTarget();
        HashMap<String, Item> map = new HashMap<>();
        Provider<Map<String, Item>> provider = new Provider<Map<String, Item>>() {
            @Override
            public Map<String, Item> getValue() {
                return map;
            }
        };

        ItemOrdering ordering = new ItemOrdering(target, provider);

        assertTrue(ordering.isEmpty());
        assertFalse(ordering.contains(target));
        assertEquals(0, ordering.toArray().length);
        assertTrue(ordering.lastIndexOf(target) != 0);
    }

    @Test
    public void iterator_sampleSource_canIterateExpectedAmount() {
        Item target = createSampleTarget();
        Item mostSimilar = new Item("lot", "iron", "shield","iron", "shield","iron", "shield","iron", "shield","iron", "shield","iron", "shield","iron", "");
        mostSimilar.setCategory(target.getCategory());
        mostSimilar.setDescription(target.getDescription());
        mostSimilar.setDynastyPeriod(target.getDynastyPeriod());
        Item leastSimilar = new Item();
        leastSimilar.setNotes("shield");
        HashMap<String, Item> map = new HashMap<>();
        map.put("1", leastSimilar);
        map.put("2", mostSimilar);
        Provider<Map<String, Item>> provider = new Provider<Map<String, Item>>() {
            @Override
            public Map<String, Item> getValue() {
                return map;
            }
        };
        ItemOrdering ordering = new ItemOrdering(target, provider);

        Iterator<Item> iterator = ordering.iterator();

        assertEquals(2, ordering.size());
        assertTrue(iterator.hasNext());
        assertEquals(mostSimilar, iterator.next());
        assertTrue(iterator.hasNext());
        assertEquals(leastSimilar, iterator.next());
        assertFalse(iterator.hasNext());
        assertThrows(NoSuchElementException.class, iterator::next);
    }

    @Test
    public void listIterator_sampleSource_canIterateExpectedAmount() {
        Item target = createSampleTarget();
        Item mostSimilar = new Item("lot", "iron", "shield","iron", "shield","iron", "shield","iron", "shield","iron", "shield","iron", "shield","iron", "");
        mostSimilar.setCategory(target.getCategory());
        mostSimilar.setDescription(target.getDescription());
        mostSimilar.setDynastyPeriod(target.getDynastyPeriod());
        Item leastSimilar = new Item();
        leastSimilar.setNotes("shield");
        HashMap<String, Item> map = new HashMap<>();
        map.put("1", leastSimilar);
        map.put("2", mostSimilar);
        Provider<Map<String, Item>> provider = new Provider<Map<String, Item>>() {
            @Override
            public Map<String, Item> getValue() {
                return map;
            }
        };
        ItemOrdering ordering = new ItemOrdering(target, provider);


        ListIterator<Item> iterator = ordering.listIterator(1);


        assertEquals(2, ordering.size());
        //
        assertTrue(iterator.hasNext());
        assertEquals(1, iterator.nextIndex());
        assertEquals(leastSimilar, iterator.next());
        //
        assertTrue(iterator.hasPrevious());
        assertEquals(0, iterator.previousIndex());
        assertEquals(mostSimilar, iterator.previous());
        //
        assertFalse(iterator.hasPrevious());
        assertEquals(-1, iterator.previousIndex());
        //
        assertEquals(leastSimilar, iterator.next());
        assertFalse(iterator.hasNext());
        assertEquals(2, iterator.nextIndex());
        assertThrows(NoSuchElementException.class, iterator::next);
    }
}
