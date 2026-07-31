package com.cscb07.taamapp;

import com.cscb07.taamapp.itemSorting.ItemRanker;

import org.junit.Test;
import org.junit.runner.RunWith;
import static org.junit.Assert.*;

import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.Strict.class)
public class ItemRankerTest {
    private static final String TAG = "ItemRankerTest";
    @Test
    public void artifactHasKeyword_in1Field_returnsTrue() {
        final String keyword = "asdf";
        boolean failed = false;
        String[] values = new String[] { "item 1 asdf", "item asdf 2", "asdf item 3", "ASDF item 4", "item ASDF 4", "item 4 ASDF", "item 4 aSDf", "aSDf item 4", "asdf", "ASDF", "aSDf", "Asdf", "123 4 asdf 123 Asdf 90876" };
        int fieldI;
        int valueI;
        for (fieldI = 0; fieldI < 13; fieldI++) {
            for (valueI = 0; valueI < values.length; valueI++) {
                Item item = new Item();
                String value = values[valueI];
                System.out.println(TAG + " - Params: fieldI=" + fieldI + " | value=" + value);
                switch (fieldI) {
                    case 0: item.setArtifactName(value); break;
                    case 1: item.setDescription(value); break;
                    case 2: item.setCategory(value); break;
                    case 3: item.setMaterial(value); break;
                    case 4: item.setDynastyPeriod(value); break;
                    case 5: item.setCulturalOrigin(value); break;
                    case 6: item.setDimensions(value); break;
                    case 7: item.setConditionReport(value); break;
                    case 8: item.setCurrentLocation(value); break;
                    case 9: item.setAcquisitionMethod(value); break;
                    case 10: item.setProvenance(value); break;
                    case 11: item.setAccessionNumber(value); break;
                    case 12: item.setNotes(value); break;
                }
                ItemRanker ranker = new ItemRanker();

                boolean result = ranker.artifactHasKeyword(item, keyword);

                try {
                    assertTrue(result);
                } catch (Throwable e) {
                    System.out.println("E: " + e + e.getMessage());
                    failed = true;
                }
            }
        }
        if (failed) {
            fail("Some test cases failed.");
        }
    }

    @Test
    public void rankSimilarity_rankingDifferentObjects_expectedSimilarityOrder() {
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
        ItemRanker ranker = new ItemRanker();

        int expectedHighest = ranker.rankSimilarity(target, mostSimilar);
        int expectedMedium = ranker.rankSimilarity(target, mediumSimilar);
        int expectedLowest = ranker.rankSimilarity(target, leastSimilar);

        assertTrue(expectedHighest > expectedMedium);
        assertTrue(expectedMedium > expectedLowest);
    }
}
