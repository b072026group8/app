package com.cscb07.taamapp;

public class Comment {
    private String name;
    private String text;

    public Comment() {
        this.name = "";
        this.text = "";
    }

    public Comment(String name, String text) {
        this.name = name;
        this.text = text;
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
