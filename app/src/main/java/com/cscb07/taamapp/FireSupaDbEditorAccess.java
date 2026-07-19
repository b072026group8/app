package com.cscb07.taamapp;

import android.util.Log;

import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

/**
 * Default implementation of {@link DbEditorAccess} to use with {@link EditArtifactFragment}.
 */
public class FireSupaDbEditorAccess implements DbEditorAccess{
    public static final String TAG = "FireSupaDbEditorAccess";
    private final FirebaseDatabase db;
    private final DatabaseReference dbRef;

    public FireSupaDbEditorAccess() {
        db = FirebaseDatabase.getInstance();
        dbRef = db.getReference("artifacts/");
    }
    @Override
    public String getUniqueLotNumber() {
        Log.wtf(TAG, "not implemented");
        return "Not Implemented";
    }

    @Override
    public DbEditorAccessResult editItem(Item item) {
        Task<Void> task = dbRef.child(item.getLotNumber()).setValue(item);
        task.addOnSuccessListener(v -> Log.i(TAG, "Updated item. LOT: " + item.getLotNumber()));
        return DbEditorAccessResult.SUCCESS;
    }

    @Override
    public void cancelAdd(String lotNumber) {
        Log.wtf(TAG, "not implemented");
    }
}
