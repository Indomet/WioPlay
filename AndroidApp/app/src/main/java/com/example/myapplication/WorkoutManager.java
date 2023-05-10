
package com.example.myapplication;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.type.MapType;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import com.prolificinteractive.materialcalendarview.CalendarDay;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

//This class manages the workout. Updates the target goal, the calories they burn and keeps track of the time
public class WorkoutManager {

    Context context;
    private final int CHANGE_TARGET_CALORIES_AMOUNT=20;
    private boolean workoutHasStarted;
    private int caloriesBurnt;
    private int calorieGoal;
    private int secondsElapsed;
    private static WorkoutManager singleton;
    //this is to communciate with the terminal such that it knows the workout type
    private int workoutTypeTerminalInt;
    private int durationInSeconds;

    private HashMap<String,Integer> workoutsMap;
    private HashMap<String,FinishedWorkoutData> workoutDataHashMap;
    private int currentMonthlyWorkoutsProgress;
    private int totalWorkoutsCount;
    WorkoutType type;
    private File managerFile;


    private WorkoutManager(File managerFile,Context context) {
        this.defaultWorkoutManager();
        this.managerFile = managerFile;
        this.context=context;
        try {
            load();
        } catch (IllegalAccessException e) {
        }
    }

    private void load() throws IllegalAccessException{
        try {

            ObjectMapper mapper = new ObjectMapper();
            JsonNode node = mapper.readTree(this.managerFile);
            //mapper.configure(DeserializationFeature.USE_JAVA_ARRAY_FOR_JSON_ARRAY, true);
            //MapType hashMapType = mapper.getTypeFactory().constructMapType(HashMap.class, CalendarDay.class, FinishedWorkoutData.class);


            for(Field field : this.getClass().getDeclaredFields()){
                String name = field.getName();
                field.setAccessible(true);
                JsonNode jsonValue = node.get(name);
                if (jsonValue != null) {
                    Object value = mapper.convertValue(jsonValue, field.getType());
                    field.set(this, value);
                }
            }

            String jsonInput = node.get("workoutDataHashMap").toString();
            TypeReference<HashMap<String, FinishedWorkoutData>> typeRef
                    = new TypeReference<HashMap<String, FinishedWorkoutData>>() {};
            HashMap<String, FinishedWorkoutData> map = mapper.readValue(jsonInput, typeRef);
            workoutDataHashMap=map;
        } catch (IOException e) {
            Log.e("tag",e.getMessage());
            saveManagerData();
            load();

        }

    }

    private void saveManagerData() {
        ObjectMapper mapper = new ObjectMapper();
        ObjectWriter writer = mapper.writer();
        JsonNode node = mapper.valueToTree(this);
        try {
            writer.writeValue(this.managerFile, node);//works
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void defaultWorkoutManager() {
        secondsElapsed =0;
        currentMonthlyWorkoutsProgress=0;
        totalWorkoutsCount=0;
        calorieGoal=0;
        caloriesBurnt=0;
        workoutHasStarted = false;
        workoutTypeTerminalInt=0;
        durationInSeconds=0;
        workoutsMap = new HashMap<>();
        workoutDataHashMap  = new HashMap<>();
        workoutsMap.put("Walking",0);
        workoutsMap.put("Running",1);
        workoutsMap.put("Hiking",2);
    }


    //singleton design pattern as the user only needs 1 workout manager to manage the backend logic
    public static WorkoutManager getInstance(File managerFile,Context c){
        if(singleton==null){
            singleton=new WorkoutManager(managerFile,c);
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
    public void setDurationInSeconds(int seconds){
        durationInSeconds=seconds;
    }


    public boolean getWorkoutHasStarted() {
        return workoutHasStarted;
    }


    public void setWorkoutHasStarted(boolean workoutHasStarted) {
        this.workoutHasStarted = workoutHasStarted;
        saveManagerData();
    }


    public int getCalorieGoal() {
        return calorieGoal;
    }

    public void setCalorieGoal(int calorieGoal) {
        this.calorieGoal = calorieGoal;
        saveManagerData();
    }

    public void stopWorkout() {
        caloriesBurnt=0;
        workoutHasStarted = false;
        secondsElapsed = 0;
        calorieGoal=0;
        saveManagerData();
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

    public int getWorkoutTypeTerminalInt(){
        return workoutTypeTerminalInt;
    }

    public void setWorkoutTypeTerminalInt(String workoutType){
        this.workoutTypeTerminalInt=workoutsMap.get(workoutType);
        saveManagerData();
    }
    public int getDurationInSeconds(){
        return durationInSeconds;
    }



    public String calculateTimeLeft() {
        // calculate remaining calories to burn
        int caloriesLeftToBurn = calorieGoal - caloriesBurnt;
        // estimate current rate of calorie burn
        double caloriesBurntPerSecond = caloriesBurnt / (double)secondsElapsed;
        // estimate time needed to burn remaining calories based on current rate of calorie burn
        int secondsLeftToAchieveGoal = (int) (caloriesLeftToBurn / caloriesBurntPerSecond);
        // convert time to hours, minutes, seconds
        int hours = secondsLeftToAchieveGoal / 3600;
        int minutes = (secondsLeftToAchieveGoal % 3600) / 60;
        int seconds = secondsLeftToAchieveGoal % 60;
        String temp= String.format("%02d:%02d:%02d", hours, minutes, seconds);
        return temp;
    }
    public void setCaloriesBurnt(int caloriesBurnt){
        this.caloriesBurnt=caloriesBurnt;
        saveManagerData();
    }

    public void setType(WorkoutType type) {
        this.type = type;
        saveManagerData();
    }
    public WorkoutType getType() {
        return type;
    }

    public void addWorkoutData(FinishedWorkoutData workout,CalendarDay date){
        if(workoutDataHashMap.containsKey(date)){
            FinishedWorkoutData data = workoutDataHashMap.get(date);
            //add the previous calories and time
            data.setGoalCalories(workout.getGoalCalories()+data.getGoalCalories());
            data.setCaloriesBurntWithExercise(workout.getCaloriesBurntWithExercise()+data.getCaloriesBurntWithExercise());
            data.setDurationInSeconds(workout.getDurationInSeconds()+data.getDurationInSeconds());
            workoutDataHashMap.put(date.toString(),data);
        }
        else{
            workoutDataHashMap.put(date.toString(),workout);
        }
        saveManagerData();
    }

    public HashMap<String, FinishedWorkoutData> getWorkoutDataHashMap() {
        return workoutDataHashMap;
    }

    public int getSecondsElapsed() {
        return secondsElapsed;
    }

    public void setSecondsElapsed(int secondsElapsed) {
        this.secondsElapsed = secondsElapsed;
        saveManagerData();
    }
    // Getter for currentMonthlyWorkoutsProgress
    public int getCurrentMonthlyWorkoutsProgress() {
        return currentMonthlyWorkoutsProgress;
    }

    // Setter for currentMonthlyWorkoutsProgress
    public void setCurrentMonthlyWorkoutsProgress(int progress) {
        this.currentMonthlyWorkoutsProgress = progress;
        saveManagerData();
    }

    // Getter for totalWorkoutsCount
    public int getTotalWorkoutsCount() {
        return totalWorkoutsCount;
    }

    // Setter for totalWorkoutsCount
    public void setTotalWorkoutsCount(int count) {
        this.totalWorkoutsCount = count;
        saveManagerData();
    }


    public void incrementTotalWorkouts() {
        totalWorkoutsCount++;
        saveManagerData();
    }

    public void incrementMonthlyWorkouts() {
        currentMonthlyWorkoutsProgress++;
        saveManagerData();
    }







}
