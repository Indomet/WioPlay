package com.example.myapplication;

public class User {
    private String username;
    private int age;
    private float height;
    private float weight;
    private String gender;

    //Empty constructor
    //todo make the user start in settings to input stuff and cant leave if they dont do it
    //and the values to not be 0
    User(){
    age = 0;
    height =0;
    weight=0;
    username="username";
    }
    public void setUsername(String username) {
        this.username = username;

    }

    public String getUsername() {
        return username;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public int getAge() {
        return age;
    }

    public void setHeight(float height) {
        this.height = height;

    }

    public float getHeight() {
        return height;
    }

    public void setWeight(float weight) {
        this.weight = weight;
    }

    public float getWeight() {
        return weight;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getGender() {
        return gender;
    }

    public String toString(){
        return age+","+height+","+weight;
    }


}
