package com.example.sun_daniel_finalproject;

public class Reply {
    private String content;

    public Reply() {
        // Default constructor required for calls to DataSnapshot.getValue(Reply.class)
    }

    public Reply(String content) {
        this.content = content;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}