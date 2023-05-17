package com.example.myapplication;

import android.content.Context;

import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.*;

import java.io.File;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class UserTest {

    Context appContext;
    String userPath;
    File userFile;
    User user;


    @Before
    public void setUp() throws Exception {
        appContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
        userPath = appContext.getFilesDir().getPath() + "/userTest.json";
        userFile = new File(userPath);
        user = User.initialize(userFile);
    }


    @Test
    public void testTest(){
        int x = 5;
        int y = 6;
        int z = 30;
        assertEquals(30, x * y);
    }

    @Test
    public void useAppContext() {
        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
        assertEquals("com.example.myapplication", appContext.getPackageName());
    }

    //Tests if the storing and loading of user data is functioning correctly
    //Every setter calls the saveData() method
    @Test
    public void setUserDataTest(){
        String username = "User 1";
        int age = 18;
        float height = 179.9f;
        float weight = 64.5f;
        String sex = "male";
        int calorieCredit = 727;
        int monthlyWorkout = 5;

        user.setUsername(username);
        user.setAge(age);
        user.setHeight(height);
        user.setWeight(weight);
        user.setSex(sex);
        user.setCalorieCredit(calorieCredit);
        user.setMonthlyWorkouts(monthlyWorkout);

        assertEquals(username, user.getUsername());
        assertEquals(age, user.getAge());
        assertEquals(height, user.getHeight(), 0.01);
        assertEquals(sex, user.getSex());
        assertEquals(calorieCredit, user.getCalorieCredit());
        assertEquals(monthlyWorkout, user.getMonthlyWorkouts());

    }

    @Test
    public void updateCreditTest(){
        int songCost = 1500;
        user.setCalorieCredit(1600);
        user.updateCredit(-songCost); //simulates purchasing of song
        assertEquals(1600-1500,user.getCalorieCredit());
    }





}
