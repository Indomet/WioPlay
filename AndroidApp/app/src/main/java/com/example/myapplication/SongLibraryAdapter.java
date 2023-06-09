package com.example.myapplication;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.ArrayList;

public class SongLibraryAdapter extends RecyclerView.Adapter<SongLibraryAdapter.ViewHolder> implements BrokerConnection.MessageListener {

    private ArrayList<Song> songsList = new ArrayList<>();

    private Song currentSong;

    private ArrayList<int[]> noteChunks = new ArrayList<>();

    private final String NEXT_SONG_MESSAGE = "NEXT";
    private final String PREVIOUS_SONG_MESSAGE = "PREVIOUS";
    private final Context context;
    private boolean confirm;

    private static SongLibraryAdapter instance;

    private SongLibraryAdapter(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //ViewGroup is the parent of all layout types
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.music_item_list, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        //position is the index of the items in the recycler view
        int currentPosition = holder.getBindingAdapterPosition();

        Song currentSong = songsList.get(currentPosition); //Maps each song to its position in the list, each instance is its own card in the recycler view.

        Picasso.get().load(currentSong.getImageURL()).into(holder.songImage);//Loads song image from url
        holder.artistName.setText(currentSong.getArtist());
        holder.songTitle.setText(currentSong.getTitle());
        int duration = currentSong.getDuration();
        holder.songDuration.setText((duration / 60) + ":" + String.format("%02d", duration % 60));

        if(!currentSong.isUnlocked()){
            holder.songPrice.setText(Integer.toString(currentSong.getPrice())+" CC");
        }else{
            holder.songPrice.setText("Play");
        }

        holder.parent.setOnClickListener(view -> validateSong(currentSong));


    }

    public void validateSong(Song currentSong){
        this.currentSong = currentSong;
        if(currentSong.isUnlocked()){
            playSong(currentSong);
        }else{
            if (User.getInstance().getCalorieCredit() >= currentSong.getPrice()) { //if user has enough credits
                confirmationDialog(currentSong); //Calls the popup window
            } else {
                Toast.makeText(context, "Not enough calorie credits", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public int getItemCount() {
        int size;
        try {
            size = songsList.size();
        } catch (Exception e){
            size = 0;
        }
        return size;

    }

    public void setSongsList(ArrayList<Song> songsList){
        SongList.getInstance().setList(songsList);
        this.songsList = songsList;
        notifyDataSetChanged(); //Notify adapter whenever the song list has updated, so the up to date information can be shown
    }

    public void updateData(){
        TextView view =((MainActivity)context).findViewById(R.id.user_balance); //Updates balance display in MusicFragment,
        //context is cast to MainActivity in order to access findViewById()
        view.setText(Integer.toString(User.getInstance().getCalorieCredit()));
        notifyDataSetChanged();
    }

    private void unlockSong(@NonNull Song currentSong){
        User.getInstance().updateCredit(-currentSong.getPrice());
        SongList.getInstance().unlockSong(currentSong);
        currentSong.setUnlocked(true);
        updateData();
        Toast.makeText(context, "Unlocked " + currentSong.getTitle(), Toast.LENGTH_SHORT).show();
    }


    private void playSong(@NonNull Song currentSong) {
        Toast.makeText(context, "Playing " + currentSong.getTitle(), Toast.LENGTH_SHORT).show();

        updateNoteChunk();
        Log.d("chunks amount", Integer.toString(noteChunks.size()));
        BrokerConnection.getInstance().getMqttClient().publish("Music/Loop", "Green light", 0, null);
        //Toast.makeText(context, "Playing " + currentSong.getTitle(), Toast.LENGTH_SHORT).show();
        BrokerConnection.getInstance().getMqttClient().publish("Music/Data/Change", currentSong.getTitle(), 0, null);

    }

    public void confirmationDialog(Song currentSong) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Unlock song?");
        builder.setMessage("Do you wish to unlock this song?");
        builder.setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                confirm = true;
                unlockSong(currentSong);
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                confirm = false;
            }
        });
        AlertDialog alert = builder.create();
        alert.show();
    }

    @Override
    public void onMessageArrived(String payload) {

        receiveNextSong(payload);

        Log.d("terminal", "terminal needs chunks");
        ObjectMapper mapper = new ObjectMapper();
        ObjectWriter writer = mapper.writer().withDefaultPrettyPrinter();

        if(noteChunks.size() < 1) {
            updateNoteChunk();
        }
        int[] chunk = noteChunks.remove(0);
        try {
            String currentChunk = writer.writeValueAsString(chunk);
            BrokerConnection.getInstance().getMqttClient().publish("Music/Song/Notes", currentChunk, 0, null);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }


    }

    public void receiveNextSong(@NonNull String payload)
    {
        if(SongList.getInstance().getUnlockedSongList().size() == 0){
            Toast.makeText(context, "No unlocked songs", Toast.LENGTH_SHORT).show();
            Log.d("Request", "No unlocked songs");
        }else{
            if(payload.equals(NEXT_SONG_MESSAGE)) {

                SongList.getInstance().increaseSongIdx();
                currentSong = SongList.getInstance().getUnlockedSongList().get(SongList.getInstance().getCurrentSongIdx());
                updateNoteChunk();
                Toast.makeText(context, "Next song is  " + currentSong.getTitle(), Toast.LENGTH_SHORT).show();
                Log.d("Request", "Next song is  " + currentSong.getTitle() + " index is " + SongList.getInstance().getCurrentSongIdx());
                BrokerConnection.getInstance().getMqttClient().publish("Music/Data/Change", currentSong.getTitle(), BrokerConnection.QOS, null);

            }
            if (payload.equals(PREVIOUS_SONG_MESSAGE)){
                SongList.getInstance().decreaseSongIdx();
                currentSong = SongList.getInstance().getUnlockedSongList().get(SongList.getInstance().getCurrentSongIdx());
                updateNoteChunk();
                Toast.makeText(context, "Previous song is " + currentSong.getTitle(), Toast.LENGTH_SHORT).show();
                Log.d("Request", "Previous song is " + currentSong.getTitle() + "index is " + SongList.getInstance().getCurrentSongIdx() );
                BrokerConnection.getInstance().getMqttClient().publish("Music/Data/Change", currentSong.getTitle(), BrokerConnection.QOS, null);
            }
        }

    }

    private void updateNoteChunk(){
        noteChunks = new ArrayList<>(Util.chunkify(currentSong.getNotes(), 40));
    }

    @Override
    public String getSubbedTopic() {
        return "request/notes";
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
    //Holds all views
        private final TextView songTitle;
        private final TextView songPrice;
        private final TextView songDuration;
        private final TextView artistName;
        private final TextView userBalance;
        private final ImageView songImage;
        private final CardView parent;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            parent = itemView.findViewById(R.id.parent_layout);
            songTitle = itemView.findViewById(R.id.song_title);
            songPrice = itemView.findViewById(R.id.song_price);
            songDuration = itemView.findViewById(R.id.song_duration);
            userBalance = itemView.findViewById(R.id.user_balance);
            songImage = itemView.findViewById(R.id.image);
            artistName = itemView.findViewById(R.id.artist_name);
        }
    }

    public static SongLibraryAdapter getInstance(Context context) {
        if(SongLibraryAdapter.instance == null) {
            SongLibraryAdapter.instance = new SongLibraryAdapter(context);
        }
        return SongLibraryAdapter.instance;
    }

}
