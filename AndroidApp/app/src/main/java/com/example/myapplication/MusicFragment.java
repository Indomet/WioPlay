package com.example.myapplication;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;


public class MusicFragment extends Fragment implements BrokerConnection.MessageListener{


    private View rootView;
    private SongList songList;
    private SongLibraryAdapter adapter;
    private EditText searchSongs;

    MusicFragment(){
        BrokerConnection broker = BrokerConnection.getInstance();
        broker.addMessageListener(this);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.fragment_music, container, false);

        RecyclerView recyclerView = rootView.findViewById(R.id.songLibraryView);
        TextView userBalance = rootView.findViewById(R.id.user_balance);
        searchSongs= rootView.findViewById(R.id.searchSongs);
        searchSongs.setOnClickListener(v -> orderTheListOfSongs());



        userBalance.setText(Integer.toString(User.getInstance().getCalorieCredit()));
        //The adapter that handles the recycler view functionalities
        adapter = new SongLibraryAdapter(recyclerView.getContext());

        adapter.setSongsList(SongList.getInstance().getSongList());
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(
                new LinearLayoutManager(recyclerView.getContext(),
                    LinearLayoutManager.VERTICAL,
                    false));
        //Linearly displays a single line of items vertically
        BrokerConnection.getInstance().addMessageListener(adapter);

        songList=SongList.getInstance();


        return rootView;
    }

    @Override
    public void onMessageArrived(String payload) {
        Log.d("onMusicPayloadArrived", payload);
        ArrayList<Song> parsedSongs = new ArrayList<>();
        ObjectMapper mapper = new ObjectMapper();
        JsonNode songs;
        try {
            songs = mapper.readTree(payload);
        } catch (IOException e) {
            Log.d("error", "unable to read payload");
            throw new RuntimeException(e);
        }
        songs.elements().forEachRemaining(node -> { //Iterates over each song in the payload containing a list of song objects
            parsedSongs.add(parseSong(node, mapper));
        });
        adapter.setSongsList(sortList(parsedSongs));
        //adapter.setSongsList(parsedSongs);
        adapter.setSongsList(parsedSongs);
        SongList.getInstance().setUnlockedSongList(new ArrayList<>()); //Make sure that the unlocked list does not have duplicates.
    }


        @Override
        public String getSubbedTopic() {
            final String SONG_TOPIC = "Send/SongList";
            return SONG_TOPIC;

    }

    private Song parseSong(JsonNode node, ObjectMapper mapper){
        String title = node.get("title").asText();
        String artist = node.get("artist").asText();
        String imageURL = node.get("imageURL").asText();
        int cost = node.get("cost").asInt();
        double tempo = node.get("tempo").asLong();
        int[] notes = mapper.convertValue(node.get("notes"), new TypeReference<int[]>(){});
        int numOfNotes = notes.length;
        int durationInMinute = (int) Math.round(numOfNotes/tempo);
        int durationInSeconds = durationInMinute * 60 / 6; //divide by 6 since every 6 notes in the array corresponds to about 1 second.

        return new Song(title, artist, durationInSeconds, cost, imageURL, false, notes, tempo);
    }
    public ArrayList<Song> sortList(ArrayList<Song> list){
        Collections.sort(list, new Comparator<Song>() {
            @Override
            public int compare(Song o1, Song o2) {

                return o1.getTitle().toLowerCase().compareTo(o2.getTitle().toLowerCase());
            }
        });
        return list;
    }

    public void orderTheListOfSongs(){

        if(searchSongs.getText().toString().equals("Search")){
            searchSongs.setText("");
        }

        if(!searchSongs.getText().toString().isEmpty()){

            String target= searchSongs.getText().toString().toLowerCase();
            sortList(SongList.getInstance().getSongList());
            ArrayList<Song> list = SongList.getInstance().getSongList();
            Collections.sort(list, new Util.KeywordComparator(target));
            SongList.getInstance().setList(list);

            adapter.setSongsList(list);

        } else searchSongs.setText("");
    }





}