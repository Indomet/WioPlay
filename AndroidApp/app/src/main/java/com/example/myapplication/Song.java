package com.example.myapplication;

import com.fasterxml.jackson.databind.JsonNode;

import java.util.Iterator;

public class Song {
    private String title;
    private String artist;
    private int duration; //in seconds
    private int price; //price in Calorie credits
    private String imageURL;
    private boolean isUnlocked; //false by default, except the starting songs
    private double tempo;
    private int[] notes;

    private static int currentChunkIdx = 0; // every song starts from the beginning and is not dependent on objects

    //Needs dummy constructor
    public Song(){} //Caused by: com.fasterxml.jackson.databind.JsonMappingException: No suitable constructor found for type [simple type, class com.example.myapplication.Song]: can not instantiate from JSON object (need to add/enable type information?)

    public Song(String title, String artist, int price, String imageURL, boolean isUnlocked, int[] notes, double tempo) {
        this.title = title;
        this.artist = artist;
        this.price = price;
        this.imageURL = imageURL;
        this.isUnlocked = isUnlocked;
        this.notes = notes;
        this.tempo = tempo;
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

    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public int[] getNotes() {
        return notes;
    }

    public void setNotes(int[] notes) {
        this.notes = notes;
    }

    public double getTempo() {
        return tempo;
    }

    public void setTempo(double tempo) {
        this.tempo = tempo;
    }

    public int getCurrentChunkIdx() { return currentChunkIdx;}

    public void resetCurrentChunkIdx() { currentChunkIdx = 0;}

    public void incrementCurrentChunkIdx() { currentChunkIdx++;}

    public int calculateChunkDuration(int numOfNotes)
    {
        return duration = (int) Math.round(numOfNotes/tempo) * 60/ 6; //divide by 6 since every 6 notes in the array corresponds to about 1 second.
    }
}
