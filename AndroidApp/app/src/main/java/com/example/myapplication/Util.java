package com.example.myapplication;

import static android.app.PendingIntent.getActivity;

import android.app.Activity;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;

import java.io.File;
import java.io.IOException;


public class Util {

    public static final String WORKOUT_MANAGER_PATH = "/workoutManager.json";
    public static String objectToJSON(Object object) throws IllegalArgumentException, IllegalAccessException {
        ObjectWriter jsonWriter = new ObjectMapper().writer().withDefaultPrettyPrinter();
        String json = null;
        try {
            json = jsonWriter.writeValueAsString(object);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return json;
    }

    public static WorkoutManager loadManagerFromFile(Activity activity){
        String managerPath = activity.getFilesDir().getPath() + Util.WORKOUT_MANAGER_PATH;
        File managerFile = new File(managerPath);
        return WorkoutManager.getInstance(managerFile);
    }

}
