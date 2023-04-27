package com.wioplay.parser.Core;

import java.util.Objects;

public class Song {

    private final String name;
    private final String imageURL;
    private final int cost;

    public Song(String name, String imageURL, int cost) {
        this.name = name;
        this.imageURL = imageURL;
        this.cost = cost;
    }

    public int getNoteCount() {
        // TODO
        throw new UnsupportedOperationException();
    }

    public String getName() {
        return this.name;
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
        return cost == song.cost && Objects.equals(name, song.name) && Objects.equals(imageURL, song.imageURL);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, imageURL, cost);
    }

    @Override
    public String toString() {
        return "Song{" +
                "name='" + name + '\'' +
                ", imageURL='" + imageURL + '\'' +
                ", cost=" + cost +
                '}';
    }

}
