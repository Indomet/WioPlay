package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import android.os.Bundle;
import android.view.MenuItem;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import java.io.File;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

public class MainActivity extends AppCompatActivity implements NavigationBarView.OnItemSelectedListener{
    private BottomNavigationView bottomNavigation;
    private Workout_Fragment workoutFragment;
    private SearchFragment searchFragment;
    private MusicFragment musicFragment;
    private SettingsFragment settingsFragment;

    //TODO THIS IS TO BE REFACTORED ITS BAD PRACTICE make into singleton pattern
    public static BrokerConnection brokerConnection;

    public static User user;

    public static SongList songList;
    public static ScheduledExecutorService scheduledExecutorService = Executors.newScheduledThreadPool(1);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        brokerConnection =  BrokerConnection.getInstance(getApplicationContext());
        //initializing the variables
        bottomNavigation = findViewById(R.id.bottomNavigationView);
        workoutFragment = new Workout_Fragment();
        searchFragment = new SearchFragment();
        musicFragment = new MusicFragment();
        settingsFragment = new SettingsFragment();
        //replaces the frame layout with the fragment when app is opened not sure if needed tbh
        changeFragment(musicFragment);
        bottomNavigation.setOnItemSelectedListener(this);


        String userPath = this.getFilesDir().getPath() + "/user.json"; //data/user/0/myapplication/files
        File userFile = new File(userPath);

        String songPath = this.getFilesDir().getPath() + "/songList.json";
        File songFile = new File(songPath);

        user = User.getInstance(userFile);
//        user.setCalorieCredit(9000);

        songList = new SongList(songFile);





    }

    @Override   //This method checks which fragment was selected by the user
    //and switches the layout accordingly
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch(item.getItemId()){
            case R.id.workout:

                return changeFragment(workoutFragment);

            case R.id.search:
                //brokerConnection.getMqttClient().publish("calories","only top gs can see this message",brokerConnection.QOS,null);
                return changeFragment(searchFragment);

            case R.id.settings:
                return changeFragment(settingsFragment);

            case R.id.music:
                return changeFragment(musicFragment);
        }
        return false;
    }

    public boolean changeFragment(Fragment fragment){
        FragmentManager fragmentManager = getSupportFragmentManager();

// Add or replace the fragment using the FragmentManager
        fragmentManager.beginTransaction()
                .replace(R.id.frameLayout, fragment)
                .addToBackStack(null)
                .commit();
        return true;
    }

    public void publishChangedData(){

    }


    }
