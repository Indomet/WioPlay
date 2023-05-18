package com.example.myapplication;
import static android.app.Activity.RESULT_OK;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;

import androidx.constraintlayout.utils.widget.ImageFilterView;
import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.security.PrivateKey;
import java.util.ArrayList;
import java.util.List;

public class SettingsFragment extends Fragment {
    private static final int PICK_IMAGE_REQUEST = 1;
    private static final int TAKE_PICTURE_REQUEST = 2;
    private View rootView;
    private Button saveButton;
    private EditText weightEditText;
    private EditText heightEditText;
    private EditText ageEditText;
    private EditText monthlyWorkouts;
    private Spinner sexSpinner;

    private Dialog dialog;

    private ImageButton changeProfile;
    private ImageFilterView profilePicture;


    private Button editButton;
    private TextView usernameTextView;
    private User user;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_settings, container, false);
        user = User.getInstance();
        widgetInit();


        return rootView;
    }

    public void widgetInit(){

        saveButton = rootView.findViewById(R.id.Save_Button);
        weightEditText = rootView.findViewById(R.id.kg_edittext);
        heightEditText = rootView.findViewById(R.id.height_edittext);
        ageEditText = rootView.findViewById(R.id.age_edit_text);
        editButton=rootView.findViewById(R.id.edit_username_btn);
        profilePicture=rootView.findViewById(R.id.user_profile_pic_settings);
        usernameTextView = rootView.findViewById(R.id.settings_username_textview);
        changeProfile=rootView.findViewById(R.id.chengeProfile);
        monthlyWorkouts = rootView.findViewById(R.id.monthly_workouts_edittxt);
        usernameTextView.setText(user.getUsername());
        //profilePicture = user.profilePic;
        saveButton.setOnClickListener(view -> publishSavedData());

        editButton.setOnClickListener(v -> editUserNamePopup());
        ImageButton sexInfoButton = rootView.findViewById(R.id.sex_info_btn);
        sexInfoButton.setOnClickListener(view -> showSexInfoPopup());
        changeProfile.setOnClickListener(v -> addPictureToTheBackground());

        checkUserprofile();

        spinnerInit();
    }

    public void spinnerInit(){
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
    public void checkUserprofile(){
        if(user.getBitmap()!=null){
            profilePicture.setImageBitmap(user.getBitmap());
        }
        if(user.getImageUri()!=null){
            profilePicture.setImageURI(user.getImageUri());
        }
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

        try {
        MainActivity.brokerConnection.getMqttClient().publish(MainActivity.brokerConnection.SETTINGS_CHANGE_TOPIC
         ,Util.objectToJSON(user)
         ,MainActivity.brokerConnection.QOS, null);
         } catch (IllegalAccessException e) {
         e.printStackTrace();
        }
    }
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
            checkTheRange(0, 250, height);
            checkTheRange(0,150,age);
            checkTheRange(0,100,monthylWorkoutsCount);

            user.setAge(age);
            user.setHeight(height);
            user.setWeight(weight);
            user.setMonthlyWorkouts(monthylWorkoutsCount);
            user.setSex(sexSpinner.getSelectedItem().toString());
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
    public void addPictureToTheBackground(){
        dialog = new Dialog(getActivity()); // creating a dialog
        dialog.setContentView(R.layout.editpicture); // set context to a dialog
        ImageView galleryImageview =dialog.findViewById(R.id.galleryImageview);
        ImageView cameraImageView= dialog.findViewById(R.id.cameraImageView);
        TextView pictureClosebtn = dialog.findViewById(R.id.pictureClosebtn);
        galleryImageview.setOnClickListener(v->Galleryscource());
        cameraImageView.setOnClickListener(v->cameracource());
        pictureClosebtn.setOnClickListener(v -> dialog.dismiss());
        dialog.show();
    }

    public void Galleryscource(){
        // getting an image from gallery
        Intent galleryPicture = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(galleryPicture,PICK_IMAGE_REQUEST);


    }
    public void cameracource(){
        // getting an image from camera
        Intent cameraPicture= new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(cameraPicture,TAKE_PICTURE_REQUEST);
    }


    // result of the request is recieved here
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri imageUri = data.getData();
            profilePicture.setImageURI(imageUri);// adding the image from gallery to the imageview
            user.setImageUri(imageUri);
            user.setBitmap(null);
            dialog.dismiss();// close the dialod


        }else if(requestCode==TAKE_PICTURE_REQUEST && resultCode== RESULT_OK ){
            assert data != null;
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");// creating a bitmap from the image from camera
            profilePicture.setImageBitmap(imageBitmap); //adding the camera from gallery to the imageview
            user.setBitmap(imageBitmap);
            user.setImageUri(null);
            dialog.dismiss();// close the dialod
        }
    }

}



