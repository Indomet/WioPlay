package com.example.myapplication;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.os.Environment;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import java.util.List;
import java.util.concurrent.TimeUnit;
import nl.dionsegijn.konfetti.core.PartyFactory;
import nl.dionsegijn.konfetti.core.emitter.Emitter;
import nl.dionsegijn.konfetti.core.emitter.EmitterConfig;
import nl.dionsegijn.konfetti.core.models.Shape;
import nl.dionsegijn.konfetti.core.models.Size;
import nl.dionsegijn.konfetti.xml.KonfettiView;


public class Workout_Fragment extends Fragment implements BrokerConnection.MessageListener{
    private TextView userBalance;
    private TextView workoutsCount;
    private int currentCalorie=0;
    private TextView username;
    private ImageButton addWalkingWorkout;
    private ImageButton addRunningWorkout;
    private ImageButton addHikingWorkout;
    private ProgressBar monthlyWorkoutsProgressbar;
    private ProgressBar caloriesProgressbar;
    private TextView targetWorkoutsThisMonth;

    private TextView caloriesBurnt;

    private TextView timeElapsed;
    private TextView timeLeft;
    private NewWorkoutFragment newWorkoutFragment;

    private boolean stopwatchRunning = false;
    private WorkoutManager workoutManager;
    private User user;
    private ImageView profilepicture;

