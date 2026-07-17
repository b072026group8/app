package com.cscb07.taamapp;

/**
 * The way for {@link EditArtifactFragment} to edit items in the database.
 */
public interface DbEditorAccess {
    /**
     * gets a Lot number that is currently not in use in the database,
     * and reserves it to potentially be later edited.
     * @return A unique Lot number that currently isn't in use in the database.
     */
    public String getUniqueLotNumber();

    /**
     * Attempt to edit the item in the database with the matching Lot number, and return whether
     * the operation was successful.
     * @param item The item to edit.
     * @return {@link DbEditorAccessResult#SUCCESS} if the database was successfully edited,<br/>
     *         {@link DbEditorAccessResult#ERROR} otherwise
     */
    public DbEditorAccessResult editItem(Item item);

    /**
     * Removes the entry in the database that was reserved by
     * {@link DbEditorAccess#getUniqueLotNumber}.
     * @param lotNumber The Lot value of the reserved entry to remove.
     */
    public void cancelAdd(String lotNumber);
}
