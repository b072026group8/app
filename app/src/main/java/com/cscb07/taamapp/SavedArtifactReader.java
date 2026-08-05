package com.cscb07.taamapp;

import androidx.annotation.NonNull;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashSet;

/**
 * Reads the collection of artifacts saved by a specific user.
 * Provides listeners for changes to individual saved artifacts
 * or the user's entire saved artifact collection.
 */
public class SavedArtifactReader {
    private final String Tag = "SavedArtifactReader";
    private static SavedArtifactReader current = null;

    /**
     * Returns the SavedArtifactReader instance for the specified user.
     * Creates a new instance if one does not already exist for that user.
     *
     * @param uid the user's unique ID
     * @return the SavedArtifactReader instance
     */
    public static SavedArtifactReader getInstance(@NonNull String uid) {
        if (current == null || !current.uid.equals(uid)) {
            current = new SavedArtifactReader(uid);
        }
        return current;
    }

    private final String uid;
    private final DatabaseReference ref;

    /**
     * Creates a reader for a user's saved artifacts.
     *
     * @param uid the user's unique ID
     * @throws IllegalArgumentException if the user ID is null
     */
    private SavedArtifactReader(String uid) {
        if (uid == null) {
            throw new IllegalArgumentException("arg 'uid' was null.");
        }
        this.uid = uid;
        ref = FirebaseDatabase.getInstance()
                .getReference(SavedArtifactWriter.DB_PATH)
                .child(uid);
    }

    /**
     * Registers a listener for changes to a specific saved artifact.
     *
     * @param lot the artifact lot number
     * @param listener the listener to receive updates
     */
    public void addOnSavedArtifactChangedListener(@NonNull String lot, @NonNull ValueEventListener listener) {
        ref.child(lot).addValueEventListener(listener);
    }

    /**
     * Registers a listener for changes to the user's saved artifact collection.
     *
     * @param listener the listener to receive updates
     */
    public void addOnChangedListener(@NonNull ValueEventListener listener) {
        ref.addValueEventListener(listener);
    }
}
