package com.example.myapplication;

import android.util.Log;

import java.util.HashMap;

//This class manages the workout. Updates the target goal, the calories they burn and keeps track of the time
public class WorkoutManager {
    private final int CHANGE_TARGET_CALORIES_AMOUNT;
    private boolean workoutHasStarted;
    private int caloriesBurnt;
    private int calorieGoal;
    private int secondsElapsed;
    private static WorkoutManager singleton;
    private int workoutType;
    private int durationInSeconds;
    HashMap<String,Integer> workoutsMap;

    private WorkoutManager(){
        //The constructor sets the default values then they are changed according to the user input from the UI
        secondsElapsed =0;
        CHANGE_TARGET_CALORIES_AMOUNT=20;
        calorieGoal=0;
        caloriesBurnt=0;
        workoutHasStarted = false;
        workoutType=0;
        durationInSeconds=0;
        workoutsMap = new HashMap<>();
        workoutsMap.put("Walking",0);
        workoutsMap.put("Running",1);
        workoutsMap.put("Hiking",2);

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
    public boolean validateDuration(String time) {
         String pattern = "^(2[0-3]|[01]?[0-9]):([0-5]?[0-9])$";
            return time.matches(pattern);
    }

    public void setDurationInSeconds(int seconds){
        durationInSeconds=seconds;
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
    public void setCaloriesBurnt(int caloriesBurnt){
        this.caloriesBurnt=caloriesBurnt;
    }
    public int getWorkoutType(){
        return workoutType;
    }

    public void setWorkoutType(String workoutType){
        this.workoutType=workoutsMap.get(workoutType);
    }
    public int getDurationInSeconds(){
        return durationInSeconds;
    }

    public static int timeToSeconds(String time) {
        String hours = "";
        String mins = "";
        boolean hasReachedMins = false;
        for (int i = 0; i < time.length(); i++) {
            Character c = time.charAt(i);
            if (c == ':') {
                hasReachedMins = true;
            } else if (!hasReachedMins) {
                hours += c;
            } else {
                mins += c;
            }
        }
        int result = (Integer.parseInt(hours) * 3600) + (Integer.parseInt(mins) * 60);
        return result;
    }

    public String calculateTimeLeft(){
        //formula is  (timeTakenSoFar / caloriesBurntSoFar) * caloriesLeftToBurn= time left
        //first we get it into seconds then convert it into a string
    int secondsLeftToAchieveGoal = (secondsElapsed/caloriesBurnt) * (calorieGoal-caloriesBurnt);
    int hours = secondsLeftToAchieveGoal / 3600;
    int minutes = (secondsLeftToAchieveGoal % 3600) / 60;
    int seconds = secondsElapsed%60;
    String temp= String.format("%02d:%02d:%02d", hours, minutes,seconds);
        Log.d("tag","temp is "+temp);

        return temp;
    }

    }
