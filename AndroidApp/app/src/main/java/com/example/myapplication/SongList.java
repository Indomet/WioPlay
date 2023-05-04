package com.example.myapplication;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class SongList {
    private ArrayList<Song> songList;

    private int id = 1;
    private File songFile;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
        saveSongList();
    }


    public SongList(File songFile){
        this.songFile = songFile;
        defaultList();
        loadData();

    }

    private void defaultList(){
        this.songList = new ArrayList<>();
    }

    public ArrayList<Song> getSongList(){
        return this.songList;
    }

    public File getSongFile() {
        return songFile;
    }

    public void setList(ArrayList<Song> songList) {
        this.songList = songList;
        saveSongList();
    }

    public void add(Song song){
        this.songList.add(song);
    }

    private void loadData(){
        try {
            ObjectMapper mapper = new ObjectMapper();
/*
            ArrayList<Song> jsonObject = mapper.readValue(
                    this.songFile,
                    new TypeReference<ArrayList<Song>>(){}
            );
            this.setList(jsonObject);

 */

            JsonNode node = mapper.readTree(this.songFile);
            this.setId(node.get("id").asInt());
            this.setList(mapper.convertValue(node.get("songList"), ArrayList.class));



        } catch (IOException e) {
            saveSongList(); //TODO PROBLEM: OVERRIDES TO NULL
            loadData();
        }
    }

    public void saveSongList() {
        ObjectMapper mapper = new ObjectMapper();
        ObjectWriter writer = mapper.writer();
        JsonNode node = mapper.valueToTree(this);
        try {
            writer.writeValue(this.songFile, node);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    //TODO: implement changing of unlock status of a specific song
}
