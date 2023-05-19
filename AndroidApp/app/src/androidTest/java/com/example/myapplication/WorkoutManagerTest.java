package com.example.myapplication;

import android.content.Context;
import android.os.Environment;

import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.*;

import com.prolificinteractive.materialcalendarview.CalendarDay;

import java.io.File;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class WorkoutManagerTest {
    WorkoutManager manager;
    @Before
    public void setup(){
        String downloadsDir = Environment.getExternalStorageDirectory() + "/" + Environment.DIRECTORY_DOCUMENTS;
        String managerPath = downloadsDir + "/workoutManager.json";
        File managerFile = new File(managerPath);
        //Delete the file
        if (managerFile.exists()) {
            managerFile.delete();
        }
        manager = WorkoutManager.initialize(managerFile);
    }
    @Test
    public void useAppContext() {
        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
        assertEquals("com.example.myapplication", appContext.getPackageName());




    }

    @Test
    public void workoutManagerIsAddingWorkoutsHistoryCorrectly(){
        FinishedWorkoutData first = new FinishedWorkoutData(500,100,WorkoutType.RUNNING,100);
        CalendarDay day = CalendarDay.from(2022,1,1);
        FinishedWorkoutData second = new FinishedWorkoutData(700,200,WorkoutType.RUNNING,200);
        manager.addWorkoutData(first,day);
        manager.addWorkoutData(second,day);
        FinishedWorkoutData result = manager.getWorkoutDataHashMap().get(day.toString());
        assertEquals(1200,result.getDurationInSeconds());
        assertEquals(300,result.getCaloriesBurntWithExercise());
        assertEquals(300,result.getGoalCalories());
    }
    @Test
    public void workoutManagercalculateTimeElapsedWorks() {
        WorkoutManager manager = WorkoutManager.getInstance();
        //should be 1 hour 20 mins
        int seconds = 4800;
        String formattedTime = Util.formatHoursMinsSecs(seconds);
        assertEquals("01:20:00",formattedTime);
    }

}