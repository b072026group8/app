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

/**
 * RecyclerView adapter for displaying comments associated with an artifact.
 * Handles displaying comment information and deleting comments when permitted.
 */
public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.CommentViewHolder> {
    private List<Comment> commentList;
    private CommentManager commentManager;

    /**
     * Creates a comment adapter.
     *
     * @param commentList the list of comments to display
     * @param commentManager the manager responsible for comment operations
     */
    public CommentAdapter(List<Comment> commentList, CommentManager commentManager) {
        this.commentList = commentList;
        this.commentManager = commentManager;
    }

    /**
     * Creates a new ViewHolder for displaying a comment.
     *
     * @param parent the parent view group
     * @param viewType the type of view
     * @return a new CommentViewHolder
     */
    @NonNull
    @Override
    public CommentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_comment_adapter, parent, false);
        return new CommentViewHolder(view);
    }

    /**
     * Binds comment data to a ViewHolder.
     * Displays the delete button only when the current user is allowed
     * to delete the comment.
     *
     * @param holder the ViewHolder to bind
     * @param position the position of the comment in the list
     */
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

    /**
     * Returns the number of comments currently displayed.
     *
     * @return the number of comments
     */
    @Override
    public int getItemCount() {
        return commentList.size();
    }

    /**
     * ViewHolder that stores references to the views used
     * to display a single comment.
     */
    public static class CommentViewHolder extends RecyclerView.ViewHolder {
        TextView commentName, commentText;
        ImageButton deleteButton;

        /**
         * Creates a ViewHolder for a comment item.
         *
         * @param itemView the view representing a single comment
         */
        public CommentViewHolder(@NonNull View itemView) {
            super(itemView);
            this.commentName = itemView.findViewById(R.id.commentName);
            this.commentText = itemView.findViewById(R.id.commentText);
            this.deleteButton = itemView.findViewById(R.id.deleteButton);
        }
    }
}
