package com.example.sun_daniel_finalproject;

public class Thread {

    private String title;
    private String carMake;
    private String content;
    private int likes;

    // default constructor for firestore
    public Thread() {}

    public Thread(String title, String carMake, String content, int likes) {
        this.title = title;
        this.carMake = carMake;
        this.content = content;
        this.likes = likes;
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
}
