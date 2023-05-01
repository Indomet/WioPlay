package com.wioplay.parser.Utils;

import com.wioplay.parser.Core.Song;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

public class Reader {

    public static final String SONG_PATH = "src/main/resources/Songs";

    private static Song loadFile(Path path) {

        try {

            List<String> lines = Files.readAllLines(path);
            return Reader.parseToSong(new ArrayList<>(lines));

        } catch (IOException e) {
            System.out.println(e.getMessage());
        }

        return null;

    }

    public static ArrayList<Song> loadFiles() {

        ArrayList<Song> songs = new ArrayList<>();

        //try with resources - ensures that the stream is closed regardless of the statement
        try(Stream<Path> stream = Files.walk(Paths.get(SONG_PATH))) {

            stream
                    .filter(Files::isRegularFile)
                    .forEach(file -> songs.add(Reader.loadFile(file)));

        } catch (IOException e) {
            System.out.println(e.getMessage());
        }

        return songs;

    }

    private static Song parseToSong(ArrayList<String> lines) {

        String title = lines.get(0).split(": ")[1];
        String artist = lines.get(1).split(": ")[1];
        String imageURL = lines.get(2).split(": ")[1];
        double tempo = Double.parseDouble(lines.get(3).split(": ")[1]);
        List<String> noteChunks =  lines.subList(6, lines.size());
        String notes = String.join("\n", noteChunks);

        return new Song(title, artist, imageURL, tempo, 200, notes);

    }



}
