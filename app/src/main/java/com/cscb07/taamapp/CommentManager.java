package com.cscb07.taamapp;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

/**
 * Manages loading, adding, and deleting comments for a specific artifact.
 * Uses Firebase Realtime Database to store and retrieve comment data.
 */
public class CommentManager {
    private final String TAG = "Comment Manager";
    private final FirebaseDatabase db = FirebaseDatabase.getInstance();
    private String lotNum;
    private String userUid;
    private String accountType;
    private Context context;
    private List<Comment> commentList;

    /**
     * Creates a manager for comments belonging to a specific artifact.
     *
     * @param lotNum the artifact lot number
     * @param userUid the current user's unique ID
     * @param accountType the current user's account type
     * @param context the context used to display messages
     * @param commentList the list that stores loaded comments
     */
    public CommentManager(String lotNum, String userUid, String accountType, Context context, List<Comment> commentList) {
        this.lotNum = lotNum;
        this.userUid = userUid;
        this.accountType = accountType;
        this.context = context;
        this.commentList = commentList;
    }

    /**
     * Loads comments for the artifact from Firebase and updates the adapter.
     * The comment list is refreshed whenever the database data changes.
     *
     * @param adapter the adapter displaying the comments
     */
    public void loadComments(CommentAdapter adapter) {
        DatabaseReference ref = db.getReference("artifacts/" + lotNum + "/comments");
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                commentList.clear();
                for (DataSnapshot curr : snapshot.getChildren()) {
                    Comment comment = curr.getValue(Comment.class);
                    commentList.add(comment);
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e(TAG, "Error fetching comments for artifact " + lotNum);
            }
        });
    }

    /**
     * Adds a new comment to the artifact's comment collection in Firebase.
     * A unique database ID is generated and assigned to the comment.
     *
     * @param comment the comment to add
     */
    public void addComment(Comment comment) {
        DatabaseReference ref = db.getReference("artifacts/" + lotNum + "/comments");
        String id = ref.push().getKey();
        if (id != null) {
            comment.setId(id);
            ref.child(id).setValue(comment).addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    Toast.makeText(context, "Comment added successfully.", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(context, "Failed to add comment.", Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            Log.e(TAG, "DB returned null id for adding comment");
            Toast.makeText(context, "Failed to add comment.", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Deletes a comment from the artifact's comment collection in Firebase.
     *
     * @param comment the comment to delete
     */
    public void deleteComment(Comment comment) {
        db.getReference("artifacts").child(lotNum).child("comments").child(comment.getId()).removeValue()
                .addOnSuccessListener(unused -> {
                    Toast.makeText(context, "Comment deleted.", Toast.LENGTH_SHORT).show();
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(context, "Failed to delete comment.", Toast.LENGTH_SHORT).show();
                });
    }

    /**
     * Returns the unique ID of the current user.
     *
     * @return the current user's unique ID
     */
    public String getUserUid() {
        return this.userUid;
    }

    /**
     * Returns the current user's account type.
     *
     * @return the current user's account type
     */
    public String getAccountType() {
        return this.accountType;
    }
}
