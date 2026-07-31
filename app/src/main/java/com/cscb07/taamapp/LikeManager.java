package com.cscb07.taamapp;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

public class LikeManager {
    private final DatabaseReference db;

    public LikeManager() {
        this.db = FirebaseDatabase.getInstance().getReference();
    }

    /*
    Like Counts for artifacts
     */
    public void likeCountUpdater(String lotNumber, ValueEventListener listener) {
        db.child("artifacts").child(lotNumber).child("likeCount").addValueEventListener(listener);
    }

    /*
    Users liked artifacts
     */
    public void checkUserLikes(String uid, String lotNumber, ValueEventListener listener) {
        db.child("likedArtifacts").child(uid).child(lotNumber).addListenerForSingleValueEvent(listener);
    }

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
