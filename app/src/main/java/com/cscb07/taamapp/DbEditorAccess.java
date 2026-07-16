package com.cscb07.taamapp;

/**
 * The way for {@link EditArtifactFragment} to edit items in the database.
 */
public interface DbEditorAccess {
    /**
     * Retrieve a Lot number that is currently not in use in the database.
     * <p>
     * The value doesn't have to be reserved. That is, it may later be invalid.
     * It is the responsibility of {@link DbEditorAccess#addNewItem} to check that it is unique.
     * @return A unique Lot number that currently isn't in use in the database.
     */
    public String getUniqueLotNumber();

    /**
     * Attempt to edit the item in the database with the matching Lot number, and return whether it
     * is successful.
     * @param item The item to edit.
     * @return {@link DbEditorAccessResult#SUCCESS} if the database was successfully edited,
     *         {@link DbEditorAccessResult#ERROR} otherwise
     */
    public DbEditorAccessResult editItem(Item item);

    /**
     * Attempt to add the item into the database, given its LOT number is unique,
     * and return whether the Lot number is unique and the operation is successful.
     * @param item The item to add.
     * @return {@link DbEditorAccessResult#DUPLICATE_LOT_NUMBER} if the item's LOT value is
     *         already used in the database,
     *         {@link DbEditorAccessResult#SUCCESS} if the item was successfully added,
     *         {@link DbEditorAccessResult#ERROR} otherwise.
     */
    public DbEditorAccessResult addNewItem(Item item);
}
