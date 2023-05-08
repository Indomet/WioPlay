package com.example.myapplication;
import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.sax.RootElement;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import java.io.File;
import java.io.IOException;

public class SettingsFragment extends Fragment {
    private View rootView;
    private Button saveButton;
    private EditText weightEditText;
    private EditText heightEditText;
    private EditText ageEditText;
    private EditText monthlyWorkouts;
    private Spinner sexSpinner;

    private Button editButton;
    private TextView usernameTextView;
    private User user;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_settings, container, false);
        saveButton = rootView.findViewById(R.id.Save_Button);
        weightEditText = rootView.findViewById(R.id.kg_edittext);
        heightEditText = rootView.findViewById(R.id.height_edittext);
        ageEditText = rootView.findViewById(R.id.age_edit_text);
        editButton=rootView.findViewById(R.id.edit_username_btn);
        usernameTextView = rootView.findViewById(R.id.settings_username_textview);
        monthlyWorkouts = rootView.findViewById(R.id.monthly_workouts_edittxt);
        String userPath = getActivity().getFilesDir().getPath() + "/user.json"; //data/user/0/myapplication/files
        File userFile = new File(userPath);

        user = User.getInstance(userFile);
        usernameTextView.setText(user.getUsername());
        /*
        weightEditText.setText(Float.toString(MainActivity.user.getWeight()));
        heightEditText.setText(Integer.toString(MainActivity.user.getAge()));
        ageEditText.setText(Integer.toString(MainActivity.user.getAge()));
        */

        saveButton.setOnClickListener(new View.OnClickListener() {
            String filePath = rootView.getContext().getFilesDir().getPath() + "/user.json"; //data/user/0/myapplication/files
            File userFile = new File(filePath);
            @Override
            public void onClick(View v) {
                try {
                    updateUserInfo();
                } catch (Exception e) {
                    String message= e.getMessage();
                    Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
                }
            }
        });

        editButton.setOnClickListener(v -> editUserNamePopup());

                //TODO make sex take enum instead of string
                sexSpinner = rootView.findViewById(R.id.sex_spinner);
        String[] sex = {"Select", "Female", "Male"};
        ArrayAdapter<String> sexAdapter = new ArrayAdapter<>(
                this.getContext(),
                android.R.layout.simple_selectable_list_item,
                sex
        );

        sexSpinner.setAdapter(sexAdapter);
        sexSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position > 0) {
                    Toast.makeText(rootView.getContext(), sex[position] + " Selected", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                Toast.makeText(rootView.getContext(), "Sex cannot be blank", Toast.LENGTH_SHORT).show();
            }
        });




        ImageButton sexInfoButton = rootView.findViewById(R.id.sex_info_btn);
        sexInfoButton.setOnClickListener(view -> showSexInfoPopup());


        return rootView;
    }
    public void editUserNamePopup(){
        Dialog dialog= new Dialog(getActivity());
        dialog.setContentView(R.layout.popupeditusername);
        TextView button=dialog.findViewById(R.id.closebtn);
        button.setOnClickListener(view -> dialog.dismiss());
        EditText editfield = dialog.findViewById(R.id.Editusername);
        Button save_btn = dialog.findViewById(R.id.popup_save_btn);
        save_btn.setOnClickListener(view -> updateUsername(editfield.getText().toString(),dialog));
        dialog.show();
    }

    private void updateUsername(String name, Dialog dialog) {

        user.setUsername(name);
        usernameTextView.setText(name);
        dialog.dismiss();
    }


    private void showSexInfoPopup() {

        Dialog popUpWindow = new Dialog(getActivity());
        popUpWindow.setContentView(R.layout.sex_info_popup);
        popUpWindow.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        //setting the ok button to remove the popup when the user clicks on it
        Button button = popUpWindow.findViewById(R.id.sex_info_ok_btn);
        button.setOnClickListener(view -> popUpWindow.dismiss());
        popUpWindow.show();
    }

    private void publishSavedData() {
        try {
            updateUserInfo();
        } catch (Exception e) {
            String message= e.getMessage();
            Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
        }

        try {//todo use singleton
        MainActivity.brokerConnection.getMqttClient().publish(MainActivity.brokerConnection.SETTINGS_CHANGE_TOPIC
         ,Util.objectToJSON(MainActivity.user)
         ,MainActivity.brokerConnection.QOS, null);
         } catch (IllegalAccessException e) {
         e.printStackTrace();
        }
    }
//TODO: READ ONLY??? SO READ FIRST THEN WRITE TO /data/user/0/com.example.myapplication/files/user.json

    private void updateUserInfo() throws Exception {


        // checking all editfields on the screens are filled
        if (!ageEditText.getText().toString().isEmpty() &
                !heightEditText.getText().toString().isEmpty() &
                !weightEditText.getText().toString().isEmpty() &
                !monthlyWorkouts.getText().toString().isEmpty()) {


            int age = Integer.parseInt(ageEditText.getText().toString());
            float height = Float.parseFloat(heightEditText.getText().toString());
            float weight = Float.parseFloat((weightEditText.getText().toString()));
            int monthylWorkoutsCount = Integer.parseInt((monthlyWorkouts.getText().toString()));
// checking if all entered numbers are within specific ranges
            checkTheRange(0, 450, weight);
            checkTheRange(0, 120, height);
            checkTheRange(0,150,age);
            checkTheRange(0,100,monthylWorkoutsCount);

            MainActivity.user.setAge(age);
            MainActivity.user.setHeight(height);
            MainActivity.user.setWeight(weight);
            MainActivity.user.setMonthlyWorkouts(monthylWorkoutsCount);
            MainActivity.user.setSex(sexSpinner.getSelectedItem().toString());
    }

            // tobe thrown if any of the Editfield is not filled
         else {
            throw new Exception("Fill in all fields");
        }

    }


    public void checkTheRange(float from, float to, float value) throws Exception {

        View fragmentView = super.getView(); // getting the rootView
        List <EditText>  listOfEditViews = new ArrayList<>(); // A List that stores EditTexts

        if(fragmentView != null){ // Avoiding a nullPointer Exception
            AllEditView(fragmentView,listOfEditViews); // This method adds EditTexts to the list
        }
        for(EditText editText: listOfEditViews){ // this loops through all EditText
            if(Float.parseFloat(editText.getText().toString())==value){ // checking if we have collect Edittext
                if(value < from ){
                    editText.setText("");
                    throw new Exception("the value can not be bellow"+ " "+String.valueOf(from));
                }else if(value > to){
                    editText.setText("");
                    throw new Exception("the value can not be over"+ " "+String.valueOf(to));
                }
            }
        }
    }
    public void AllEditView ( View view, List<EditText> listOfEditViews){
        if(view instanceof EditText){ // checking if the view is type EditText View before adding to the list
            listOfEditViews.add((EditText) view);
        } else if(view instanceof ViewGroup){ // checking if it is a view container
            ViewGroup viewGroup= (ViewGroup) view; // casting to view container
            for(int i =0; i< viewGroup.getChildCount();i++){ // getting every view i the container
                View innerView= viewGroup.getChildAt(i);
                AllEditView(innerView,listOfEditViews);// recursive call
            }

        }
    }

}


