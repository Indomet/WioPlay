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
import android.widget.Spinner;
import android.widget.Toast;

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


        return rootView;
    }
    private int changeToString(String sentance){
         int number = Integer.parseInt(sentance);
         return number;
    }
    // this is for getting the same user


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