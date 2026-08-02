package com.cscb07.taamapp.itemSorting;

import com.cscb07.taamapp.Item;
import com.cscb07.taamapp.util.Provider;

import org.junit.Test;
import static org.junit.Assert.*;

import java.util.HashMap;
import java.util.Map;

public class DbOrderingFactoryTest {
    @Test
    public void getOrdering_sampleSettingsProvider_passedToReturnValue() {
        ItemOrdering.FilterSettings settings = new ItemOrdering.FilterSettings();
        settings.setMaxDisplayed(21);
        settings.setMinDisplayed(12);
        settings.setMinRanking(-312);
        Item item1 = new Item();
        item1.setArtifactName("asdf");
        Item item2 = new Item();
        item2.setArtifactName("different");
        HashMap<String, Item> map = new HashMap<>();
        map.put("different", item2);
        Provider<Map<String,Item>> provider = new Provider<Map<String, Item>>() {
            @Override
            public Map<String, Item> getValue() {
               return map;
            }
        };

        DbOrderingFactory factory = new DbOrderingFactory(provider, settings);
        ItemOrdering ordering = factory.getOrdering(item1);

        assertEquals(settings.getMaxDisplayed(), ordering.getMaxDisplayed());
        assertEquals(settings.getMinDisplayed(), ordering.getMinDisplayed());
        assertEquals(settings.getMinRanking(), ordering.getMinRanking());
        assertEquals(1, ordering.size());
        map.put("different 2", item2);
        provider.updateValue();
        assertEquals(2, ordering.size());
    }

    @Test
    public void constructor_explicitNumbers_passedToReturnValue() {
        ItemOrdering.FilterSettings settings = new ItemOrdering.FilterSettings();
        settings.setMinDisplayed(12);
        settings.setMaxDisplayed(21);
        settings.setMinRanking(-312);
        Item item1 = new Item();
        item1.setArtifactName("asdf");
        Item item2 = new Item();
        item2.setArtifactName("different");
        HashMap<String, Item> map = new HashMap<>();
        map.put("different", item2);
        Provider<Map<String,Item>> provider = new Provider<Map<String, Item>>() {
            @Override
            public Map<String, Item> getValue() {
                return map;
            }
        };

        DbOrderingFactory factory = new DbOrderingFactory(provider, settings.getMinDisplayed(), settings.getMaxDisplayed(), settings.getMinRanking());
        ItemOrdering ordering = factory.getOrdering(item1);

        assertEquals(settings.getMaxDisplayed(), ordering.getMaxDisplayed());
        assertEquals(settings.getMinDisplayed(), ordering.getMinDisplayed());
        assertEquals(settings.getMinRanking(), ordering.getMinRanking());
        assertEquals(1, ordering.size());
        map.put("different 2", item2);
        provider.updateValue();
        assertEquals(2, ordering.size());
    }
}
