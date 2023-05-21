package com.example.myapplication;


import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;

import com.fasterxml.jackson.databind.JsonNode;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentTransaction;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.nio.file.Files;
import java.nio.file.Paths;
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


    public static boolean stringIsAlphabeticallyGreater(String string1, String string2) {
        int minLength = Math.min(string1.length(), string2.length());

        int x = -1;
        int alphabeticalComparison = 0;

        // While not able to identify any alphabetical fluctuations of the starting common substring
        while (++x < minLength && alphabeticalComparison == 0)
        {
            alphabeticalComparison = compareAlphabeticalCharacterOrder(string1.charAt(x), string2.charAt(x));

            if (alphabeticalComparison != 0)
                return (alphabeticalComparison > 0) ? true : false;
        }

        // Deal with edge case where both strings starts with the same substring
        if (string2.length() > string1.length())
            return true;
        return false;
    }

    private static int compareAlphabeticalCharacterOrder(char c1, char c2)
    {
        if (c1 < c2)
            return 1;
        else if (c2 < c1)
            return -1;
        return 0;
    }

    // Binary Search Insertion for Strings
    public static int binarySearchInsertion(ArrayList<String> stringList, String stringToInsert)
    {
        int left = 0;
        int right = stringList.size() - 1;

        while (left <= right)
        {
            int mid = (left + right) / 2;
            String currentTitle = stringList.get(mid);

            boolean newSongTitleIsAlphabeticallyGreater = stringIsAlphabeticallyGreater(stringToInsert, currentTitle);

            if (newSongTitleIsAlphabeticallyGreater)
                right = mid - 1;
            else
                left = mid + 1;
        }

        return (int)Math.ceil(((left + right) /(double)2));
    }
    public static Bitmap addTheProfileFromExistingPhotoInPhone(){
        File file1 = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        String filePath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES) + File.separator + "myImage.jpg";
        if(pictureExist(file1)){
            try {
                InputStream inputStream = null;
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                    inputStream = Files.newInputStream(Paths.get(filePath));
                }

                Bitmap bitmap1= BitmapFactory.decodeStream(inputStream);
                Log.d("It is being reached", "it is being reachesd");
                //profilePicture.setImageBitmap(bitmap1);
                //LayoutInflater inflater = LayoutInflater.from(this);
                //View rootView = inflater.inflate(R.layout.fragment_settings ,null);
                //ImageView imageView = rootView.findViewById(R.id.user_profile_pic_settings);
                // imageView.setImageBitmap(bitmap1);

                //LayoutInflater inflater1 = LayoutInflater.from(getContext());
                //View rootView1 = inflater1.inflate(R.layout.fragment_workout ,null);
                //ImageView imageView1 = rootView1.findViewById(R.id.user_picture);
                //imageView1.setImageBitmap(bitmap1);
                return bitmap1;

            }catch (Exception e){
                e.getMessage();
            }


        }
        return null;

    }
    public static boolean pictureExist(File file){

        if(file.isDirectory()){
            File[] files = file.listFiles();
            if(files!=null){
                for (File myfile:files) {
                    if(myfile.isDirectory()){
                        pictureExist(myfile);
                    } else {
                        String compare1=myfile.getAbsolutePath();
                        String compare2=file.getAbsolutePath()+"/myImage.jpg";
                        Log.d("compare2",compare2);
                        Log.d("compare1",compare1);
                        if(compare2.equals(compare1)) {
                            return true;
                        }
                    }
                }

            }

        }
        return false;
    }
}
