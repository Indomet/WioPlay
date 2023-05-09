package com.example.myapplication;

import com.fasterxml.jackson.databind.JsonNode;

import java.util.Iterator;

public class Song {
    private String title;
    private int duration; //in seconds
    private int price; //price in Calorie credits
    private String imageURL;
    private boolean isUnlocked; //false by default, except the starting songs

    //Needs dummy constructor
    public Song(){} //Caused by: com.fasterxml.jackson.databind.JsonMappingException: No suitable constructor found for type [simple type, class com.example.myapplication.Song]: can not instantiate from JSON object (need to add/enable type information?)

    public Song(String title, int duration, int price, String imageURL, boolean isUnlocked) {
        this.title = title;
        this.duration = duration;
        this.price = price;
        this.imageURL = imageURL;
        this.isUnlocked = isUnlocked;

        // Ideas of approaches to present to group 10th May:

        // 1.
        // Add 'String artist' to constructor which allows us to find
        // artistPackage and then title.txt to retrieve notes, using multikey-quicksort or binary search.
        // These notes are then parsed using Parser.parse().
        // Lastly, we retrieve the parsed string and publish it in 'BrokerConnection'
        // (If approved: Get help with making sure the imports of packages are correct due to auto-correction-issues)

        // 2.
        // Add 'parsedNotes' to constructor
        // 'parsedNotes' contains a string already parsed in 'NotesParser'
        // Pass this string in the publish-message in 'BrokerConnection'

        // Note: The simple solution is to add an additional attribute in the constructor 'parsedNotes' containing
        // a string of already parsed notes in 'NoteParser'
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

    //TODO
    /*
    public void temp(){
        Iterator<JsonNode> it = node.get("song").elements();

        for (; it.hasNext(); it.next()) {
        }

        while(it.hasNext()){
            //save to object
            it.next();
        }
    }
    }

     */
}
