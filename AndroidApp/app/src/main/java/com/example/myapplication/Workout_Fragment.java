package com.example.myapplication;

import android.app.Dialog;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.DrawableRes;
import androidx.annotation.IdRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.os.Handler;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;

import java.io.File;
import java.time.LocalDate;
import java.util.Calendar;
import java.util.List;
import java.util.concurrent.TimeUnit;

import nl.dionsegijn.konfetti.core.PartyFactory;
import nl.dionsegijn.konfetti.core.emitter.Emitter;
import nl.dionsegijn.konfetti.core.emitter.EmitterConfig;
import nl.dionsegijn.konfetti.core.models.Shape;
import nl.dionsegijn.konfetti.core.models.Size;
import nl.dionsegijn.konfetti.xml.KonfettiView;


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

    private TextView caloriesBurnt;
    private Button stopOrPlayStopwatch;
    private TextView timeElapsed;
    private TextView timeLeft;
    //TODO DELETER AFTER TEST
    private NewWorkoutFragment newWorkoutFragment;

    private boolean stopwatchRunning = false;
    WorkoutManager workoutManager;
    private User user;

    private View rootView;
    MaterialCalendarView calendarView;


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

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_workout, container, false);

        workoutManager = WorkoutManager.getInstance();
        BrokerConnection broker = MainActivity.brokerConnection;
        broker.setMessageListener(this);
        newWorkoutFragment = new NewWorkoutFragment();
        String filePath = getActivity().getFilesDir().getPath() + "/user.json"; //data/user/0/myapplication/files
        user = User.getInstance(new File(filePath));

        widgetInit();

        return rootView;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void widgetInit() {
        calendarView = rootView.findViewById(R.id.day_date_picker);
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
        caloriesBurnt = rootView.findViewById(R.id.calories_burnt_textview);
        stopOrPlayStopwatch = rootView.findViewById(R.id.stop_or_start_stopwatch_btn);
        timeElapsed = rootView.findViewById(R.id.stopwatch_textview);
        timeLeft = rootView.findViewById(R.id.time_left_textview);
        addWalkingWorkout.setOnClickListener(view -> changeToNewWorkoutFragment(view));
        addHikingWorkout.setOnClickListener(view -> changeToNewWorkoutFragment(view));
        addRunningWorkout.setOnClickListener(view -> changeToNewWorkoutFragment(view));

        calendarView.setOnDateChangedListener((widget, date, selected) -> onDateSelected(date));


        caloriesProgressbar.setMax(workoutManager.getCalorieGoal());
        caloriesProgressbar.setProgress(workoutManager.getCaloriesBurnt(), true);
        if (!workoutManager.getWorkoutHasStarted()) {
            stopOrPlayStopwatch.setVisibility(View.INVISIBLE);
            caloriesBurnt.setText("0");
            caloriesProgressbar.setProgress(0,true);
        }


        workoutsCount.setText(Integer.toString(workoutManager.getTotalWorkoutsCount()));
        userBalance.setText(Integer.toString(user.getCalorieCredit()));

        monthlyWorkoutsProgressbar.setMax(user.getMonthlyWorkouts());
        monthlyWorkoutsProgressbar.setProgress(workoutManager.getCurrentMonthlyWorkoutsProgress());

        username.setText(user.getUsername());

        //sets the date to be today

        CalendarDay today = CalendarDay.today();
        calendarView.setDateSelected(today,true);
        calendarView.state().edit().setMaximumDate(today).commit();
        //reset monthly progress bar if the today is first day of the month
        if(today.getDay()==1){
            final int resetVal = 0;
            monthlyWorkoutsProgressbar.setProgress(resetVal);
            workoutManager.setCurrentMonthlyWorkoutsProgress(resetVal);
        }

    }

    public void changeToNewWorkoutFragment(View buttonPressed) {
        newWorkoutFragment.setWorkoutType(buttonPressed.getId());
        FragmentTransaction fm = getActivity().getSupportFragmentManager().beginTransaction();
        fm.replace(R.id.frameLayout, newWorkoutFragment).setReorderingAllowed(true).commit();
    }
    public void onDateSelected(CalendarDay date) {
        FinishedWorkoutData data = workoutManager.getWorkoutDataHashMap().get(date);
        //check if its before
        if(CalendarDay.today().equals(date)){
            showToday();
        }
        else if(date.isBefore(CalendarDay.today())) {
            if(data==null || date.isAfter(CalendarDay.today())){
                //just showing that the progress is 0 since no workouts done that day
                showPastWorkoutStats("00:00",0,100);
            }
            else{
                //there is data in the past now setting the proper views to show it
                int totalSeconds = data.getDurationInSeconds();
                int hours = totalSeconds / 3600;
                int minutes = (totalSeconds % 3600) / 60;
                String timeString = String.format("%02d:%02d", hours, minutes);
                showPastWorkoutStats(timeString,data.getCaloriesBurntWithExercise(), data.getGoalCalories());
            }
        }

        }


    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onMessageArrived(String payload) {

        if (workoutManager.getWorkoutHasStarted()) {
            workoutManager.setCaloriesBurnt((int)Float.parseFloat(payload));
            //TODO update the calorie balance

            caloriesProgressbar.setProgress(workoutManager.getCaloriesBurnt(), true);
            caloriesBurnt.setText(Integer.toString(workoutManager.getCaloriesBurnt()));
            String calculatedTimeLeft = workoutManager.calculateTimeLeft();
            timeLeft.setText(calculatedTimeLeft);

        }

        //TODO update the time left gto reach goal view
        if (workoutManager.isGoalAchieved()&& workoutManager.getWorkoutHasStarted()) {
            //before anything add the workout data
            CalendarDay date = CalendarDay.today();
            //get the time calories burnt and the goal with the workout after its done and the type
            FinishedWorkoutData finishedWorkout = new FinishedWorkoutData(workoutManager.getDurationInSeconds(),workoutManager.getCaloriesBurnt(),
                    workoutManager.getType(),workoutManager.getCalorieGoal());
            workoutManager.addWorkoutData(finishedWorkout,date);


            caloriesProgressbar.setProgress(0,true);
            caloriesBurnt.setText("0");
            createPopWindow();
            workoutManager.stopWorkout();
            timeLeft.setText("0:00:00");
            //todo use manager
            workoutManager.incrementTotalWorkouts();
            workoutManager.incrementMonthlyWorkouts();
            monthlyWorkoutsProgressbar.setProgress(workoutManager.getCurrentMonthlyWorkoutsProgress());
            workoutsCount.setText(Integer.toString(workoutManager.getTotalWorkoutsCount()));
            stopOrPlayStopwatch.setVisibility(View.INVISIBLE);

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

    public void adjustVisibility(int statsVisibility,int pastStatsVisibility){

        //first remove the stats views for the current workouts
        rootView.findViewById(R.id.stopwatch_whitebix).setVisibility(statsVisibility);
        rootView.findViewById(R.id.stopwatch_icon).setVisibility(statsVisibility);
        rootView.findViewById(R.id.stopwatch_textview).setVisibility(statsVisibility);
        rootView.findViewById(R.id.stopwatch_whitebix).setVisibility(statsVisibility);
        rootView.findViewById(R.id.stop_or_start_stopwatch_btn).setVisibility(statsVisibility);
        rootView.findViewById(R.id.time_left_label).setVisibility(statsVisibility);
        rootView.findViewById(R.id.time_left_whitebox).setVisibility(statsVisibility);
        rootView.findViewById(R.id.time_left_textview).setVisibility(statsVisibility);

        TextView totalTime = rootView.findViewById(R.id.total_past_exercise_time);
        TextView whiteBox = rootView.findViewById(R.id.total_exercise_time_box);
        totalTime.setVisibility(pastStatsVisibility);
        whiteBox.setVisibility(pastStatsVisibility);
    }

    public void showPastWorkoutStats(String time, int caloriesBurntData,int GoalCalories){
        //make stats views invisible while past stats views visible
        adjustVisibility(View.INVISIBLE,View.VISIBLE);
        TextView totalTime = rootView.findViewById(R.id.total_past_exercise_time);
        totalTime.setText(time);
        caloriesProgressbar.setProgress(caloriesBurntData);
        caloriesProgressbar.setMax(GoalCalories);
        caloriesBurnt.setText(Integer.toString(caloriesBurntData));
    }

    public void showToday(){
        //make stats views invisible while past stats views visible
        adjustVisibility(View.VISIBLE,View.INVISIBLE);
        caloriesProgressbar.setMax(workoutManager.getCalorieGoal());
        caloriesProgressbar.setProgress(workoutManager.getCaloriesBurnt());
        caloriesBurnt.setText(Integer.toString(workoutManager.getCaloriesBurnt()));

    }

}