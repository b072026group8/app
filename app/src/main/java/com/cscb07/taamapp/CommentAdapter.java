package com.cscb07.taamapp;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.CommentViewHolder> {
    private List<Comment> commentList;
    public CommentAdapter(List<Comment> commentList) {
        this.commentList = commentList;
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
        holder.commentName.setText(comment.getName());
        holder.commentText.setText(comment.getText());
    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public static class CommentViewHolder extends RecyclerView.ViewHolder {
        TextView commentName, commentText;
        public CommentViewHolder(@NonNull View itemView) {
            super(itemView);
            this.commentName = itemView.findViewById(R.id.commentName);
            this.commentText = itemView.findViewById(R.id.commentText);
        }
    }
}
