package com.example.myapplication;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
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

import org.graalvm.compiler.core.common.util.Util;

import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.ScheduledExecutorService;

import javax.swing.text.Utilities;

public class SongLibraryAdapter extends RecyclerView.Adapter<SongLibraryAdapter.ViewHolder>{

    private ArrayList<Song> songsList = new ArrayList<>();
    private Context context;
    private boolean confirm;

    public SongLibraryAdapter(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //ViewGroup is the parent of all layout types
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.music_item_list, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        //position is the index of the items in the recycler view
        int currentPosition = holder.getBindingAdapterPosition();

        Song currentSong = songsList.get(currentPosition); //This has to be initialized here to work properly.
        Picasso.get().load(currentSong.getImageURL()).into(holder.songImage);//Loads image from url
        holder.artistName.setText(currentSong.getArtist());
        holder.songTitle.setText(currentSong.getTitle());
        int duration = currentSong.getDuration();
        holder.songDuration.setText((duration / 60) + ":" + String.format("%02d", duration % 60));

        if(!currentSong.isUnlocked()){
            holder.songPrice.setText(Integer.toString(currentSong.getPrice())+" CC");
        }else{
            holder.songPrice.setText("Play");
        }
        //TODO Glide framework image display
        holder.parent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(currentSong.isUnlocked()){
                    playSong(currentSong);
                }else{
                    if (MainActivity.user.getCalorieCredit() >= currentSong.getPrice()) {
                        confirmationDialog(currentSong); //Calls the popup window
                    } else {
                        Toast.makeText(context, "Not enough calorie credits", Toast.LENGTH_SHORT).show();
                        //Toast.makeText(context, "Locked, " +currentSong.getTitle() + " costs " + currentSong.getPrice() + " Calorie Credits", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        int size;
        try{
            size = songsList.size();
        } catch (Exception e){
            size = 0;
        }
        return size;

    }

    public void setSongsList(ArrayList<Song> songsList){
        this.songsList = songsList;
        notifyDataSetChanged(); //Notify adapter whenever the song list has updated, so the up to date information can be shown
    }

    public void updateData(){
        TextView view =((MainActivity)context).findViewById(R.id.user_balance); //Updates balance display in MusicFragment
        view.setText(Integer.toString(MainActivity.user.getCalorieCredit()));
        notifyDataSetChanged();
    }

    private void unlockSong(@NonNull Song currentSong){
        MainActivity.user.updateCredit(-currentSong.getPrice());
        MainActivity.songList.unlockSong(currentSong);
        currentSong.setUnlocked(true);
        updateData();
        Toast.makeText(context, "Unlocked " + currentSong.getTitle(), Toast.LENGTH_SHORT).show();
    }



    private void playSong(@NonNull Song currentSong) {
        try {
            String notes = writer.writeValueAsString(currentSong.getNotes());
            String[] songSegmentNotes = Util.splitStringInSegments(notes, 12, 40); //
            int[][] songNotes =  Util.splitIntegerArrayInSegments(currentSong.getNotes(), 30, 40);

            // Song.resetCurrentChunkIdx(); //

            // Two possible solutions: MQQT & Future-Scheduling-Invoking of publish-methods

            // MQTT Solution:
            // 1. Publish array: [currentSongChunkSegment, durationOfSegment] --> "currentSongChunkSegment+durationOfSegmentInSeconds"
            // 2. In Arduino: Recieve array, set timer of 'durationOfSegment'
            // 3. When timer is up, send signal to Android Studio and publish next array

            // BrokerConnection.getInstance(context).getMqttClient().publish(BrokerConnection.SONG_NOTES_TOPIC, notes, 0, null); // PREVIOUS
            // BrokerConnection.getInstance(context).getMqttClient().publish(BrokerConnection.SONG_NOTES_TOPIC, songSegmentNotes[Song.getCurrentChunkIdx()], 0, null); // CURRENT


            // Future-Scheduling-Invoking of publish-methods solution

            MainActivity.scheduledExecutorService.shutdownNow(); // Interrupt future-scheduled publishes of the previously played song's chunks
            int currentScheduleDelay = 0;

            for (int i = 0; i < songNotes.length; i++)
            {
                ObjectMapper mapper = new ObjectMapper();
                ObjectWriter writer = mapper.writer().withDefaultPrettyPrinter();

                String currentChunk = writer.writeValueAsString(songNotes[i]);

                MainActivity.scheduledExecutorService.schedule(()->BrokerConnection.getInstance(context).getMqttClient().publish(BrokerConnection.SONG_NOTES_TOPIC, currentChunk, 0, null), currentScheduleDelay, TimeUnit.SECONDS);

                // MainActivity.scheduledExecutorService.schedule(()->BrokerConnection.getInstance(context).getMqttClient().publish(BrokerConnection.SONG_NOTES_TOPIC, songSegmentNotes[i], 0, null), currentScheduleDelay, TimeUnit.SECONDS);
                currentScheduleDelay += currentSong.calculateChunkDuration(songSegmentNotes[i].length());
            }

            scheduledExecutorService.shutdown(); // Shutdown when song is done for the sake of performance

            // Song.incrementCurrentChunkIdx(); // (This is only needed for MQTT solution)

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        Toast.makeText(context, "Playing " + currentSong.getTitle(), Toast.LENGTH_SHORT).show();

        // TODO: Implement the logic to play the song
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

//TODO Display artist name
    public class ViewHolder extends RecyclerView.ViewHolder{
    //Holds all views
        private TextView songTitle, songPrice, songDuration, artistName, userBalance;
        private ImageView songImage;

        private CardView parent;
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
}
