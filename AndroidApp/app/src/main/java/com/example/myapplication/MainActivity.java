package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;


import android.content.ClipData;
import android.content.Context;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

public class MainActivity extends AppCompatActivity implements NavigationBarView.OnItemSelectedListener{
    private BottomNavigationView bottomNavigation;
    private WorkoutFragment workoutFragment;
    private SearchFragment searchFragment;
    private MusicFragment musicFragment;
    private  SettingsFragment settingsFragment;

    //TODO THIS IS TO BE REFACTORED ITS BAD PRACTICE make into singleton pattern
    public static BrokerConnection brokerConnection;
    public static User user= User.getInstance(); // This is changed in order to have the same object evertime this method in called



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //initializing the variables
        bottomNavigation = findViewById(R.id.bottomNavigationView);
        workoutFragment = new WorkoutFragment();
        searchFragment = new SearchFragment();
        musicFragment = new MusicFragment();
        settingsFragment = new SettingsFragment();
        //replaces the frame layout with the fragment when app is opened not sure if needed tbh
        changeFragment(musicFragment);
        bottomNavigation.setOnItemSelectedListener(this);
        brokerConnection = BrokerConnection.getInstance(getApplicationContext());// the instance of the brockerConnection class
                                                                                 // that is being returned is one object evertime
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
        getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout,fragment).setReorderingAllowed(true)
                .addToBackStack(null)
                .commit();
        return true;
    }

    public void publishChangedData(){

    }


    }
