package com.example.wioplay;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;

public class WorkoutFragment extends Fragment implements View.OnClickListener{

    private View rootView;
    private ProgressBar calorieProgressBar;

    private Button newWorkoutButton,burnDownChartButton;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView= inflater.inflate(R.layout.fragment_workout, container, false);
        calorieProgressBar = rootView.findViewById(R.id.calorie_progress_bar);
        newWorkoutButton = rootView.findViewById(R.id.start_new_workout_button);
        burnDownChartButton = rootView.findViewById(R.id.burn_down_chart_button);
        return rootView;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.start_new_workout_button://TODO
            break;
            case R.id.burn_down_chart_button://todo
            break;
        }
    }
}