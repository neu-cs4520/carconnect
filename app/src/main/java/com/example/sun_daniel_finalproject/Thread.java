package com.example.sun_daniel_finalproject;

import java.util.ArrayList;
import java.util.List;

public class Thread {

    private String threadId;
    private String title;
    private String carMake;
    private String content;
    private String authorId;
    private int likes;

    // default constructor for firestore
    public Thread() {}

    public Thread(String title, String content, String authorId, String carMake) {
        this.threadId = threadId;
        this.title = title;
        this.content = content;
        this.authorId = authorId;
        this.carMake = carMake;
        this.likes = 0;
    }

    public String getThreadId() {
        return threadId;
    }

    public void setThreadId(String threadId) {
        this.threadId = threadId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getCarMake() {
        return carMake;
    }

    public void setCarMake(String carMake) {
        this.carMake = carMake;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getLikes() {
        return likes;
    }

    public void setLikes(int likes) {
        this.likes = likes;
    }

    public void addLike() {
        this.likes++;
    }

    public String getAuthor() {
        return this.authorId;
    }
}
