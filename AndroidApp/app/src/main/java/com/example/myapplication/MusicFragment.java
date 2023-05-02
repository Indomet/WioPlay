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

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;

import java.io.IOException;
import java.util.ArrayList;


public class MusicFragment extends Fragment implements BrokerConnection.MessageListener {


    private RecyclerView recyclerView;
    private TextView userBalance;
    private View rootView;
    private ArrayList<Song> songsList = new ArrayList<>();

    private boolean hasCreated = false;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.fragment_music, container, false);
        recyclerView = rootView.findViewById(R.id.songLibraryView);
        userBalance = rootView.findViewById(R.id.user_balance);

        MainActivity.brokerConnection.setMessageListener(this);

        userBalance.setText(Integer.toString(MainActivity.user.getCalorieCredit()));

        if(!hasCreated) {
            //The song list database
            //TODO: replace with reading data from music list file
            songsList.add(new Song("Song 1", 185, 200, "", true));
            songsList.add(new Song("Song 2", 200, 250, "", true));
            songsList.add(new Song("Song 3", 170, 300, "", false));
            songsList.add(new Song("Song 4", 180, 200, "", false));
            songsList.add(new Song("Song 5", 200, 250, "", false));
            songsList.add(new Song("Song 6", 173, 300, "", false));
            hasCreated = true;
        }

        SongLibraryAdapter adapter = new SongLibraryAdapter(recyclerView.getContext());
        adapter.setSongsList(songsList);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(recyclerView.getContext(), LinearLayoutManager.VERTICAL, false));
        //Linear displays a single line of items vertically


        return rootView;
    }

    @Override
    public void onMessageArrived(String payload) {
        ObjectMapper mapper = new ObjectMapper();
        try {

            /*
            * {"title":"Unholy (feat. Kim Petras)","artist":"Sam Smith","imageURL":"https://i.scdn.co/image/ab67616d0000b273a935e4689f15953311772cc4","tempo":131.121,"cost":200}
            * */
            ArrayList<Song> parsedSongs = new ArrayList<>();
            JsonNode songs = mapper.readTree(payload);

            songs.elements().forEachRemaining(node -> {

                String title = node.get("title").asText();
                String artist = node.get("artist").asText();
                String imageURL = node.get("imageURL").asText();
                double tempo = node.get("tempo").asLong();
                int cost = node.get("cost").asInt();
                Song song = new Song(title, 0, cost, imageURL, false);
                parsedSongs.add(song);

            });


            SongLibraryAdapter adapter = new SongLibraryAdapter(recyclerView.getContext());
            adapter.setSongsList(parsedSongs);
            recyclerView.setAdapter(adapter);
            recyclerView.setLayoutManager(new LinearLayoutManager(recyclerView.getContext(), LinearLayoutManager.VERTICAL, false));

            Log.d("songs", parsedSongs.toString());
//            mapper.readValue(payload, Song.class);
        } catch (Exception e) {
            Log.d("songs",e.getMessage());
        }
    }

}