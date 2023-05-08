package com.example.myapplication;

import android.app.Dialog;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.os.Handler;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import android.widget.ImageButton;
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

import com.fasterxml.jackson.databind.ObjectMapper;


import java.util.Date;


public class Workout_Fragment extends Fragment implements BrokerConnection.MessageListener{
    private TextView userBalance;
    private TextView workoutsCount;
    private TextView username;
    private ImageButton addWalkingWorkout;
    private ImageButton addRunningWorkout;
    private ImageButton addHikingWorkout;
    private ProgressBar monthlyWorkoutsProgressbar;
    private ProgressBar caloriesProgressbar;
    private TextView targetWorkoutsThisMonth;
    private TextView currentWorkoutsThisMonth;
    private TextView walkingClickableView;
    private TextView runningClickableView;
    private TextView hikingClickableView;
    private TextView caloriesBurnt;
    private Button stopOrPlayStopwatch;
    private TextView timeElapsed;
    private TextView timeLeft;
    //TODO DELETER AFTER TEST
    private NewWorkoutFragment newWorkoutFragment;

    private boolean stopwatchRunning = false;
    private final String WORKOUT_NOT_STARTED = "No ongoing workout";

    private View rootView;
    private WorkoutManager workoutManager;

    int currentCalorie = 0;

    //required empty constructor
    public Workout_Fragment() {

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
        workoutManager = WorkoutManager.getInstance();
        BrokerConnection broker = MainActivity.brokerConnection;
        broker.setMessageListener(this);
        newWorkoutFragment = new NewWorkoutFragment();
        widgetInit();

        return rootView;
    }

    private void widgetInit() {

        userBalance = rootView.findViewById(R.id.workout_tab_user_balance);
        workoutsCount = rootView.findViewById(R.id.user_total_workouts);
        username = rootView.findViewById(R.id.Username_workout_tab);
        addWalkingWorkout = rootView.findViewById(R.id.add_walking_workout_btn);
        addRunningWorkout = rootView.findViewById(R.id.add_running_workout_btn);
        addHikingWorkout = rootView.findViewById(R.id.add_hiking_workout_btn);
        monthlyWorkoutsProgressbar =  rootView.findViewById(R.id.monthly_progressbar);
        caloriesProgressbar =  rootView.findViewById(R.id.calories_burnt_progressbar);
        targetWorkoutsThisMonth =  rootView.findViewById(R.id.max_workouts_this_month);
        currentWorkoutsThisMonth =  rootView.findViewById(R.id.current_workouts_this_month);
        walkingClickableView =  rootView.findViewById(R.id.walking_textview_BTN);
        runningClickableView =  rootView.findViewById(R.id.running_textview_BTN);
        hikingClickableView =  rootView.findViewById(R.id.hiking_textview_BTN);
        caloriesBurnt = rootView.findViewById(R.id.calories_burnt_textview);
        stopOrPlayStopwatch = rootView.findViewById(R.id.stop_or_start_stopwatch_btn);
        timeElapsed = rootView.findViewById(R.id.stopwatch_textview);
        timeLeft = rootView.findViewById(R.id.time_left_textview);
        //TODO ADD OTHER BTNS AND RPELACE WITH POPUP
        addWalkingWorkout.setOnClickListener(view -> changeToNewWorkoutFragment(view));
        addHikingWorkout.setOnClickListener(view -> changeToNewWorkoutFragment(view));
        addRunningWorkout.setOnClickListener(view -> changeToNewWorkoutFragment(view));

        monthlyWorkoutsProgressbar.setMax(MainActivity.user.getMonthlyWorkouts());
        targetWorkoutsThisMonth.setText(Integer.toString(MainActivity.user.getMonthlyWorkouts()));
        username.setText(MainActivity.user.getUsername());
        userBalance.setText(Integer.toString(MainActivity.user.getCalorieCredit()));
        //TODO USE WORKOUT MANAGER TO GET HTE TOTAL WORKOUTS COUNT
        //workoutsCount.setText(Integer.toString(MainActivity.user.getWorkoutsCount()));
        caloriesProgressbar.setMax(workoutManager.getCalorieGoal());
        caloriesProgressbar.setProgress(workoutManager.getCaloriesBurnt(), true);
        int calories = workoutManager.getCaloriesBurnt();
        caloriesBurnt.setText(Integer.toString(calories));
        caloriesProgressbar.setProgress(calories,true);

    }
    public void changeToNewWorkoutFragment(View buttonPressed) {
        newWorkoutFragment.setWorkoutType(buttonPressed.getId());
        FragmentTransaction fm = getActivity().getSupportFragmentManager().beginTransaction();
        fm.replace(R.id.frameLayout, newWorkoutFragment).setReorderingAllowed(true).commit();
    }
    public void onDateSelected(@Nullable Date date) {
        if(date != null){
            // do something with selected date
        }
    }

    @Override
    public void onMessageArrived(String payload) {

        if (workoutManager.getWorkoutHasStarted()) {
            workoutManager.setCaloriesBurnt((int)Float.parseFloat(payload));
            //TODO update the calorie balance and lifetime calories, but thats a seperate issue
            if(caloriesProgressbar==null){
                Log.d("tag","the shit is null");
            }
            caloriesProgressbar.setProgress(workoutManager.getCaloriesBurnt(), true);
            caloriesBurnt.setText(Integer.toString(workoutManager.getCaloriesBurnt()));
            String calculatedTimeLeft = workoutManager.calculateTimeLeft();
            timeLeft.setText(calculatedTimeLeft);

            int cumulativeCalorie = Integer.parseInt(payload);
            int changeInCalories = Math.abs(cumulativeCalorie - currentCalorie);
            currentCalorie = changeInCalories;
            MainActivity.user.updateLifeTimeCalories(changeInCalories);
            MainActivity.user.updateCredit(changeInCalories);
        }

        //TODO update the time left gto reach goal view
        if (workoutManager.isGoalAchieved()&& workoutManager.getWorkoutHasStarted()) {
            caloriesProgressbar.setProgress(0,true);
            caloriesBurnt.setText("0");
            createPopWindow();
            workoutManager.stopWorkout();
            timeLeft.setText("0:00:00");
            currentCalorie = 0;
            //TODO USE WOKROUT MANAGER TO INCREMENT THE STUFF
            //MainActivity.user.incrementWorkoutsCounts();

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