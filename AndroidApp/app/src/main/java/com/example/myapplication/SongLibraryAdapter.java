package com.example.myapplication;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Formatter;
import java.util.concurrent.TimeUnit;

public class SongLibraryAdapter extends RecyclerView.Adapter<SongLibraryAdapter.ViewHolder>{

    private ArrayList<Song> songsList = new ArrayList<>();
    private Context context;

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

        int duration = songsList.get(currentPosition).getDuration();



        holder.songTitle.setText(songsList.get(currentPosition).getTitle());

        holder.songDuration.setText((duration / 60) + ":" + String.format("%02d", duration % 60));
        if(!songsList.get(currentPosition).isUnlocked()){
            holder.songPrice.setText(Integer.toString(songsList.get(currentPosition).getPrice())+" CC");
        }else{
            holder.songPrice.setText("Play");
        }
        //TODO: add other attributes

        holder.parent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(songsList.get(currentPosition).isUnlocked()){
                    //TODO: Play song
                    Toast.makeText(context, "Selected " + songsList.get(currentPosition).getTitle(), Toast.LENGTH_SHORT).show();
                }else{
                    //TODO: popup window to unlock song by spending Calorie Credits

                    Toast.makeText(context, "Locked, " +songsList.get(currentPosition).getTitle() + " costs " + songsList.get(currentPosition).getPrice() + " Calorie Credits", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return songsList.size();
    }

    public void setSongsList(ArrayList<Song> songsList){
        this.songsList = songsList;
        notifyDataSetChanged(); //Notify adapter whenever the song list has updated, so the up to date information can be shown
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
    //Holds all views
        private TextView songTitle, songPrice, songDuration, songImage;
        private CardView parent;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            parent = itemView.findViewById(R.id.parent_layout);
            songTitle = itemView.findViewById(R.id.song_title);
            songPrice = itemView.findViewById(R.id.song_price);
            songDuration = itemView.findViewById(R.id.song_duration);


        }
    }
}