    private View rootView;
    private MaterialCalendarView calendarView;
    private Handler handler = new Handler();


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
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }


    @Override
    public void onPause() {
        super.onPause();
        //pause the stopwatch
        handler.removeCallbacksAndMessages(null);
        stopwatchRunning=false;
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    Workout_Fragment(){
        BrokerConnection broker= MainActivity.brokerConnection;
        broker.addMessageListener(this);
        newWorkoutFragment = new NewWorkoutFragment();
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_workout, container, false);

        BrokerConnection broker = MainActivity.brokerConnection;

        user = User.getInstance();
        workoutManager = WorkoutManager.getInstance();

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
        caloriesBurnt = rootView.findViewById(R.id.calories_burnt_textview);
        timeElapsed = rootView.findViewById(R.id.stopwatch_textview);
        timeLeft = rootView.findViewById(R.id.time_left_textview);
        profilepicture= rootView.findViewById(R.id.user_picture);
        addWalkingWorkout.setOnClickListener(view -> changeToNewWorkoutFragment(view));
        addHikingWorkout.setOnClickListener(view -> changeToNewWorkoutFragment(view));
        addRunningWorkout.setOnClickListener(view -> changeToNewWorkoutFragment(view));

        calendarView.setOnDateChangedListener((widget, date, selected) -> onDateSelected(date));
        targetWorkoutsThisMonth.setText(Integer.toString(user.getMonthlyWorkouts()));

        caloriesProgressbar.setMax(workoutManager.getCalorieGoal());
        caloriesProgressbar.setProgress(workoutManager.getCaloriesBurnt(), true);
        int cals = workoutManager.getCaloriesBurnt();
        caloriesBurnt.setText(Integer.toString(cals));
        caloriesProgressbar.setProgress(cals,true);
        addProfile();



        workoutsCount.setText(Integer.toString(workoutManager.getTotalWorkoutsCount()));
        userBalance.setText(Integer.toString(user.getCalorieCredit()));

        monthlyWorkoutsProgressbar.setMax(user.getMonthlyWorkouts());
        monthlyWorkoutsProgressbar.setProgress(workoutManager.getCurrentMonthlyWorkoutsProgress(),true);

        username.setText(user.getUsername());

        //sets the date to be today

        CalendarDay today = CalendarDay.today();
        calendarView.setDateSelected(today,true);
        calendarView.state().edit().setMaximumDate(today).commit();
        //reset monthly progress bar if the today is first day of the month
        if(today.getDay()==1){
            final int resetVal = 0;
            monthlyWorkoutsProgressbar.setProgress(resetVal,true);
            workoutManager.setCurrentMonthlyWorkoutsProgress(resetVal);
        }

    }

    public void changeToNewWorkoutFragment(View buttonPressed) {
        newWorkoutFragment.setWorkoutType(buttonPressed.getId());
        Util.changeFragment(newWorkoutFragment, getActivity());
    }
    public void onDateSelected(CalendarDay date) {
        FinishedWorkoutData data = workoutManager.getWorkoutDataHashMap().get(date.toString());
        //check if its before
        if(CalendarDay.today().equals(date)){
            showToday();
        }
        else if(date.isBefore(CalendarDay.today())) {
            if(data==null || date.isAfter(CalendarDay.today())){
                //just showing that the progress is 0 since no workouts done that day
                showPastWorkoutStats("00:00:00",0,100);
            }
            else{
                //there is data in the past now setting the proper views to show it
                int totalSeconds = data.getDurationInSeconds();
                String timeString =Util.formatHoursMinsSecs(totalSeconds);
                showPastWorkoutStats(timeString,data.getCaloriesBurntWithExercise(), data.getGoalCalories());
            }
        }
    }



    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onMessageArrived(String payload) {

        if (workoutManager.getWorkoutHasStarted()) {
            Log.d("Calorie", "Burned");
            int integerPayload = (int)Float.parseFloat(payload);
            workoutManager.setCaloriesBurnt(integerPayload);

            caloriesProgressbar.setProgress(workoutManager.getCaloriesBurnt(), true);
            caloriesBurnt.setText(Integer.toString(workoutManager.getCaloriesBurnt()));
            String calculatedTimeLeft = workoutManager.calculateTimeLeft();
            timeLeft.setText(calculatedTimeLeft);

            int diff = workoutManager.calculateCalDiff(integerPayload);
            workoutManager.setCurrentCalorie(integerPayload);

            user.updateCredit(diff);
            userBalance.setText(Integer.toString(user.getCalorieCredit()));
        }

        if (workoutManager.isGoalAchieved()&& workoutManager.getWorkoutHasStarted()) {
            //before anything add the workout data
            resetWorkoutUI();

            createPopWindow();
            workoutManager.stopWorkout();
            workoutManager.incrementTotalWorkouts();
            workoutManager.incrementMonthlyWorkouts();
            monthlyWorkoutsProgressbar.setProgress(workoutManager.getCurrentMonthlyWorkoutsProgress(),true);
            workoutsCount.setText(Integer.toString(workoutManager.getTotalWorkoutsCount()));
            //refresh the fragment such that the views get updated
            workoutManager.setCurrentCalorie(0);
            Util.changeFragment(this, getActivity());

        }

        else if(workoutManager.getDurationInSeconds()<=workoutManager.getSecondsElapsed()){
            resetWorkoutUI();
            Toast.makeText(rootView.getContext(),"You didnt complete the workout in time ",Toast.LENGTH_LONG);
        }
    }

    public void resetWorkoutUI(){
        CalendarDay date = CalendarDay.today();
        //get the time calories burnt and the goal with the workout after its done and the type
        FinishedWorkoutData finishedWorkout = new FinishedWorkoutData(workoutManager.getDurationInSeconds(),workoutManager.getCaloriesBurnt(),
                workoutManager.getType(),workoutManager.getCalorieGoal());
        workoutManager.addWorkoutData(finishedWorkout,date);

        //this makes sure that the progress bar is set to 0. The lib is very buggy so sometimes it updates others it doesnt

        caloriesProgressbar.setProgress(0,true);

        caloriesBurnt.setText("0");

        workoutManager.stopWorkout();
        timeLeft.setText("0:00:00");

        workoutManager.setCurrentCalorie(0);
        Util.changeFragment(this, getActivity());

    }

    @Override
    public String getSubbedTopic() {
        final String WORKOUT_TOPIC = "Send/Calorie/Burn/Data";
        return WORKOUT_TOPIC;
    }



    public void startStopWatch() {
        handler.post(new Runnable() {
            @Override
            public void run() {

                String time = Util.formatHoursMinsSecs(workoutManager.getSecondsElapsed());
                timeElapsed.setText(time);

                if (workoutManager.getWorkoutHasStarted()) {
                    workoutManager.incrementSecondsElapsed();
                }
                else {
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
        caloriesProgressbar.setMax(GoalCalories);
        caloriesProgressbar.setProgress(caloriesBurntData,true);
        caloriesBurnt.setText(Integer.toString(caloriesBurntData));
    }

    public void showToday(){
        //make stats views invisible while past stats views visible
        adjustVisibility(View.VISIBLE,View.INVISIBLE);
        caloriesProgressbar.setMax(workoutManager.getCalorieGoal());
        caloriesProgressbar.setProgress(workoutManager.getCaloriesBurnt());
        caloriesBurnt.setText(Integer.toString(workoutManager.getCaloriesBurnt()));

    }
    public void addProfile(){
        if(user.getBitmap()!=null){
            removebackGroud();
            profilepicture.setBackground(getResources().getDrawable(R.drawable.bentconnersforworkout));
            profilepicture.setImageBitmap(user.getBitmap());
        }
        if(user.getImageUri()!=null){
            removebackGroud();
            profilepicture.setBackground(getResources().getDrawable(R.drawable.bentconnersforworkout));
            profilepicture.setImageURI(user.getImageUri());
        }

    }
    public void removebackGroud(){
        profilepicture.setBackground(null);
    }


}