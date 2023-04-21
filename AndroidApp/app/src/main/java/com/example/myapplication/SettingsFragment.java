package com.example.myapplication;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
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


        


         // I will need to talk to Ali about this method, If they really need to be there.
        /*
        {
        //weightEditText.setText(Float.toString(MainActivity.user.getWeight()));

        //ageEditText.setText(Integer.toString(MainActivity.user.getAge()));
        //heightEditText.setText(Integer.toString(MainActivity.user.getAge()));
         }
         */

        saveButton.setOnClickListener(view -> publishSavedData());
        //TODO make gender take enum instead of string
        genderSpinner= rootView.findViewById(R.id.sex_spinner);
        List<String> genders = List.of("Female","Male");
        ArrayAdapter<String> genderAdapter = new ArrayAdapter<>(rootView.getContext(), androidx.appcompat.R.layout.support_simple_spinner_dropdown_item,genders);
        genderSpinner.setAdapter(genderAdapter);
        return rootView;
    }
    private int changeToString(String sentance){
         int number = Integer.parseInt(sentance);
         return number;
    }
    // this is for getting the same user


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

    public void changescreen(){

        SettingsFragment settingsFragment = new SettingsFragment();

        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout,settingsFragment).setReorderingAllowed(true).addToBackStack(null).commit();
    


    }
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        weightEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // This method is called to notify that the text is about to change
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
               String currentnumberString=weightEditText.getText().toString();
               if(!currentnumberString.isEmpty()){
                   int currentNumber= changeToString(currentnumberString);
                   if(currentNumber<0){
                       Toast.makeText(getActivity(),"the value can not be negative",Toast.LENGTH_LONG).show();
                   } else if (currentNumber>650){
                       changescreen();
                       Toast.makeText(getActivity(),"value can not be over 650",Toast.LENGTH_LONG).show();
                   }
               }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }

        });

        heightEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String currentnumberString=heightEditText.getText().toString();
                if(!currentnumberString.isEmpty()) {
                    int currentNumber = changeToString(currentnumberString);
                    if (currentNumber < 0) {
                        Toast.makeText(getActivity(), "the value can not be negative", Toast.LENGTH_LONG).show();
                    } else if (currentNumber > 300) {
                        changescreen();
                        Toast.makeText(getActivity(), "value can not be over 300", Toast.LENGTH_LONG).show();
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }

        });

        ageEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
              String currentnumberString=ageEditText.getText().toString();
              if(!currentnumberString.isEmpty()) {
                  int currentNumber = changeToString(currentnumberString);
                  if (currentNumber < 0) {
                      Toast.makeText(getActivity(), "the value can not be negative", Toast.LENGTH_LONG).show();
                  } else if (currentNumber > 150) {
                      changescreen();
                      Toast.makeText(getActivity(), "value can not be over 150", Toast.LENGTH_LONG).show();
                  }
              }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

    }
}