package com.example.myapplication;


import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.os.Handler;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.fasterxml.jackson.databind.ObjectMapper;


public class WorkoutFragment extends Fragment implements BrokerConnection.MessageListener {

    private View rootView;
    private  WorkoutManager workoutManager;
    private ProgressBar calorieProgressBar;

    private NewWorkoutFragment newWorkoutFragment;

    private TextView caloriesBurntLabel;
    private TextView caloriesBurntTextView;

    private TextView timeElapsed;
    private boolean stopwatchRunning = false;
    private final String WORKOUT_NOT_STARTED = "No ongoing workout";

    private int currentCalories = 0;
    private int calorieDiff;

    //required empty constructor
    public WorkoutFragment() {

    }
    @Override
    public void onStart() {
        super.onStart();
        if (workoutManager.getWorkoutHasStarted() && !stopwatchRunning) {
            startStopWatch();
        }
    }
    @Override
    public void onResume() {
        super.onResume();
        // Reconnect to any necessary services or resources here
    }

    @Override
    public void onStop() {
        super.onStop();
        // Release any resources that are no longer needed here
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        // Release all resources here
    }


    @Override
    public void onPause() {
        super.onPause();
        Handler handler = new Handler();
        handler.removeCallbacksAndMessages(null);
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_workout, container, false);
        workoutManager=WorkoutManager.getInstance();
        calorieProgressBar = rootView.findViewById(R.id.calorie_progress_bar);

        calorieProgressBar.setMax(workoutManager.getCalorieGoal());

        calorieProgressBar.setProgress(workoutManager.getCaloriesBurnt(), true);

        timeElapsed = rootView.findViewById(R.id.time_elapsed_textview);
        Button newWorkoutButton = rootView.findViewById(R.id.start_new_workout_button);
        newWorkoutButton.setOnClickListener(view -> changeToNewWorkoutFragment());

        //TODO make this into singleton and remove the static attributes
        BrokerConnection broker = MainActivity.brokerConnection;
        broker.setMessageListener(this);

        caloriesBurntLabel = rootView.findViewById(R.id.calories_burnt_label);
        caloriesBurntTextView = rootView.findViewById(R.id.calories_burnt_text);



        if (!workoutManager.getWorkoutHasStarted()) {
            caloriesBurntLabel.setText(WORKOUT_NOT_STARTED);
            calorieProgressBar.setProgress(0);
        }

        newWorkoutFragment = new NewWorkoutFragment();
        caloriesBurntTextView.setText(Integer.toString(workoutManager.getCaloriesBurnt()));
        newWorkoutButton.setOnClickListener(view -> changeToNewWorkoutFragment());
        return rootView;

    }

    public void changeToNewWorkoutFragment() {
        FragmentTransaction fm = getActivity().getSupportFragmentManager().beginTransaction();
        fm.replace(R.id.frameLayout, newWorkoutFragment).setReorderingAllowed(true).commit();
    }


    @Override
    public void onMessageArrived(String payload) {
        calorieDiff = Math.abs(workoutManager.getCaloriesBurnt() - currentCalories);

        if (workoutManager.getWorkoutHasStarted()) {
            workoutManager.setCaloriesBurnt((int)Float.parseFloat(payload));
            calorieProgressBar.setProgress(workoutManager.getCaloriesBurnt(), true);
            caloriesBurntTextView.setText(Integer.toString(workoutManager.getCaloriesBurnt()));
            currentCalories = workoutManager.getCaloriesBurnt();

            //TODO: add calorieDiff to user balance.


        }

        //TODO update the time left gto reach goal view
        if (workoutManager.isGoalAchieved()) {
            workoutManager.stopWorkout();

            calorieProgressBar.setProgress(0,true);
            caloriesBurntTextView.setText("0");
            caloriesBurntLabel.setText(WORKOUT_NOT_STARTED);
        }
    }


    public void startStopWatch() {
        final Handler handler = new Handler();
        handler.post(new Runnable() {
            @Override
            public void run() {

                String time = workoutManager.calculateTimeElapsed();
                timeElapsed.setText(time);

                if (workoutManager.getWorkoutHasStarted()) {
                    workoutManager.incrementSecondsElapsed();
                } else {
                    handler.removeCallbacksAndMessages(null); // stop the handler
                    final String DEFAULT_TIME_FORMAT = "0:00:00";
                    timeElapsed.setText(DEFAULT_TIME_FORMAT);
                    stopwatchRunning = false;
                    return;
                }
                stopwatchRunning = true;
                handler.postDelayed(this, 1000);
            }
        });
    }






}