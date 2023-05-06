package com.example.myapplication;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;

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

    public SongList(){}

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
        saveSongList();
    }

    public void loadData(){
        try{
            ObjectMapper mapper = new ObjectMapper();

            JsonNode node = mapper.readTree(this.songFile);
            List<LinkedHashMap<String, Object>> songList = mapper.convertValue(node.get("songList"), ArrayList.class);
            ArrayList<Song> songs = new ArrayList<>();
            for (LinkedHashMap<String, Object> songMap : songList) {
                Song song = mapper.readValue(mapper.writeValueAsString(songMap), Song.class);
                songs.add(song);
            }
            this.setList(songs);
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

    //TODO: implement changing of unlock status of a specific song
}
