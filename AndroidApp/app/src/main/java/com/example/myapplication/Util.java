package com.example.myapplication;

import static android.app.PendingIntent.getActivity;

import android.app.Activity;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;

import java.io.File;
import java.io.IOException;


public class Util {

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
        final String WORKOUT_MANAGER_PATH = "/workoutManager.json";
        String managerPath = activity.getFilesDir().getPath() + WORKOUT_MANAGER_PATH;
        File managerFile = new File(managerPath);
        return WorkoutManager.getInstance(managerFile);
    }

    public static String formatHoursMinsSecs(int timeInSeconds){
        //3600 is how many seconds in an hour
        int hours = timeInSeconds / 3600;
        //60 is how many seconds in a minute
        int minutes = (timeInSeconds % 3600) / 60;
        //% 60 to limit the seconds when being formatted
        int seconds = timeInSeconds % 60;
        return String.format("%02d:%02d:%02d", hours, minutes, seconds);
    }

    public static String formatHoursMins(int timeInSeconds){
        int hours = timeInSeconds / 3600;
        int minutes = (timeInSeconds % 3600) / 60;
        return String.format("%02d:%02d", hours, minutes);
    }

}
