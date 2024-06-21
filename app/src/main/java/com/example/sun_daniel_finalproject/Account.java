package com.example.sun_daniel_finalproject;

public class Account {

    private String userId;
    private String username;

    // default constructor for firestore
    public Account() {}

    public Account(String userId, String username) {
        this.userId = userId;
        this.username = username;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
