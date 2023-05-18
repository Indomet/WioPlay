package com.example.myapplication;

import android.graphics.Bitmap;
import android.net.Uri;
import android.graphics.Bitmap;
import android.util.Log;

import androidx.constraintlayout.utils.widget.ImageFilterView;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;

import java.io.File;
import java.io.IOException;

public class User {
    private String username;
    private int age;
    private float height;

    private Bitmap bitmap;

    private Uri imageUri;
    private float weight;
    private String sex;
    private int calorieCredit;
    private File userFile;

    private static User instance;
    private int monthlyWorkouts;

    private User(File userFile) {
        this.defaultUser();
        this.userFile = userFile;
        load();
    }


    public static User getInstance(){
        if(instance == null){
            throw new NullPointerException();
        }
        return instance;
    }
    public static User initialize(File userFile){
        if(instance == null){
            instance = new User(userFile);
        }
        return instance;
    }

    private void defaultUser() {
        this.age = 0;
        this.height = 0;
        this.weight = 0;
        this.username = "username";
        this.calorieCredit = 500; //A new user starts with 500 CalorieCurrency
        this.monthlyWorkouts=30; //30 is the default number, so a workout per day per month
        bitmap=null;
        imageUri=null;
    }

    public void setUsername(String username) {
        this.username = username;
        saveUserData();
    }

    public void setAge(int age) {
        this.age = age;
        saveUserData();
    }


    public void setHeight(float height) {
        this.height = height;
        saveUserData();
    }

    public float getHeight() {
        return height;
    }

    public void setWeight(float weight) {
        this.weight = weight;
        saveUserData();
    }
    public void setBitmap(Bitmap bitmap){
        this.bitmap=bitmap;
    }
    public Bitmap getBitmap(){
        return this.bitmap;
    }
    public void setImageUri (Uri imageUri){
        this.imageUri=imageUri;
    }
    public Uri getImageUri(){
        return this.imageUri;
    }



    public void setSex(String sex) {
        this.sex = sex;
        saveUserData();
    }

    public void updateCredit(float calorie) {
        //calorie can be positive when gaining, and negative when spending.
        int diff = Math.round(calorie);
        this.calorieCredit += diff;
        saveUserData();

    }

    public int getCalorieCredit() {
        return this.calorieCredit;
    }


    public String toString() {
        return age + "," + height + "," + weight;
    }

    private void load() {

        try {

            ObjectMapper mapper = new ObjectMapper();
            JsonNode node = mapper.readTree(this.userFile);
            Util.loadFields(this,node,mapper);
        } catch (IOException e) {

            saveUserData();
            load();

        } catch (IllegalAccessException e) {
            Log.e("IllegalAccessException",e.getMessage());
        }

    }

    public void saveUserData() {
        ObjectMapper mapper = new ObjectMapper();
        ObjectWriter writer = mapper.writer();
        JsonNode node = mapper.valueToTree(this);
        try {
            writer.writeValue(this.userFile, node);//works
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public String getUsername() {
        return username;
    }


    public void setMonthlyWorkouts(int monthylWorkoutsCount) {
        this.monthlyWorkouts=monthylWorkoutsCount;
        saveUserData();
    }

    public int getMonthlyWorkouts() {
        return monthlyWorkouts;

    }

    public File getUserFile() {
        return userFile;
    }


}
