package com.example.sun_daniel_finalproject;

public class Car {

    private String make;
    private String model;
    private String year;
    private String color;
    private String ownerId;

    // default constructor for firestore
    public Car() {}

    public Car(String make, String model, String year, String color, String ownerId) {
        this.make = make;
        this.model = model;
        this.year = year;
        this.color = color;
        this.ownerId = ownerId;
    }

    public String getMake() {
        return make;
    }

    public String getModel() {
        return model;
    }

    public String getYear() {
        return year;
    }

    public String getColor() { return color; }

    public String getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(String ownerId) {
        this.ownerId = ownerId;
    }
}
