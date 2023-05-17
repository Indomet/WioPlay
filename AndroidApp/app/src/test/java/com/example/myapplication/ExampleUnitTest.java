package com.example.myapplication;

import org.junit.Test;

import static org.junit.Assert.*;

import android.content.Context;
import android.os.Environment;


import com.prolificinteractive.materialcalendarview.CalendarDay;

import java.io.File;


/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
  //  @Test
   // public void addition_isCorrect() {
    //    assertEquals(4, 2 + 2);
  //  }
/*
    @Test
    public void workoutManagerIsAddingWorkoutsHistoryCorrectly(){
        FinishedWorkoutData first = new FinishedWorkoutData(500,100,WorkoutType.RUNNING,100);
        CalendarDay day = CalendarDay.from(2023,5,6);
        FinishedWorkoutData second = new FinishedWorkoutData(700,200,WorkoutType.RUNNING,200);
        String downloadsDir = Environment.getExternalStorageDirectory() + "/" + Environment.DIRECTORY_DOCUMENTS;
        String managerPath = downloadsDir + "/workoutManager.json";
        File managerFile = new File(managerPath);
        WorkoutManager manager = WorkoutManager.initialize(managerFile);
        manager.addWorkoutData(first,day);
        manager.addWorkoutData(second,day);
        FinishedWorkoutData result = manager.getWorkoutDataHashMap().get(day);
        assertEquals(1200,result.getDurationInSeconds());
        assertEquals(300,result.getCaloriesBurntWithExercise());
        assertEquals(300,result.getGoalCalories());
    }

    @Test
    public void workoutManagerIsAddingWorkoutsHistoryCorrectly() {
        FinishedWorkoutData first = new FinishedWorkoutData(500,100,WorkoutType.RUNNING,100);
        CalendarDay day = CalendarDay.from(2023,5,6);
        FinishedWorkoutData second = new FinishedWorkoutData(700,200,WorkoutType.RUNNING,200);

        WorkoutManager manager = WorkoutManager.getInstance();
        manager.addWorkoutData(first,day);
        manager.addWorkoutData(second,day);
        FinishedWorkoutData result = manager.getWorkoutDataHashMap().get(day);
        assertEquals(1200,result.getDurationInSeconds());
        assertEquals(300,result.getCaloriesBurntWithExercise());
        assertEquals(300,result.getGoalCalories());
    }

    @Test
    public void workoutManagercalculateTimeElapsedWorks() {
        WorkoutManager manager = WorkoutManager.getInstance();
        //should be 1 hour 20 mins
        int seconds = 4800;
        manager.setSecondsElapsed(seconds);
        String formattedTime = manager.calculateTimeElapsed();
        assertEquals("01:20:00",formattedTime);
    }

 */



}