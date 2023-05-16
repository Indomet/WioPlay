
package com.example.myapplication;
import android.util.Log;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.HashMap;

//This class manages the workout. Updates the target goal, the calories they burn and keeps track of the time
public class WorkoutManager {

    private boolean workoutHasStarted;
    private int caloriesBurnt;
    private int calorieGoal;
    private int secondsElapsed;
    private static WorkoutManager singleton;
    //this is to communicate with the terminal such that it knows the workout type
    private int workoutTypeTerminalInt;
    private int durationInSeconds;

    //This hashmap is used to create final variables that the arduino uses to calculate calories
    private HashMap<String,Integer> workoutsMap;
    //This hashmap is used to store the data when a workout is done to be displayed in the calendar
    private HashMap<String,FinishedWorkoutData> workoutDataHashMap;
    private int currentMonthlyWorkoutsProgress;
    private int totalWorkoutsCount;
    //This enum is used for the arduino calorie calculations
    private WorkoutType type;
    private final File managerFile;


    private WorkoutManager(File managerFile) {
        this.defaultWorkoutManager();
        this.managerFile = managerFile;
        try {
            load();
        } catch (IllegalAccessException e) {
            Log.e("IllegalAccessException", "Cannot access workout manager file");
        }
    }

    private void load() throws IllegalAccessException{
        try {
            //reads the json node and saves all attributes according to the data in the file
            ObjectMapper mapper = new ObjectMapper();
            JsonNode node = mapper.readTree(this.managerFile);
            Util.loadFields(this,node,mapper);
            //the data hash map is a special case that needs to be treated differently
            final String dataHashMapPointerName = "workoutDataHashMap";
            String jsonInput = node.get(dataHashMapPointerName).toString();
            TypeReference<HashMap<String, FinishedWorkoutData>> typeRef = new TypeReference<>() {};
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
            writer.writeValue(this.managerFile, node);
        } catch (IOException e) {
            Log.e("saving failure","File couldn't be saved");
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
        workoutsMap.put(WorkoutType.WALKING.getName(), 0);
        workoutsMap.put(WorkoutType.RUNNING.getName(),1);
        workoutsMap.put(WorkoutType.HIKING.getName(),2);
    }


    //singleton design pattern as the user only needs 1 workout manager to manage the backend logic
    public static WorkoutManager getInstance(){
        if(singleton==null){
            throw new NullPointerException();
        }
        return singleton;
    }

    public static WorkoutManager initialize(File managerFile){
        if(singleton==null){
            singleton=new WorkoutManager(managerFile);
        }
        return singleton;
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
        //final variable that declares how much the target calories the user
        int CHANGE_TARGET_CALORIES_AMOUNT = 20;
        return CHANGE_TARGET_CALORIES_AMOUNT;
    }

    public int getCaloriesBurnt(){
        return caloriesBurnt;
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
        return Util.formatHoursMinsSecs(secondsLeftToAchieveGoal);

    }
    // Calculates the elapsed time in hours, minutes, and seconds
    // returns a formatted string of the elapsed time in the format HH:MM:SS

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
        if(workoutDataHashMap.containsKey(date.toString())){
            FinishedWorkoutData data = workoutDataHashMap.get(date.toString());
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


    public void incrementTotalWorkouts() {
        totalWorkoutsCount++;
        saveManagerData();
    }

    public void incrementMonthlyWorkouts() {
        currentMonthlyWorkoutsProgress++;
        saveManagerData();
    }

}
