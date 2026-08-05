package com.cscb07.taamapp;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.cscb07.taamapp.auth.AccountType;

import java.util.List;

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.CommentViewHolder> {
    private List<Comment> commentList;
    private CommentManager commentManager;
    public CommentAdapter(List<Comment> commentList, CommentManager commentManager) {
        this.commentList = commentList;
        this.commentManager = commentManager;
    }

    @NonNull
    @Override
    public CommentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_comment_adapter, parent, false);
        return new CommentViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CommentViewHolder holder, int position) {
        Comment comment = this.commentList.get(position);
        holder.deleteButton.setVisibility(View.GONE);
        if (comment.getUserUid().equals(this.commentManager.getUserUid()) || AccountType.ADMIN.equals(this.commentManager.getAccountType())) {
            holder.deleteButton.setVisibility(View.VISIBLE);
            holder.deleteButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    commentManager.deleteComment(comment);
                }
            });
        }
        holder.commentName.setText(comment.getName());
        holder.commentText.setText(comment.getText());
    }

    @Override
    public int getItemCount() {
        return commentList.size();
    }

    public static class CommentViewHolder extends RecyclerView.ViewHolder {
        TextView commentName, commentText;
        ImageButton deleteButton;
        public CommentViewHolder(@NonNull View itemView) {
            super(itemView);
            this.commentName = itemView.findViewById(R.id.commentName);
            this.commentText = itemView.findViewById(R.id.commentText);
            this.deleteButton = itemView.findViewById(R.id.deleteButton);
        }
    }
}
