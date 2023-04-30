package com.example.myapplication;

import android.os.Build;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.harrywhewell.scrolldatepicker.DayScrollDatePicker;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;

public class rework_workout extends Fragment {

    private View rootView;

    DayScrollDatePicker weeklyCalendar;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_rework_workout, container, false);
        weeklyCalendar = (DayScrollDatePicker) rootView.findViewById(R.id.day_date_picker);

        weeklyCalendar.getSelectedDate(date -> onDateSelected(date));

        return rootView;
    }

    public void onDateSelected(@Nullable Date date) {
        if(date != null){
            // do something with selected date
        }
    }

}