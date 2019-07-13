package com.joy.thejsblog;

import android.support.annotation.NonNull;

public class StoreToDB{

    private String title, discription, imageURI, userName, email, key, date, time, cirleImage;

    public StoreToDB() {

    }

    public StoreToDB(String title, String discription, String imageURI, String userName, String email,
                     String key, String date, String time, String cirleImage) {
        this.title = title;
        this.discription = discription;
        this.imageURI = imageURI;
        this.userName = userName;
        this.email = email;
        this.key = key;
        this.date = date;
        this.time = time;
        this.cirleImage = cirleImage;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDiscription() {
        return discription;
    }

    public void setDiscription(String discription) {
        this.discription = discription;
    }

    public String getImageURI() {
        return imageURI;
    }

    public void setImageURI(String imageURI) {
        this.imageURI = imageURI;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getCirleImage() {
        return cirleImage;
    }

    public void setCirleImage(String cirleImage) {
        this.cirleImage = cirleImage;
    }
}
