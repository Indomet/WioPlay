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
import android.widget.Toast;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;


public class MusicFragment extends Fragment implements BrokerConnection.MessageListener{


    private View rootView;
    private SongList songList;
    private SongLibraryAdapter adapter;
    private EditText searchSongs;

    public static Song currentSong;
    public static ArrayList<int[]> notes = new ArrayList<>();

    MusicFragment(){
        BrokerConnection broker= MainActivity.brokerConnection;
        broker.addMessageListener(this);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.fragment_music, container, false);

        RecyclerView recyclerView = rootView.findViewById(R.id.songLibraryView);
        TextView userBalance = rootView.findViewById(R.id.user_balance);
        searchSongs=rootView.findViewById(R.id.searchSongs);
        searchSongs.setOnClickListener(v -> searchRelatedSongs());

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

        songList=SongList.getInstance();
        if(songList.getSongList()!=null){
            sortList(songList.getSongList());
        }



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
        sortList(parsedSongs);
        adapter.setSongsList(parsedSongs);
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
        Collections.sort(list, Comparator.comparing(Song::getTitle));
        return list;

    }
    public  int findBestMatchIndex(List<Song> sortedList, String targetString) {
        int low = 0;
        int high = sortedList.size() - 1;

        while (low <= high) {
            int mid = (low + high) / 2;
            int comparison = sortedList.get(mid).getTitle().compareTo(targetString);

            if (comparison == 0) {
                return mid; // Found an exact match
            } else if (comparison < 0) {
                low = mid + 1;
            } else {
                high = mid - 1;
            }
        }

        return high; // No exact match found, return the index for the best match
    }


    public ArrayList<Song> searchResult(List<Song> sortedList, String target){

        int mid = findBestMatchIndex(sortedList,target);

        ArrayList<Song> myList=new ArrayList<>();

        int counter=0;

        while ( mid >= 0 && sortedList.get(mid).getTitle().charAt(0)==target.charAt(0)){
            myList.add(sortedList.get(mid));
            mid--;
            counter++;

        }

        mid=mid+counter+1;

        while (mid<(sortedList.size()) && sortedList.get(mid).getTitle().charAt(0)==target.charAt(0)){
            myList.add(sortedList.get(mid));
            mid++;
        }
        //checking just in case the find best match was somewhat wrong.

        for (Song songs: sortedList) {
            if(!myList.contains(songs) && songs.getTitle().charAt(0)!=target.charAt(0)){
                myList.add(songs);
            }
        }

        return myList;

    }
    public void searchRelatedSongs(){
        if(searchSongs.getText().toString()!=null){
            String target= searchSongs.getText().toString();
            songList.setList(searchResult(songList.getSongList(),target));
        }



    }







}