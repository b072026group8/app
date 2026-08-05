package com.cscb07.taamapp;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

/**
 * Manages liking and unliking artifacts in Firebase.
 * Handles like counts and tracks which artifacts users have liked.
 */
public class LikeManager {
    private final DatabaseReference db;

    /**
     * Creates a LikeManager using the Firebase Realtime Database.
     */
    public LikeManager() {
        this.db = FirebaseDatabase.getInstance().getReference();
    }

    /**
     * Registers a listener that receives updates whenever an
     * artifact's like count changes.
     *
     * @param lotNumber the artifact lot number
     * @param listener the listener that receives like count updates
     */
    public void likeCountUpdater(String lotNumber, ValueEventListener listener) {
        db.child("artifacts").child(lotNumber).child("likeCount").addValueEventListener(listener);
    }

    /**
     * Checks whether a user has liked a specific artifact.
     *
     * @param uid the user's unique ID
     * @param lotNumber the artifact lot number
     * @param listener the listener that receives the result
     */
    public void checkUserLikes(String uid, String lotNumber, ValueEventListener listener) {
        db.child("likedArtifacts").child(uid).child(lotNumber).addListenerForSingleValueEvent(listener);
    }

    /**
     * Adds or removes a user's like for an artifact and updates
     * the artifact's total like count.
     *
     * @param uid the user's unique ID
     * @param lotNumber the artifact lot number
     * @param isLiked true to like the artifact, false to unlike it
     */
    public void toggleLike(String uid, String lotNumber, boolean isLiked) {
        // Uses Atomic server-side increments for tracking like count for artifacts
        Map<String, Object> updates = new HashMap<>();

        if (isLiked) {
            updates.put("likedArtifacts/" + uid + "/" + lotNumber, true);
            updates.put("artifacts/" + lotNumber + "/likeCount", ServerValue.increment(1));
        } else {
            // null removes field from database instead of storing it as false
            updates.put("likedArtifacts/" + uid + "/" + lotNumber, null);
            updates.put("artifacts/" + lotNumber + "/likeCount", ServerValue.increment(-1));
        }

        db.updateChildren(updates);
    }
}
