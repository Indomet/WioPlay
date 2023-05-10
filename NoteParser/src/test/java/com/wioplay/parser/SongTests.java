package com.wioplay.parser;

import com.wioplay.parser.Core.Song;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class SongTests {

    @Test
    public void shouldDisplayCorrectly() {

        Song song = new Song(
                "MEGALOVANIA",
                "Toby Fox",
                "https://i.scdn.co/image/ab67616d0000b27324edb22d068eb245a924b7f2",
                119.987,
                200
        );

        assertEquals("MEGALOVANIA", song.getTitle());
        assertEquals("https://i.scdn.co/image/ab67616d0000b27324edb22d068eb245a924b7f2", song.getImageURL());
        assertEquals(200, song.getCost());
        assertEquals("Song{title='MEGALOVANIA', artist='Toby Fox', imageURL='https://i.scdn.co/image/ab67616d0000b27324edb22d068eb245a924b7f2', tempo=119.987, cost=200}", song.toString());

    }

}