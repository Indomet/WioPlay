package com.example.wioplay;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;

public class WorkoutFragment extends Fragment{

    private View rootView;
    private ProgressBar calorieProgressBar;

    private Button newWorkoutButton;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView= inflater.inflate(R.layout.fragment_workout, container, false);
        calorieProgressBar = rootView.findViewById(R.id.calorie_progress_bar);
        newWorkoutButton = rootView.findViewById(R.id.start_new_workout_button);
        newWorkoutButton.setOnClickListener(view -> changeToNewWorkoutFragment());
        return rootView;
    }




    public void changeToNewWorkoutFragment() {
        FragmentTransaction fm = getActivity().getSupportFragmentManager().beginTransaction();
        fm.replace(R.id.frameLayout,new NewWorkoutFragment()).setReorderingAllowed(true).addToBackStack(null).commit();
    }
}