package com.example.myapplication;

import android.media.Image;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

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
    private BrokerConnection broker;
    private View rootView;
    private Spinner exerciseTypeSpinner;

    private EditText durationEditText;
    private Button startWorkoutButton;
    private int targetCalories;

    private TextView targetCaloriesTextView;

    private ImageButton incrementButton,decrementButton;

    private final int CHANGE_TARGET_CALORIES_AMOUNT=20;



    private WorkoutStartedListener workoutStartedListener;
    //This interface is a contract between this class to notify other classes that implement it that a message has arrived
    public interface WorkoutStartedListener{
        public void onWorkoutStarted(int calories,String duration);
    }

    //attach a class to listen to incoming messages with this setter
    public void setWorkoutStartedListener(WorkoutStartedListener workoutStartedListener){
        this.workoutStartedListener=workoutStartedListener;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView= inflater.inflate(R.layout.fragment_new_workout, container, false);
        exerciseTypeSpinner = rootView.findViewById(R.id.exercise_type_spinner);

        List<String> genders = List.of("Running","Walking","Cycling","Hiking");
        ArrayAdapter<String> exerciseAdapter = new ArrayAdapter<>(rootView.getContext(), androidx.appcompat.R.layout.support_simple_spinner_dropdown_item,genders);
        exerciseTypeSpinner.setAdapter(exerciseAdapter);
        broker = MainActivity.brokerConnection;
        durationEditText = rootView.findViewById(R.id.duration_edit_text);
        startWorkoutButton = rootView.findViewById(R.id.start_button);
        startWorkoutButton.setOnClickListener(view->startWorkout());

        targetCalories =0;
        targetCaloriesTextView = rootView.findViewById(R.id.user_calorie_goal);
        targetCaloriesTextView.setText(Integer.toString(targetCalories));
        incrementButton = rootView.findViewById(R.id.plus_calories_btn);
        decrementButton = rootView.findViewById(R.id.minus_calories_btn);

        incrementButton.setOnClickListener(view -> changeCalorieGoal(CHANGE_TARGET_CALORIES_AMOUNT));
        decrementButton.setOnClickListener(view -> changeCalorieGoal(-CHANGE_TARGET_CALORIES_AMOUNT));




        return rootView;
    }

    public void changeCalorieGoal(int changeAmount){
        targetCalories = Math.max(0,targetCalories+changeAmount);
        targetCaloriesTextView.setText(Integer.toString(targetCalories));
    }

    public void startWorkout(){
        String duration = durationEditText.getText().toString();
        boolean durationIsValid = validateDuration(duration);
        if(!durationIsValid){
            Toast.makeText(rootView.getContext(), "Please input a valid time Hours:Minutes:Seconds", Toast.LENGTH_SHORT).show();
        }
        else if(workoutStartedListener != null){
            //indicates that a workout has started and gives the calories and duration to the observer
            workoutStartedListener.onWorkoutStarted(targetCalories,duration);
        }
    }

    public boolean validateDuration(String time) {
        String[] separatedTime = time.split(":");
        if (separatedTime.length != 3) {
            return false;
        }
        for (int i = 0; i < separatedTime.length; i++) {
            final int HOURS = 24;
            final int MINUTES_OR_SECONDS = 60;
            int timeInput = Integer.parseInt(separatedTime[i]);
            if (i == 0 && (HOURS - timeInput >= 24 || HOURS - timeInput <= 0)) {
                return false;
            } else if (MINUTES_OR_SECONDS - timeInput > 60 ||  MINUTES_OR_SECONDS - timeInput < 0) {
                return false;

            }
        }
        return true;
    }
}