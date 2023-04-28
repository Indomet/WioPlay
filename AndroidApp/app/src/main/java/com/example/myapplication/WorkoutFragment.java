package com.example.myapplication;


import android.app.Dialog;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.os.Handler;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.List;
import java.util.concurrent.TimeUnit;

import nl.dionsegijn.konfetti.core.PartyFactory;
import nl.dionsegijn.konfetti.core.emitter.Emitter;
import nl.dionsegijn.konfetti.core.emitter.EmitterConfig;
import nl.dionsegijn.konfetti.core.models.Shape;
import nl.dionsegijn.konfetti.core.models.Size;
import nl.dionsegijn.konfetti.xml.KonfettiView;


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
        return rootView;

    }

    public void changeToNewWorkoutFragment() {
        FragmentTransaction fm = getActivity().getSupportFragmentManager().beginTransaction();
        fm.replace(R.id.frameLayout, newWorkoutFragment).setReorderingAllowed(true).commit();
    }


    @Override
    public void onMessageArrived(String payload) {
        if (workoutManager.getWorkoutHasStarted()) {
            workoutManager.setCaloriesBurnt((int)Float.parseFloat(payload));
            //TODO update the calorie balance and lifetime calories, but thats a seperate issue

            calorieProgressBar.setProgress(workoutManager.getCaloriesBurnt(), true);
            caloriesBurntTextView.setText(Integer.toString(workoutManager.getCaloriesBurnt()));

        }

        //TODO update the time left gto reach goal view
        if (workoutManager.isGoalAchieved()) {
            calorieProgressBar.setProgress(0,true);
            caloriesBurntTextView.setText("0");
            caloriesBurntLabel.setText(WORKOUT_NOT_STARTED);
            createPopWindow();
            workoutManager.stopWorkout();

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



    public void createPopWindow(){
        //create a dialog object that is the pop up window and set the layout to be the xml layout
        Dialog popUpWindow = new Dialog(getActivity());
        popUpWindow.setContentView(R.layout.workout_done_popup);
        popUpWindow.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        //setting the ok button to remove the popup when the user clicks on it
        Button button = popUpWindow.findViewById(R.id.popup_button);
        button.setOnClickListener(view -> popUpWindow.dismiss());

        //creating a golden rectangle to be set as the confetti
        Drawable goldDrawable = new ColorDrawable(ContextCompat.getColor(getContext(), R.color.gold));
        Shape.DrawableShape drawableShapes = new Shape.DrawableShape(goldDrawable, true);
        KonfettiView konfettiView = popUpWindow.findViewById(R.id.konfettiView);
        setUpKonfetti(konfettiView,drawableShapes);
        TextView caloriesBurntText = popUpWindow.findViewById(R.id.popup_calories);
        //escape sequences for formatting purposes
        String text = workoutManager.getCalorieGoal()+"\n\nCalories";
        caloriesBurntText.setText(text);
        popUpWindow.show();


    }
    //This method setups the confetti. Alternatively we can pass parameters to make the method more modular however
    //this is the only place its used and its a static animation meaning it shouldnt change and we dont want it to.
    //Therefore i set the values inside the method
    private void setUpKonfetti(KonfettiView konfettiView, Shape.DrawableShape drawableShapes) {
        //this variable is 300 to note consume too many resources with the animation
         final int DURATION_AND_AMOUNT = 300;
        //sets the spread to be 360 degrees
        final int SPREAD =360;
        //here we set the 2 colors
        final int COLOR1= Color.rgb(255, 215, 0);
        final int COLOR2 = Color.rgb(255, 223, 0);
        final double MIN_X=0.5;
        final double MIN_Y = 0.25;
        final double MAX_X =1;
        final double MAX_Y = 1;
        final int MASS =50;
        final int MASS_VARIANCE =10;
        final int SIZE_IN_DP = 8;

        EmitterConfig config = new Emitter(DURATION_AND_AMOUNT, TimeUnit.MILLISECONDS).max(DURATION_AND_AMOUNT);
        konfettiView.start(
                new PartyFactory(config)
                        .shapes(Shape.Circle.INSTANCE,Shape.Square.INSTANCE,drawableShapes)
                        .spread(SPREAD)
                        .colors(List.of(COLOR1,COLOR2)
                        )
                        .position(MIN_X,MIN_Y,MAX_X,MAX_Y)
                        .sizes(new Size(SIZE_IN_DP,MASS,MASS_VARIANCE))
                        .timeToLive(10000)
                        .fadeOutEnabled(true)
                        .build()
        );


    }


}