package com.cscb07.taamapp;

import androidx.annotation.Nullable;

/**
 * Represents a comment made by a user on an artifact.
 * Stores the comment ID, the author's user ID, display name, and comment text.
 */
public class Comment {
    private String id;
    private String userUid;
    private String name;
    private String text;

    /**
     * Creates an empty comment.
     */
    public Comment() {
        this.id = "";
        this.userUid = "";
        this.name = "";
        this.text = "";
    }

    /**
     * Creates a comment with the specified author and content.
     *
     * @param userUid the unique ID of the user
     * @param name the display name of the user
     * @param text the comment text
     */
    public Comment(String userUid, String name, String text) {
        this.id = "";
        this.userUid = userUid;
        this.name = name;
        this.text = text;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setUserUid(String userUid) {
        this.userUid = userUid;
    }

    public String getUserUid() {
        return this.userUid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    /**
     * Returns the hash code for this comment based on its ID.
     *
     * @return the hash code of the comment ID
     */
    @Override
    public int hashCode() {
        return getId().hashCode();
    }

    /**
     * Compares this comment with another object for equality.
     * Two comments are considered equal if they have the same ID.
     *
     * @param obj the object to compare
     * @return true if the comments have the same ID, false otherwise
     */
    @Override
    public boolean equals(@Nullable Object obj) {
        if (obj instanceof Comment) {
            return getId().equals(((Comment) obj).getId());
        }
        return false;
    }
}
