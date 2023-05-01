package com.wioplay.parser.Network.Subscriptions;

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
        this.getClient().publish("songs", songs.toString());

    }

}
