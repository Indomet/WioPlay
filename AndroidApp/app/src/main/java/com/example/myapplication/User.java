package com.example.myapplication;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;

import java.io.File;
import java.io.IOException;

public class User {
    private String username;
    private int age;
    private float height;
    private float weight;
    private String sex;
    private int calorieCredit;
    private int lifeTimeCalories;
    private File userFile;
    private int lifeTimeCredit;
    private static User user= null;

    
    
    
    public User(File userFile) {
        this.defaultUser();
        this.userFile = userFile;
        load();
    }
    //todo make the user start in settings to input stuff and cant leave if they don't do it
    //and the values to not be 0
    private User(){
    age = 0;
    height =0;
    weight=0;
    username="username";
    calorieCredit = 727; //A new user starts with 0 CalorieCurrency
    lifeTimeCredit=727;
    }
    public static User getInstance(){
        if(user== null){
            user = new User();
        }
        return user;
    }

    
    public void setUsername(String username) {
        this.username = username;
        saveUserData();
    }

    public void setAge(int age) {
        this.age = age;
        saveUserData();
    }


    public void setHeight(float height) {
        this.height = height;
        saveUserData();
    }

    public float getHeight() {
        return height;
    }

    public void setWeight(float weight) {
        this.weight = weight;
        saveUserData();
    }


    public void setSex(String sex) {
        this.sex = sex;
        saveUserData();
    }

    public String getSex() {
        return sex;
    }

    public void updateCredit(float calorie) {
        //calorie can be positive when gaining, and negative when spending.
        int diff = Math.round(calorie);
        this.calorieCredit += diff;
        saveUserData();

    }

    public int getCalorieCredit() {
        return this.calorieCredit;
    }

    public void setCalorieCredit(int calorieCredit) {
        this.calorieCredit = calorieCredit;
        saveUserData();
    }

    public String toString() {
        return age + "," + height + "," + weight;
    }

    public int getLifeTimeCalories() {
        return lifeTimeCalories;
    }

    public void setLifeTimeCalories(int lifeTimeCalories) {
        this.lifeTimeCalories = lifeTimeCalories;
        saveUserData();
    }

    private void load() {

        try {

            ObjectMapper mapper = new ObjectMapper();
            JsonNode node = mapper.readTree(this.userFile);
            this.setUsername(node.get("username").asText());
            this.setAge(node.get("age").asInt());
            this.setHeight(node.get("height").asLong());
            this.setSex(node.get("sex").asText());
            this.setWeight(node.get("weight").asLong());
            this.setCalorieCredit(node.get("calorieCredit").asInt());
            this.setLifeTimeCalories(node.get("lifeTimeCalories").asInt());

        } catch (IOException e) {

            saveUserData();
            load();

        }

    }

    public void saveUserData() {
        ObjectMapper mapper = new ObjectMapper();
        ObjectWriter writer = mapper.writer();
        JsonNode node = mapper.valueToTree(this);
        try {
            writer.writeValue(this.userFile, node);//works
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public String getUsername() {
        return username;
    }

    public int getAge() {
        return age;
    }

    public float getWeight() {
        return weight;
    }

}
