package com.cscb07.taamapp;

import androidx.annotation.Nullable;

public class Comment {
    private String id;
    private String userUid;
    private String name;
    private String text;

    public Comment() {
        this.id = "";
        this.userUid = "";
        this.name = "";
        this.text = "";
    }

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

    @Override
    public int hashCode() {
        return getId().hashCode();
    }

    @Override
    public boolean equals(@Nullable Object obj) {
        if (obj instanceof Comment) {
            return getId().equals(((Comment) obj).getId());
        }
        return false;
    }
}
