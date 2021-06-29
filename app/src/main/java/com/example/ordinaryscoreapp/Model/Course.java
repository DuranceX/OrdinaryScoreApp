package com.example.ordinaryscoreapp.Model;

public class Course {
    private String title;
    private String location;
    private String time;

    public Course(String title, String location, String time) {
        this.title = title;
        this.location = location;
        this.time = time;
    }

    public Course() {
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
