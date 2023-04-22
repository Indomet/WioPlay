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
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

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
        holder.textView.setText(songsList.get(position).getTitle());
        final int currentPosition = position;

        holder.parent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(songsList.get(currentPosition).isUnlocked()){
                    Toast.makeText(context, "Selected " + songsList.get(currentPosition).getTitle(), Toast.LENGTH_SHORT).show();
                }else{
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
        private TextView textView;
        private RelativeLayout parent;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.song_title);
            parent = itemView.findViewById(R.id.parent_layout);
        }
    }
}
