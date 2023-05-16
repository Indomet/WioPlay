package com.example.myapplication;

public class FinishedWorkoutData {
    private int durationInSeconds;
    private int caloriesBurntWithExercise;
    private int goalCalories;
    private WorkoutType type;

    FinishedWorkoutData(int durationInSeconds,int caloriesBurntWithExercise,WorkoutType type,int goalCalories){
            this.type=type;
            this.caloriesBurntWithExercise=caloriesBurntWithExercise;
            this.durationInSeconds=durationInSeconds;
            this.goalCalories=goalCalories;
    }
    //Empty constructor is needed such that the object can serialize upon reading from the file
    public FinishedWorkoutData() {}

    public int getDurationInSeconds() {
        return durationInSeconds;
    }

    public int getGoalCalories() {
        return goalCalories;
    }

    public void setGoalCalories(int goalCalories) {
        this.goalCalories = goalCalories;
    }

    public void setDurationInSeconds(int durationInSeconds) {
        this.durationInSeconds = durationInSeconds;
    }

    public int getCaloriesBurntWithExercise() {
        return caloriesBurntWithExercise;
    }

    public void setCaloriesBurntWithExercise(int caloriesBurntWithExercise) {
        this.caloriesBurntWithExercise = caloriesBurntWithExercise;
    }

    public WorkoutType getType() {
        return type;
    }

    public void setType(WorkoutType type) {
        this.type = type;
    }
}
