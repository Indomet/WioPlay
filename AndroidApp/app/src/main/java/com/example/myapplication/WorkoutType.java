package com.example.myapplication;

public enum WorkoutType {
    HIKING("Hiking"),
    WALKING("Walking"),
    RUNNING("Running");

    private final String name;

    WorkoutType(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
