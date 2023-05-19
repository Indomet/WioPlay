package com.example.myapplication;


import com.fasterxml.jackson.databind.JsonNode;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentTransaction;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;


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

    public static List<int[]> chunkify(int[] array, int N) {
        List<int[]> chunks = new ArrayList<>();

        for (int i = 0; i < array.length; i += N) {
            int chunkSize = Math.min(N, array.length - i);
            int[] chunk = new int[chunkSize];
            System.arraycopy(array, i, chunk, 0, chunkSize);
            chunks.add(chunk);
        }

        return chunks;
    }

    public static void changeFragment(Fragment fragment, FragmentActivity activity){
        FragmentTransaction fm = activity.getSupportFragmentManager().beginTransaction();
        fm.replace(R.id.frameLayout, fragment).setReorderingAllowed(true).commit();
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

    public static void loadFields(Object object,JsonNode node, ObjectMapper mapper) throws IllegalAccessException {
        for(Field field : object.getClass().getDeclaredFields()){
            String name = field.getName();
            field.setAccessible(true);
            JsonNode jsonValue = node.get(name);
            if (jsonValue != null) {
                //set the value of the field to be the json saved value
                Object value = mapper.convertValue(jsonValue, field.getType());
                field.set(object, value);
            }
            field.setAccessible(false);

        }
    }
    static class KeywordComparator implements Comparator<Song> {
        private String keyword;

        public KeywordComparator(String keyword) {
            this.keyword = keyword.toLowerCase();
        }

        @Override
        public int compare(Song s1, Song s2) {
            // Convert strings to lowercase for case-insensitive comparison
            String lowerS1 = s1.getTitle().toLowerCase();
            String lowerS2 = s2.getTitle().toLowerCase();

            // Check if the keyword is present in each string
            boolean s1ContainsKeyword = lowerS1.contains(keyword);
            boolean s2ContainsKeyword = lowerS2.contains(keyword);

            if (s1ContainsKeyword && !s2ContainsKeyword) {
                return -1; // s1 comes before s2
            } else if (!s1ContainsKeyword && s2ContainsKeyword) {
                return 1; // s1 comes after s2
            } else {
                return s1.getTitle().compareTo(s2.getTitle()); // no preference based on the keyword, sort alphabetically
            }
        }
    }


// WHEN TEXT CHANGES


}
