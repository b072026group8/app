package com.cscb07.taamapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import com.cscb07.taamapp.util.OperationListener;

/**
 * Manages adding and removing artifacts from a user's saved collection.
 * Writes changes to the Firebase Realtime Database.
 */
public class SavedArtifactWriter {
    public static final String DB_PATH = "saved_collection/";
    private final DatabaseReference collectionRoot;

    /**
     * Creates a writer for managing users' saved artifact collections.
     */
    public SavedArtifactWriter() {
        collectionRoot = FirebaseDatabase.getInstance().getReference(DB_PATH);
    }

    /**
     * Adds an artifact to a user's saved collection.
     *
     * @param userId the user's unique ID
     * @param artifactLot the artifact lot number
     */
    public void addSavedArtifact(@NonNull String userId, @NonNull String artifactLot) {
        addSavedArtifact(userId, artifactLot, null);
    }

    /**
     * Adds an artifact to a user's saved collection and optionally
     * notifies a listener when the operation completes.
     *
     * @param userId the user's unique ID
     * @param artifactLot the artifact lot number
     * @param listener the listener notified when the operation completes
     */
    public void addSavedArtifact(@NonNull String userId, @NonNull String artifactLot, @Nullable OperationListener<Void> listener) {
        Task<Void> task = collectionRoot.child(userId).child(artifactLot).setValue(true);
        if (listener != null) {
            task.addOnSuccessListener(listener::onSuccess);
            task.addOnFailureListener(listener::onFailure);
        }
    }

    /**
     * Removes an artifact from a user's saved collection.
     *
     * @param userId the user's unique ID
     * @param artifactLot the artifact lot number
     */
    public void removeSavedArtifact(@NonNull String userId, @NonNull String artifactLot) {
        removeSavedArtifact(userId, artifactLot, null);
    }

    /**
     * Removes an artifact from a user's saved collection and optionally
     * notifies a listener when the operation completes.
     *
     * @param userId the user's unique ID
     * @param artifactLot the artifact lot number
     * @param listener the listener notified when the operation completes
     */
    public void removeSavedArtifact(@NonNull String userId, @NonNull String artifactLot, @Nullable OperationListener<Void> listener) {
        Task<Void> task = collectionRoot.child(userId).child(artifactLot).removeValue();
        if (listener != null) {
            task.addOnSuccessListener(listener::onSuccess);
            task.addOnFailureListener(listener::onFailure);
        }
    }
}
