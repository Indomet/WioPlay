package com.example.myapplication;

public class Song {
    private String title;
    private int duration; //in seconds
    private int price; //price in Calorie credits
    private String imageURL;
    private boolean isUnlocked; //false by default, except the starting songs

    public Song(String title, int duration, int price, String imageURL, boolean isUnlocked) {
        this.title = title;
        this.duration = duration;
        this.price = price;
        this.imageURL = imageURL;
        this.isUnlocked = isUnlocked;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    public boolean isUnlocked() {
        return isUnlocked;
    }

    public void setUnlocked(boolean unlocked) {
        isUnlocked = unlocked;
    }
}
