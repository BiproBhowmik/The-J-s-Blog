package com.joy.thejsblog;

import android.support.annotation.NonNull;

public class StoreToDB_UsersInfo implements Comparable<StoreToDB_UsersInfo> {

    String userID, userName, userEmail, userPassward, imageUrl, key;
    boolean onOffStatus;

    public StoreToDB_UsersInfo() {

    }

    public StoreToDB_UsersInfo(String userID, String userName, String userEmail,
                               String userPassward, String imageUrl, String key, boolean onOffStatus) {
        this.userID = userID;
        this.userName = userName;
        this.userEmail = userEmail;
        this.userPassward = userPassward;
        this.imageUrl = imageUrl;
        this.key = key;
        this.onOffStatus = onOffStatus;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public String getUserPassward() {
        return userPassward;
    }

    public void setUserPassward(String userPassward) {
        this.userPassward = userPassward;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public boolean isOnOffStatus() {
        return onOffStatus;
    }

    public void setOnOffStatus(boolean onOffStatus) {
        this.onOffStatus = onOffStatus;
    }

    @Override
    public int compareTo(@NonNull StoreToDB_UsersInfo o) {
        int compare = userName.compareTo(o.userName);
        if(compare == 0)
        {
            compare = userEmail.compareTo(o.userEmail);
        }

        return compare;
    }
}
