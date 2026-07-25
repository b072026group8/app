package com.cscb07.taamapp;

import androidx.annotation.NonNull;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashSet;

/**
 * Class for getting a collection of artifacts saved from a particular user.
 */
public class SavedArtifactReader {
    private final String Tag = "SavedArtifactReader";
    private static SavedArtifactReader current = null;

    public static SavedArtifactReader getInstance(@NonNull String uid) {
        if (current == null || !current.uid.equals(uid)) {
            current = new SavedArtifactReader(uid);
        }
        return current;
    }

    private final String uid;
    private final DatabaseReference ref;
    private SavedArtifactReader(String uid) {
        if (uid == null) {
            throw new IllegalArgumentException("arg 'uid' was null.");
        }
        this.uid = uid;
        ref = FirebaseDatabase.getInstance()
                .getReference(SavedArtifactWriter.DB_PATH)
                .child(uid);
    }

    public void addOnSavedArtifactChangedListener(@NonNull String lot, @NonNull ValueEventListener listener) {
        ref.child(lot).addValueEventListener(listener);
    }

    public void addOnChangedListener(@NonNull ValueEventListener listener) {
        ref.addValueEventListener(listener);
    }
}
