package com.example.myapplication;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class SettingsFragment extends Fragment {
    private View rootView;
    private Button saveButton;
    private EditText weightEditText;
    private EditText heightEditText;
    private EditText ageEditText;

    private Spinner genderSpinner;
    private TextView lifeTimeCurrency;
    private TextView currentBalance;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_settings, container, false);
        saveButton=rootView.findViewById(R.id.Save_Button);
        weightEditText = rootView.findViewById(R.id.kg_edittext);
        heightEditText = rootView.findViewById(R.id.height_edittext);
        ageEditText = rootView.findViewById(R.id.age_edit_text);

        /*
        weightEditText.setText(Float.toString(MainActivity.user.getWeight()));
        heightEditText.setText(Integer.toString(MainActivity.user.getAge()));
        ageEditText.setText(Integer.toString(MainActivity.user.getAge()));
        */

        saveButton.setOnClickListener(view -> publishSavedData());
        //TODO make gender take enum instead of string
        genderSpinner= rootView.findViewById(R.id.sex_spinner);
        String[] genders = {"Select", "Female", "Male", "Other"};
        ArrayAdapter<String> genderAdapter = new ArrayAdapter<>(
                this.getContext(),
                android.R.layout.simple_selectable_list_item,
                genders
        );

        genderSpinner.setAdapter(genderAdapter);
        genderSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(position > 0) {
                    Toast.makeText(rootView.getContext(), genders[position] + " Selected", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                Toast.makeText(rootView.getContext(), "Gender cannot be blank", Toast.LENGTH_SHORT).show();
            }
        });

        currentBalance = rootView.findViewById(R.id.current_balance_text_view);
        lifeTimeCurrency = rootView.findViewById(R.id.total_calories_burnt_text_view);
        //TODO use singleton
        currentBalance.setText(Integer.toString(MainActivity.user.getCalorieCredit()));
        lifeTimeCurrency.setText(Integer.toString(MainActivity.user.getLifeTimeCredit()));
        return rootView;
    }

    private void publishSavedData(){
        updateUserInfo();
        try {//todo use singleotn
            MainActivity.brokerConnection.getMqttClient().publish(MainActivity.brokerConnection.SETTINGS_CHANGE_TOPIC
            ,Util.toJSON(MainActivity.user)
                    ,MainActivity.brokerConnection.QOS,null);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

    }

    private void updateUserInfo(){
        //TODO exception handling
        MainActivity.user.setAge(Integer.parseInt(ageEditText.getText().toString()));
        MainActivity.user.setHeight(Float.parseFloat(heightEditText.getText().toString()));
        MainActivity.user.setWeight(Float.parseFloat(weightEditText.getText().toString()));

        MainActivity.user.setSex(genderSpinner.getSelectedItem().toString());


    }

}