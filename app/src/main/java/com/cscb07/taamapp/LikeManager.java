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

    public void likeCountUpdater(String lotNumber, ValueEventListener listener) {
        db.child("artifacts").child(lotNumber).child("likeCount").addValueEventListener(listener);
    }

    public void checkUserLikes(String uid, String lotNumber, ValueEventListener listener) {
        db.child("users").child(uid).child("likedArtifacts").child(lotNumber).addListenerForSingleValueEvent(listener);
    }

    public void toggleLike(String uid, String lotNumber, boolean isLiked) {
        Map<String, Object> updates = new HashMap<>();

        if (isLiked) {
            updates.put("users/" + uid + "/likedArtifacts/" + lotNumber, true);
            updates.put("artifacts/" + lotNumber + "/likeCount", ServerValue.increment(1));
        } else {
            updates.put("users/" + uid + "/likedArtifacts/" + lotNumber, null);
            updates.put("artifacts/" + lotNumber + "/likeCount", ServerValue.increment(-1));
        }

        db.updateChildren(updates);
    }
}
