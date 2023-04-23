package com.example.myapplication;

import android.os.Build;

import androidx.annotation.RequiresApi;

import java.time.LocalTime;
import java.time.format.DateTimeParseException;

//This class manages the workout. Updates the target goal, the calories they burn and keeps track of the time
public class WorkoutManager {
    private final int CHANGE_TARGET_CALORIES_AMOUNT;
    private boolean workoutHasStarted;
    private int caloriesBurnt;
    private int calorieGoal;
    private int secondsElapsed;
    private static WorkoutManager singleton;

    private WorkoutManager(){
        //The constructor sets the default values then they are changed according to the user input from the UI
        secondsElapsed =0;
        CHANGE_TARGET_CALORIES_AMOUNT=20;
        calorieGoal=0;
        caloriesBurnt=0;
        workoutHasStarted = false;

    }
    //singleton design pattern as the user only needs 1 workout manager to manage the backend logic
    public static WorkoutManager getInstance() {
        if (singleton == null) {
            singleton = new WorkoutManager();
        }
        return singleton;
    }

 // Calculates the elapsed time in hours, minutes, and seconds
 // returns a formatted string of the elapsed time in the format HH:MM:SS
    public String calculateTimeElapsed(){

        int hours = secondsElapsed / 3600;
        int minutes = (secondsElapsed % 3600) / 60;
        int secs = secondsElapsed % 60;

        return String.format("%02d:%02d:%02d", hours, minutes, secs);
    }

    //validates the time using the LocalTime import
    @RequiresApi(api = Build.VERSION_CODES.O)
    public boolean validateDuration(String time) {

        try {
            LocalTime.parse(time);
        } catch (DateTimeParseException | NullPointerException e) {
            return false;
        }
        return true;
    }


    public boolean getWorkoutHasStarted() {
        return workoutHasStarted;
    }


    public void setWorkoutHasStarted(boolean workoutHasStarted) {
        this.workoutHasStarted = workoutHasStarted;
    }


    public int getCalorieGoal() {
        return calorieGoal;
    }

    public void setCalorieGoal(int calorieGoal) {
        this.calorieGoal = calorieGoal;
    }

    public void stopWorkout() {
        caloriesBurnt=0;
        workoutHasStarted = false;
        secondsElapsed = 0;
        calorieGoal=0;
    }

    public void incrementSecondsElapsed() {
        secondsElapsed++;
    }

    public boolean isGoalAchieved() {
        return caloriesBurnt >= calorieGoal;
    }
    public int getCHANGE_TARGET_CALORIES_AMOUNT(){
        return  CHANGE_TARGET_CALORIES_AMOUNT;
    }

    public int getCaloriesBurnt(){
        return caloriesBurnt;
    }
    public void addToCaloriesBurnt(int caloriesToAdd){
        caloriesBurnt+=caloriesToAdd;
    }



    //potentially a useful function to display statistics
    /*public int getRemainingCalories(int caloriesBurned) {
        int remainingCalories = calorieGoal - caloriesBurned;
        if (remainingCalories < 0) {
            return 0;
        }
        return remainingCalories;
    }*/

    }
