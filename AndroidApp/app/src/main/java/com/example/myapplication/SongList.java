package com.example.myapplication;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class SongList {
    private ArrayList<Song> songList;
    private File songFile;

    private static SongList instance;

    private SongList(){} //Jackson library requires a default empty constructor

    private SongList(File songFile){
        this.songFile = songFile;
        defaultList();
        loadData();

    }
    public static SongList getInstance(){
        if(instance == null){
            throw new NullPointerException();
        }
        return  instance;
    }

    public static SongList initialize(File songFile){
        if(instance == null){
            instance = new SongList(songFile);
        }
        return instance;
    }


    private void defaultList(){
        this.songList = new ArrayList<>();

    }

    public ArrayList<Song> getSongList(){
        return this.songList;
    }


    public void setList(ArrayList<Song> songList) {
        this.songList = songList;
        saveSongList();
    }

    public void loadData(){
        try{
            ObjectMapper mapper = new ObjectMapper();
            JsonNode node = mapper.readTree(this.songFile);

            //Must specify the deserialization class type
            ArrayList<Song> loadedList = mapper.convertValue(node.get("songList"), new TypeReference<ArrayList<Song>>(){});
            this.setList(loadedList);
        }catch (IOException e){
            saveSongList();
            loadData();
        }


    }

    public void saveSongList() {
        ObjectMapper mapper = new ObjectMapper();
        ObjectWriter writer = mapper.writer().withDefaultPrettyPrinter();
        JsonNode node = mapper.valueToTree(this);
        try {
            writer.writeValue(this.songFile, node);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void unlockSong(Song song){
        this.songList.get(this.songList.indexOf(song)).setUnlocked(true);
        saveSongList();
    }

}
