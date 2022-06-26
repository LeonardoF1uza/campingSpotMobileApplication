package com.example.campingspot.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class User {
    private String id, name, email;
    private ArrayList<String> historic;

    public User(String id, String name, String email) {
        this.id = id;
        this.name = name;
        this.email = email;
    }

    public User(ArrayList<String> historic) {
        this.historic = historic;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String email) {
        this.name = name;
    }


    public Map<String, Object> toDB() {
        Map<String, Object> user = new HashMap<>();

        user.put("id", id);
        user.put("name", name);
        user.put("email", email);
        user.put("historic", new ArrayList<String>());
        return user;
    }

    public Map<String, Object> toDB_2() {
        Map<String, Object> user = new HashMap<>();
        user.put("historic", historic);
        return user;
    }
}
