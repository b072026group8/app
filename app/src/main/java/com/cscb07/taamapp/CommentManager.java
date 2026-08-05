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

public class CommentManager {
    private final String TAG = "Comment Manager";
    private final FirebaseDatabase db = FirebaseDatabase.getInstance();
    private String lotNum;
    private String userUid;
    private String accountType;
    private Context context;
    private List<Comment> commentList;

    public CommentManager(String lotNum, String userUid, String accountType, Context context, List<Comment> commentList) {
        this.lotNum = lotNum;
        this.userUid = userUid;
        this.accountType = accountType;
        this.context = context;
        this.commentList = commentList;
    }
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

    public void deleteComment(Comment comment) {

    }

    public String getUserUid() {
        return this.userUid;
    }

    public String getAccountType() {
        return this.accountType;
    }
}
