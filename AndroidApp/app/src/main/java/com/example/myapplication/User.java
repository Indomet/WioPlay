package com.example.myapplication;

public class User {
    private String username;
    private int age;
    private float height;
    private float weight;
    private String sex;
    private int calorieCredit;
    private int lifeTimeCredit;

    //Empty constructor
    //todo make the user start in settings to input stuff and cant leave if they don't do it
    //and the values to not be 0
    User(){
    age = 0;
    height =0;
    weight=0;
    username="username";
    calorieCredit = 727; //A new user starts with 0 CalorieCurrency
    lifeTimeCredit=727;
    }
    
    public void setUsername(String username) {
        this.username = username;

    }

    public void setAge(int age) {
        this.age = age;
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


    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getSex() {
        return sex;
    }

    public void updateCredit(float calorie){
        //calorie can be positive when gaining, and negative when spending.
        int diff = Math.round(calorie);
        this.calorieCredit += diff;

    }
    public int getCalorieCredit(){
        return this.calorieCredit;
    }
    public int getLifeTimeCredit(){
        return lifeTimeCredit;
    }
    public void setLifeTimeCredit(int newCredit){
        lifeTimeCredit=newCredit;
    }

    public String toString(){
        return age+","+height+","+weight;
    }




}
