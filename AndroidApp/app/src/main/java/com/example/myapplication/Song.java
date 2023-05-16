package com.example.myapplication;


public class Song {
    private String title;
    private String artist;
    private int duration; //in seconds
    private int price; //price in Calorie credits
    private String imageURL;
    private boolean isUnlocked; //false by default, except the starting songs
    private double tempo;
    private int[] notes;

    //Needs dummy constructor
    public Song(){} //Caused by: com.fasterxml.jackson.databind.JsonMappingException: No suitable constructor found for type [simple type, class com.example.myapplication.Song]: can not instantiate from JSON object (need to add/enable type information?)

    public Song(String title, String artist, int duration, int price, String imageURL, boolean isUnlocked, int[] notes, double tempo) {
        this.title = title;
        this.artist = artist;
        this.duration = duration;
        this.price = price;
        this.imageURL = imageURL;
        this.isUnlocked = isUnlocked;
        this.notes = notes;
        this.tempo = tempo;
    }

    public String getTitle() {
        return title;
    }

    public int getDuration() {

        return duration;
    }

    public int getPrice() {
        return price;
    }

    public String getImageURL() {
        return imageURL;
    }

    public boolean isUnlocked() {
        return isUnlocked;
    }

    public void setUnlocked(boolean unlocked) {
        isUnlocked = unlocked;
    }

    public String getArtist() {
        return artist;
    }

    public int[] getNotes() {
        return notes;
    }

    public double getTempo() { //Needed for jackson library
        return tempo;
    }

    public void setTempo(double tempo) {
        this.tempo = tempo;
    }
}
