package com.example.myapplication;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;


public class SettingsFragment extends Fragment {
    private View rootView;
    private Button saveButton;
    private EditText weightEditText;
    private EditText heightEditText;
    private EditText ageEditText;

    private Spinner genderSpinner;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_settings, container, false);
        saveButton=rootView.findViewById(R.id.Save_Button);
        weightEditText = rootView.findViewById(R.id.kg_edittext);
        heightEditText = rootView.findViewById(R.id.height_edittext);
        ageEditText = rootView.findViewById(R.id.age_edit_text);

        weightEditText.setText(Float.toString(MainActivity.user.getWeight()));
        ageEditText.setText(Integer.toString(MainActivity.user.getAge()));
        heightEditText.setText(Integer.toString(MainActivity.user.getAge()));

        saveButton.setOnClickListener(view -> publishSavedData());
        //TODO make gender take enum instead of string
        genderSpinner= rootView.findViewById(R.id.sex_spinner);
        List<String> genders = List.of("Female","Male");
        ArrayAdapter<String> genderAdapter = new ArrayAdapter<>(rootView.getContext(), androidx.appcompat.R.layout.support_simple_spinner_dropdown_item,genders);
        genderSpinner.setAdapter(genderAdapter);
        return rootView;
    }

    private void publishSavedData(){
        updateUserInfo();
        MainActivity.brokerConnection.getMqttClient().publish("User/Data/Change",MainActivity.user.toString()
                ,MainActivity.brokerConnection.QOS,null);

    }

    private void updateUserInfo(){
        //TODO exception handling
        MainActivity.user.setAge(Integer.parseInt(ageEditText.getText().toString()));
        MainActivity.user.setHeight(Float.parseFloat(heightEditText.getText().toString()));
        MainActivity.user.setWeight(Float.parseFloat(weightEditText.getText().toString()));
        MainActivity.user.setGender(genderSpinner.getSelectedItem().toString());
    }

}