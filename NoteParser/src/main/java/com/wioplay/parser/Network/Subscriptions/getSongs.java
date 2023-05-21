package com.wioplay.parser.Network.Subscriptions;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.wioplay.parser.Core.Song;
import com.wioplay.parser.Network.AbstractSubscription;
import com.wioplay.parser.Utils.Reader;

import java.util.ArrayList;

public class getSongs extends AbstractSubscription {

    public getSongs() {
        super("songs/all");
    }

    @Override
    public void messageArrived(String message) {

        System.out.println("User is requesting for all songs");
        ArrayList<Song> songs = Reader.loadFiles();
        ObjectMapper mapper = new ObjectMapper();

        try {

            String jsonString = mapper.writeValueAsString(songs);
            this.getClient().publish("Send/SongList", jsonString);

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

    }

}
