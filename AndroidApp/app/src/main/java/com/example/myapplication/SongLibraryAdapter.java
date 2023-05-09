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

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

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
