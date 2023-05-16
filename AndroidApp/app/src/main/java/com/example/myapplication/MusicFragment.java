package com.example.myapplication;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;


public class MusicFragment extends Fragment implements BrokerConnection.MessageListener{


    private RecyclerView recyclerView;
    private TextView userBalance;
    private View rootView;

    private SongLibraryAdapter adapter;


    public static Song currentSong;
    public static ArrayList<int[]> notes = new ArrayList<>();

    private ArrayList<Song> songsList = new ArrayList<>();
    private boolean hasCreated = false;

    MusicFragment(){
        BrokerConnection broker= MainActivity.brokerConnection;
        broker.addMessageListener(this);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.fragment_music, container, false);
        recyclerView = rootView.findViewById(R.id.songLibraryView);
        userBalance = rootView.findViewById(R.id.user_balance);

        userBalance.setText(Integer.toString(MainActivity.user.getCalorieCredit()));
        adapter = new SongLibraryAdapter(recyclerView.getContext());
        adapter.setSongsList(SongList.getInstance().getSongList());
        recyclerView.setAdapter(adapter);
        BrokerConnection.getInstance(getContext()).addMessageListener(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(recyclerView.getContext(), LinearLayoutManager.VERTICAL, false));
        //Linearly displays a single line of items vertically

        return rootView;
    }

    @Override
    public void onMessageArrived(String payload) {

        Toast.makeText(rootView.getContext(), payload, Toast.LENGTH_SHORT).show();

        ArrayList<Song> parsedSongs = new ArrayList<>();
        ObjectMapper mapper = new ObjectMapper();
        JsonNode songs = null;
        try {
            songs = mapper.readTree(payload);
        } catch (IOException e) {
            Log.d("error", "unable to read payload");
            throw new RuntimeException(e);
        }
        songs.elements().forEachRemaining(node -> {

            String title = node.get("title").asText();
            String artist = node.get("artist").asText();
            String imageURL = node.get("imageURL").asText();
            int cost = node.get("cost").asInt();

            //TODO: include numOfNotes and tempo in payload, currently null
            double tempo = node.get("tempo").asLong();

            int[] notes = mapper.convertValue(node.get("notes"), new TypeReference<int[]>(){});
            int numOfNotes = notes.length;
            int duration = (int) Math.round(numOfNotes/tempo) * 60/ 6; //divide by 6 since every 6 notes in the array corresponds to about 1 second.

            Song song = new Song(title, artist, duration, cost, imageURL, false, notes, tempo);
            parsedSongs.add(song);
        });
        adapter.setSongsList(parsedSongs);
    }


        @Override
        public String getSubbedTopic() {
            String SONG_TOPIC = "songs";
            return SONG_TOPIC;

    }


}