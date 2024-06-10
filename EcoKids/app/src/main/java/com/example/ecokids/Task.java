package com.example.ecokids;

public class Task {
    private String id;
    private String description;
    private int points;

    public Task() {
        // Default constructor required for calls to DataSnapshot.getValue(Task.class)
    }

    public Task(String description, int points) {
        this.description = description;
        this.points = points;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public int getPoints() {
        return points;
    }
}