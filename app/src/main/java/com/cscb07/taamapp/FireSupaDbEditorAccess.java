package com.cscb07.taamapp;

import android.util.Log;

/**
 * Default implemented of {@link DbEditorAccess} to use with {@link EditArtifactFragment}.
 */
public class FireSupaDbEditorAccess implements DbEditorAccess{
    public static final String TAG = "FireSupaDbEditorAccess";
    @Override
    public String getUniqueLotNumber() {
        Log.wtf(TAG, "not implemented");
        return "Not Implemented";
    }

    @Override
    public DbEditorAccessResult editItem(Item item) {
        Log.wtf(TAG, "not implemented");
        return DbEditorAccessResult.ERROR;
    }

    @Override
    public void cancelAdd(String lotNumber) {
        Log.wtf(TAG, "not implemented");
    }
}
