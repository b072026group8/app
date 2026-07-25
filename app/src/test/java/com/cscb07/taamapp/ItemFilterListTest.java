package com.cscb07.taamapp;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import static org.junit.Assert.*;

import com.cscb07.taamapp.itemSorting.ItemFilterList;

import java.util.ArrayList;

@RunWith(MockitoJUnitRunner.Strict.class)
public class ItemFilterListTest {

    @Test
    public void constructor_sampleSource_defaultsDisplaysAll() {
        ArrayList<Item> items = new ArrayList<>();
        items.add(new Item());
        items.add(new Item());
        items.add(new Item());
        items.get(0).setArtifactName("item 1");
        items.get(1).setArtifactName("item 2");
        items.get(2).setArtifactName("item 3");
        ItemFilterList sut;

        sut = new ItemFilterList(items);

        assertEquals(3, sut.size());
        assertTrue(sut.contains(sut.get(0)));
        assertTrue(sut.contains(sut.get(1)));
        assertTrue(sut.contains(sut.get(2)));
    }

    @Test
    public void queryKeyword_sampleSource_FiltersByAllFieldsCaseInvariant() {
        final String keyword = "asdf";
        ArrayList<Item> matchingItems = new ArrayList<>();
        matchingItems.add(new Item());
        matchingItems.add(new Item());
        matchingItems.add(new Item());
        matchingItems.add(new Item());
        matchingItems.add(new Item());
        matchingItems.add(new Item());
        matchingItems.add(new Item());
        matchingItems.add(new Item());
        matchingItems.add(new Item());
        matchingItems.add(new Item());
        matchingItems.add(new Item());
        matchingItems.add(new Item());
        matchingItems.add(new Item());
        matchingItems.get(0).setArtifactName("item 1 asdf");
        matchingItems.get(1).setDescription("item asdf 2");
        matchingItems.get(2).setCategory("asdf item 3");
        matchingItems.get(3).setMaterial("ASDF item 4");
        matchingItems.get(4).setDynastyPeriod("item ASDF 4");
        matchingItems.get(5).setCulturalOrigin("item 4 ASDF");
        matchingItems.get(6).setDimensions("item 4 aSDf");
        matchingItems.get(7).setConditionReport("aSDf item 4");
        matchingItems.get(8).setCurrentLocation("asdf");
        matchingItems.get(9).setAcquisitionMethod("ASDF");
        matchingItems.get(10).setProvenance("aSDf");
        matchingItems.get(11).setAccessionNumber("Asdf");
        matchingItems.get(12).setNotes("123 4 asdf 123 Asdf 90876");
        ArrayList<Item> totalItems = new ArrayList<>(matchingItems);
        totalItems.add(new Item());
        totalItems.add(new Item("asd", "sdf", "as df", "AS DF", "ASD", "AS1DF", "fdsa", "FDSA", "asmdf", "a s d f", "asf", ".as.df.", "as|df", "as*df", "asd)f"));
        ItemFilterList sut = new ItemFilterList(totalItems);

        sut.queryKeyword(keyword);

        assertEquals(sut.size(), matchingItems.size());
        assertTrue(sut.containsAll(matchingItems));
    }

    @Test
    public void queryKeyword_emptyQuery_getsAll() {
        ArrayList<Item> items = new ArrayList<>();
        items.add(new Item());
        items.add(new Item());
        items.add(new Item());
        items.get(0).setArtifactName("item 1");
        items.get(1).setArtifactName("item 2");
        items.get(2).setArtifactName("item 3");
        ItemFilterList sut = new ItemFilterList(items);
        sut.queryKeyword("impossible, should be nothing");
        assertEquals(0, sut.size());

        sut.queryKeyword("");

        assertEquals(3, sut.size());
        assertTrue(sut.contains(sut.get(0)));
        assertTrue(sut.contains(sut.get(1)));
        assertTrue(sut.contains(sut.get(2)));
    }

    @Test
    public void requery_sourceChanges_updatesCollection() {
        final String query = "impossible-query-asdf";
        ArrayList<Item> items = new ArrayList<>();
        items.add(new Item());
        items.add(new Item());
        items.add(new Item());
        items.get(0).setArtifactName("item 1");
        items.get(1).setArtifactName("item 2");
        items.get(2).setArtifactName("item 3");
        ItemFilterList sut = new ItemFilterList(items);
        sut.queryKeyword(query);
        assertEquals(0, sut.size());
        items.add(new Item());
        items.get(3).setMaterial(query);

        sut.requery();

        assertEquals(1, sut.size());
        assertTrue(sut.contains(items.get(3)));
    }
}
