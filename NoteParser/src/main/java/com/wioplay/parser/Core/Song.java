package com.wioplay.parser.Core;

import java.util.Objects;

public class Song {

    private final String title;
    private final String imageURL;
    private final double tempo;
    private final int cost;

    public Song(String title, String imageURL, int tempo, int cost) {
        this.title = title;
        this.imageURL = imageURL;
        this.tempo = tempo;
        this.cost = cost;
    }

    public int getNoteCount() {
        // TODO
        throw new UnsupportedOperationException();
    }

    public String getTitle() {
        return this.title;
    }

    public String getImageURL() {
        return this.imageURL;
    }

    public int getCost() {
        return this.cost;
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
                "name='" + title + '\'' +
                ", imageURL='" + imageURL + '\'' +
                ", cost=" + cost +
                '}';
    }

}
