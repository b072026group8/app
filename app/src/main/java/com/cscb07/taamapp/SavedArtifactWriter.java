package com.cscb07.taamapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import com.cscb07.taamapp.util.OperationListener;

/**
 * Class to add or remove artifacts from a user's saved artifact list.
 */
public class SavedArtifactWriter {
    public final String DB_PATH = "saved_collection/";
    private final DatabaseReference collectionRoot;

    public SavedArtifactWriter() {
        collectionRoot = FirebaseDatabase.getInstance().getReference(DB_PATH);
    }

    public void addSavedArtifact(@NonNull String userId, @NonNull String artifactLot) {
        addSavedArtifact(userId, artifactLot, null);
    }
    public void addSavedArtifact(@NonNull String userId, @NonNull String artifactLot, @Nullable OperationListener<Void> listener) {
        Task<Void> task = collectionRoot.child(userId).child(artifactLot).setValue(true);
        if (listener != null) {
            task.addOnSuccessListener(listener::onSuccess);
            task.addOnFailureListener(listener::onFailure);
        }
    }

    public void removeSavedArtifact(@NonNull String userId, @NonNull String artifactLot) {
        removeSavedArtifact(userId, artifactLot, null);
    }
    public void removeSavedArtifact(@NonNull String userId, @NonNull String artifactLot, @Nullable OperationListener<Void> listener) {
        Task<Void> task = collectionRoot.child(userId).child(artifactLot).removeValue();
        if (listener != null) {
            task.addOnSuccessListener(listener::onSuccess);
            task.addOnFailureListener(listener::onFailure);
        }
    }
}
