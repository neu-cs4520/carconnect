package com.example.sun_daniel_finalproject;

public class MarketItem {

    private String name;
    private String price;
    private String description;
    private String sellerName;
    private String sellerContact;

    // No-argument constructor required for Firestore deserialization
    public MarketItem() {}

    public MarketItem(String name, String price, String description, String sellerName, String sellerContact) {
        this.name = name;
        this.price = price;
        this.description = description;
        this.sellerName = sellerName;
        this.sellerContact = sellerContact;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getSellerName() {
        return sellerName;
    }

    public void setSellerName(String sellerName) {
        this.sellerName = sellerName;
    }

    public String getSellerContact() {
        return sellerContact;
    }

    public void setSellerContact(String sellerContact) {
        this.sellerContact = sellerContact;
    }
}
