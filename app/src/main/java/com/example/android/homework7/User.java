package com.example.android.homework7;

/**
 * Created by Jake on 11/17/16.
 */

public class User {
    String name, gender, imageUrl;

    public User() {

    }

    public User(String name, String gender) {
        this.name = name;
        this.gender = gender;
    }

    public User(String name, String gender, String imageUrl) {
        this.name = name;
        this.gender = gender;
        this.imageUrl = imageUrl;
    }
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }



}
