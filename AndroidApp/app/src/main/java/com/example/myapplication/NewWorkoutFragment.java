package com.example.myapplication;

import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.io.File;


public class NewWorkoutFragment extends Fragment {





    private WorkoutType workoutType;

    private View rootView;


    private TextView targetCaloriesTextView;

    private WorkoutManager workoutManager;

    private TimePicker timePicker;

    //The following 6 methods are to maintain th lifecycle of the fragment by using the parent class method
    @Override
    public void onStart() {
        super.onStart();
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
        rootView= inflater.inflate(R.layout.fragment_new_workout, container, false);

        Button startWorkoutButton = rootView.findViewById(R.id.start_button);
        startWorkoutButton.setOnClickListener(view->startWorkout());

        targetCaloriesTextView = rootView.findViewById(R.id.current_calorie_goal);

        String managerPath = getActivity().getFilesDir().getPath() + "/workoutManager.json"; //data/user/0/myapplication/files
        File managerFile = new File(managerPath);
        workoutManager = WorkoutManager.getInstance(managerFile,getContext());

        targetCaloriesTextView.setText(Integer.toString(workoutManager.getCalorieGoal()));
        ImageButton incrementButton = rootView.findViewById(R.id.plus_calories_btn);
        ImageButton decrementButton = rootView.findViewById(R.id.minus_calories_btn);

        incrementButton.setOnClickListener(view -> changeCalorieGoal(workoutManager.getCHANGE_TARGET_CALORIES_AMOUNT()));
        decrementButton.setOnClickListener(view -> changeCalorieGoal(-workoutManager.getCHANGE_TARGET_CALORIES_AMOUNT()));

        timePicker = rootView.findViewById(R.id.timePicker);
        timePicker.setIs24HourView(true);
        timePicker.setHour(0);
        timePicker.setMinute(0);
        return rootView;
    }

    //here we set the calorie goal based on if the uer want sto burn more or less calories. If they want
    //to decrease the goal, then the parameter passed is negative
    public void changeCalorieGoal(int changeAmount){
        workoutManager.setCalorieGoal(Math.max(0,workoutManager.getCalorieGoal()+changeAmount));
        targetCaloriesTextView.setText(Integer.toString(workoutManager.getCalorieGoal()));
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void startWorkout(){
        //validate that time written is valid and the calorie goal are above 0
        boolean calorieGoalIsValid = workoutManager.getCalorieGoal()>0;
        boolean durationIsValid = timePicker.getHour()>0 || timePicker.getMinute()>0;
        if(workoutManager.getWorkoutHasStarted()){
            Toast.makeText(rootView.getContext(), "Please finish your ongoing workout before starting a new one", Toast.LENGTH_SHORT).show();

        }

        else if(!calorieGoalIsValid){
            Toast.makeText(rootView.getContext(), "Please set a calorie goal above 0", Toast.LENGTH_SHORT).show();
        }
        else if(!durationIsValid){
            Toast.makeText(rootView.getContext(), "Please set a valid time", Toast.LENGTH_SHORT).show();

        }

        else {
            //TODO use singleton
            workoutManager.setWorkoutHasStarted(true);
            //publish the exercise type and calorie goal
            workoutManager.setWorkoutTypeTerminalInt(workoutType.getName());
            final int SECONDS_IN_HOUR = 3600;
            final int SECONDS_IN_MINUTE = 60;
            int durationInSeconds = (timePicker.getHour()*SECONDS_IN_HOUR)+(timePicker.getMinute()*SECONDS_IN_MINUTE);
            workoutManager.setDurationInSeconds(durationInSeconds);
            workoutManager.setType(workoutType);

            try {
                String json = Util.objectToJSON(workoutManager);
                MainActivity.brokerConnection.getMqttClient().publish(MainActivity.brokerConnection.WORKOUT_STARTED_TOPIC,Util.objectToJSON(workoutManager),MainActivity.brokerConnection.QOS,null);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }

        }

    }

    public void setWorkoutType(int buttonPressedID){
        switch (buttonPressedID){
            case R.id.add_hiking_workout_btn:
                workoutType = WorkoutType.HIKING;
                break;
            case R.id.add_walking_workout_btn:
                workoutType = WorkoutType.WALKING;
                break;
            case R.id.add_running_workout_btn:
                workoutType = WorkoutType.RUNNING;

            default:
        }
    }


}