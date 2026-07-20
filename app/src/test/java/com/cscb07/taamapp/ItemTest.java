package com.cscb07.taamapp;

import org.junit.Test;

import static org.junit.Assert.*;

public class ItemTest {
    private Item getSomeItem() {
        return new Item("A01231",
                "Shield",
                "very brittle old shield.",
                "cat1",
                "metal bronze",
                "old dynasty",
                "Some group",
                "40cm x 40cm disk",
                "edges heavily chipped",
                "Museum",
                "Donated",
                "Some specific location",
                "138918234",
                "Very curious notes here" ,
                ""
        );
    }

    @Test
    public void equals_2Refs_ReturnsTrue() {
        Item i1 = getSomeItem(), i2 = getSomeItem();

        boolean result = i1.equals(i2);

        assertTrue(result);
    }

    @Test
    public void hashCode_2SameRefs_HashCodesEqual() {
        Item i1 = getSomeItem(), i2 = getSomeItem();

        int hash1 = i1.hashCode(), hash2 = i2.hashCode();

        assertEquals(hash1, hash2);
    }

    @Test
    public void equals_NullRef_ReturnsFalse() {
        Item item = getSomeItem();

        //noinspection ConstantValue
        boolean result = item.equals(null);

        //noinspection ConstantValue
        assertFalse(result);
    }
}
