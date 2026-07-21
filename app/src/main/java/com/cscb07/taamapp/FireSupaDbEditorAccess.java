package com.cscb07.taamapp;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

/**
 * Default implementation of {@link DbEditorAccess} to use with {@link EditArtifactFragment}.
 */
public class FireSupaDbEditorAccess implements DbEditorAccess{
    public static final String TAG = "FireSupaDbEditorAccess";
    private final FirebaseDatabase db;
    private final DatabaseReference dbRef;
    @Nullable
    private DatabaseReference addedItemRef;
    @Nullable
    private AddedItemChangeListener addChangeListener = null;

    public FireSupaDbEditorAccess() {
        db = FirebaseDatabase.getInstance();
        dbRef = db.getReference("artifacts/");
        Log.i(TAG, dbRef.getKey());
    }

    private void removeAddedKey() {
        if (addedItemRef != null && addChangeListener != null) {
            if (addChangeListener.wasChanged()) {
                Log.w(TAG, "It seems that a reserved item was modified before push. Not removing.");
            } else {
                addedItemRef.removeValue();
            }
            addedItemRef.removeEventListener(addChangeListener);
        }

        addedItemRef = null;
        addChangeListener = null;
    }
    @Override
    public String getUniqueLotNumber() {
        removeAddedKey();
        addedItemRef = dbRef.push();
        Log.i(TAG, "Adding item - created key: " + addedItemRef.getKey());

        return addedItemRef.getKey();
    }

    @Override
    public DbEditorAccessResult editItem(Item item) {
        Log.d(TAG, "editing item, LOT: " + item.getLotNumber());
        Task<Void> task = dbRef.child(item.getLotNumber()).setValue(item);
        task.addOnSuccessListener(v -> Log.i(TAG, "Updated item. LOT: " + item.getLotNumber()));
        task.addOnFailureListener(e -> Log.i(TAG, "exception:", e));
        task.addOnCanceledListener(() -> Log.i(TAG, "canceled"));
        return DbEditorAccessResult.SUCCESS;
    }

    @Override
    public void cancelAdd(@NonNull String lotNumber) {
        if (addedItemRef == null) {
            return;
        }
        if (!lotNumber.isEmpty()) {
            if (!lotNumber.equals(addedItemRef.getKey())) {
                Log.wtf(TAG, "Mismatch between given lot and recorded lot of reserved added item. Not Removing.");
            } else {
                removeAddedKey();
            }
        }
    }

    private static class AddedItemChangeListener implements ValueEventListener {
        private boolean wasChanged = false;
        public boolean wasChanged() {
            return wasChanged;
        }

        @Override
        public void onDataChange(@NonNull DataSnapshot snapshot) {
            wasChanged = snapshot.exists();
            Log.i(TAG, "Added item updated: exists = " + wasChanged);
        }

        @Override
        public void onCancelled(@NonNull DatabaseError error) {
            Log.w(TAG, "listener removed with error", error.toException());
        }
    }
}
