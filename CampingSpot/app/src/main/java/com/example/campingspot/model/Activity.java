package com.example.campingspot.model;

import java.util.HashMap;
import java.util.Map;

public class Activity {
    private String name, location, description, duration_initial, duration_final, capacity, img;

    public Activity(String name, String location, String description, String duration_initial, String duration_final, String capacity) {
        this.name = name;
        this.capacity = capacity;
        this.description = description;
        this.img = name;
        this.location = location;
        this.duration_initial = duration_initial;
        this.duration_final = duration_final;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDuration_initial() {
        return duration_initial;
    }

    public void setDuration_initial(String duration_initial) {
        this.duration_initial = duration_initial;
    }

    public String getDuration_final() {
        return duration_final;
    }

    public void setDuration_final(String duration_final) {
        this.duration_final = duration_final;
    }

    public String getCapacity() {
        return capacity;
    }

    public void setCapacity(String capacity) {
        this.capacity = capacity;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public Map<String, Object> toDB() {
        Map<String, Object> activity = new HashMap<>();

        activity.put("name", name);
        activity.put("description", description);
        activity.put("capacity", capacity);
        activity.put("duration_initial", duration_initial);
        activity.put("duration_final", duration_final);
        activity.put("image", img);
        activity.put("location", location);

        return activity;
    }
}

