package com.example.myapplication;

import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;


public class NewWorkoutFragment extends Fragment {
    private View rootView;

    private EditText durationEditText;

    private TextView targetCaloriesTextView;

    private WorkoutManager workoutManager;
    Spinner exerciseTypeSpinner;

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
        exerciseTypeSpinner = rootView.findViewById(R.id.exercise_type_spinner);

        List<String> genders = List.of("Running","Walking","Hiking");
        ArrayAdapter<String> exerciseAdapter = new ArrayAdapter<>(rootView.getContext(), androidx.appcompat.R.layout.support_simple_spinner_dropdown_item,genders);
        exerciseTypeSpinner.setAdapter(exerciseAdapter);
        durationEditText = rootView.findViewById(R.id.duration_edit_text);
        Button startWorkoutButton = rootView.findViewById(R.id.start_button);
        startWorkoutButton.setOnClickListener(view->startWorkout());

        targetCaloriesTextView = rootView.findViewById(R.id.user_calorie_goal);

        workoutManager = WorkoutManager.getInstance();

        targetCaloriesTextView.setText(Integer.toString(workoutManager.getCalorieGoal()));
        ImageButton incrementButton = rootView.findViewById(R.id.plus_calories_btn);
        ImageButton decrementButton = rootView.findViewById(R.id.minus_calories_btn);

        incrementButton.setOnClickListener(view -> changeCalorieGoal(workoutManager.getCHANGE_TARGET_CALORIES_AMOUNT()));
        decrementButton.setOnClickListener(view -> changeCalorieGoal(-workoutManager.getCHANGE_TARGET_CALORIES_AMOUNT()));

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
        String duration = durationEditText.getText().toString();
        boolean durationIsValid = workoutManager.validateDuration(duration);
        boolean calorieGoalIsValid = workoutManager.getCalorieGoal()>0;

        if(!durationIsValid){
            Toast.makeText(rootView.getContext(), "Please input a valid time Hours:Minutes", Toast.LENGTH_SHORT).show(); //Shouldn't this be Minutes:Seconds?
        }
        else if(!calorieGoalIsValid){
            Toast.makeText(rootView.getContext(), "Please set a calorie goal above 0", Toast.LENGTH_SHORT).show();
        }

        else {
            //TODO use singleton
            workoutManager.setWorkoutHasStarted(true);
            String workoutType = exerciseTypeSpinner.getSelectedItem().toString();
            //publish the exercise type, calorie goal and duration
            workoutManager.setWorkoutType(workoutType);
            workoutManager.setDurationInSeconds(workoutManager.timeToSeconds(duration));
            Log.d("tag","duration in sec is "+workoutManager.getDurationInSeconds());
            try {
                String json = Util.objectToJSON(workoutManager);
                Toast.makeText(rootView.getContext(), json, Toast.LENGTH_SHORT).show();
                MainActivity.brokerConnection.getMqttClient().publish(MainActivity.brokerConnection.WORKOUT_STARTED_TOPIC,Util.objectToJSON(workoutManager),MainActivity.brokerConnection.QOS,null);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }

        }

    }


}