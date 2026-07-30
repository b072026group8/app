package com.cscb07.taamapp;

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

    public Comment(String id, String userUid, String name, String text) {
        this.id = id;
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
}
