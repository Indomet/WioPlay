package com.wioplay.parser.Core;

import java.util.Objects;

public class Song {

    private final String title;
    private final String artist;
    private final String imageURL;
    private final double tempo;
    private final int cost;
    private int[] notes;

    public Song(String title, String artist, String imageURL, double tempo, int cost) {
        this.title = title;
        this.artist = artist;
        this.imageURL = imageURL;
        this.tempo = tempo;
        this.cost = cost;
        this.notes = new int[0];
    }

    public Song(String title, String artist, String imageURL, double tempo, int cost, int[] notes) {
        this(title, artist, imageURL, tempo, cost);
        this.notes = notes;
    }

//    public int getNoteCount() {
//        // TODO
//        throw new UnsupportedOperationException();
//    }

    public String getTitle() {
        return this.title;
    }

    public String getImageURL() {
        return this.imageURL;
    }

    public int getCost() {
        return this.cost;
    }

    public String getArtist() {
        return artist;
    }

    public double getTempo() {
        return tempo;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Song song = (Song) o;
        return cost == song.cost && Objects.equals(title, song.title) && Objects.equals(imageURL, song.imageURL);
    }

    @Override
    public int hashCode() {
        return Objects.hash(title, imageURL, cost);
    }

    @Override
    public String toString() {
        return "Song{" +
                "title='" + title + '\'' +
                ", artist='" + artist + '\'' +
                ", imageURL='" + imageURL + '\'' +
                ", tempo=" + tempo +
                ", cost=" + cost +
                ", notes='" + notes + '\'' +
                '}';
    }

}
