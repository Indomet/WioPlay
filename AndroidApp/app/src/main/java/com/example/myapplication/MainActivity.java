package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import android.os.Bundle;
import android.os.Environment;
import android.view.MenuItem;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import java.io.File;

public class MainActivity extends AppCompatActivity implements NavigationBarView.OnItemSelectedListener {
    private BottomNavigationView bottomNavigation;
    private Workout_Fragment workoutFragment;
    private MusicFragment musicFragment;
    private SettingsFragment settingsFragment;

    //This field is set to the singleton getInstance method then the same reference is used
    //elsewhere ein other classes
    public static BrokerConnection brokerConnection;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        brokerConnection = BrokerConnection.initialize(getApplicationContext());

        //initializing the variables
        bottomNavigation = findViewById(R.id.bottomNavigationView);
        workoutFragment = new Workout_Fragment();
        musicFragment = new MusicFragment();
        settingsFragment = new SettingsFragment();
        //replaces the frame layout with the fragment when app is opened not sure if needed tbh
        changeFragment(musicFragment);
        bottomNavigation.setOnItemSelectedListener(this);


        String downloadsDir = Environment.getExternalStorageDirectory() + "/" + Environment.DIRECTORY_DOCUMENTS;

        String userPath = downloadsDir + "/user.json";
        File userFile = new File(userPath);

        String songPath = downloadsDir + "/songList.json";
        File songFile = new File(songPath);

        String managerPath = downloadsDir + "/workoutManager.json";
        File managerFile = new File(managerPath);

        User.initialize(userFile);

        SongList.initialize(songFile);

        WorkoutManager.initialize(managerFile);

    }

    @Override   //This method checks which fragment was selected by the user
    //and switches the layout accordingly
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.workout:

                return changeFragment(workoutFragment);

            case R.id.settings:
                return changeFragment(settingsFragment);

            case R.id.music:
                return changeFragment(musicFragment);
        }
        return false;
    }

    public boolean changeFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();

// Add or replace the fragment using the FragmentManager
        fragmentManager.beginTransaction()
                .replace(R.id.frameLayout, fragment)
                .addToBackStack(null)
                .commit();
        return true;
    }


}
