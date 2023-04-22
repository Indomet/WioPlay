package com.example.myapplication;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
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
        int currentPosition = holder.getBindingAdapterPosition();
        Song currentSong = songsList.get(currentPosition);
        int duration = songsList.get(currentPosition).getDuration();



        holder.songTitle.setText(songsList.get(currentPosition).getTitle());

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
                    //TODO: Play song
                    Toast.makeText(context, "Playing " + currentSong.getTitle(), Toast.LENGTH_SHORT).show();
                }else{
                    //TODO: popup window to confirm unlock song by spending Calorie Credits
                    if(MainActivity.user.getCalorieCredit() >= currentSong.getPrice()){
                        MainActivity.user.updateCredit(-currentSong.getPrice());//deducted
                        currentSong.setUnlocked(true);
                        updateData();
                        Toast.makeText(context, "Unlocked " + currentSong.getTitle(), Toast.LENGTH_SHORT).show();
                        //TODO: notify data change in fragment_music
                    }else{
                        Toast.makeText(context, "Not enough calorie credits", Toast.LENGTH_SHORT).show();
                    }
                    //Toast.makeText(context, "Locked, " +currentSong.getTitle() + " costs " + currentSong.getPrice() + " Calorie Credits", Toast.LENGTH_SHORT).show();



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

    public void updateData(){
        TextView view =((MainActivity)context).findViewById(R.id.user_balance); //Updates balance display in MusicFragment
        view.setText(Integer.toString(MainActivity.user.getCalorieCredit()));
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
    //Holds all views
        private TextView songTitle, songPrice, songDuration, songImage, userBalance;

        private CardView parent;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            parent = itemView.findViewById(R.id.parent_layout);
            songTitle = itemView.findViewById(R.id.song_title);
            songPrice = itemView.findViewById(R.id.song_price);
            songDuration = itemView.findViewById(R.id.song_duration);
            userBalance = itemView.findViewById(R.id.user_balance);


        }
    }
}
