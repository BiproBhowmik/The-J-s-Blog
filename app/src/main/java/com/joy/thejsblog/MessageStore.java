package com.joy.thejsblog;

public class MessageStore{
    String uName, mSG, iURL;

    public MessageStore() {
    }

    public MessageStore(String uName, String mSG, String iURL) {
        this.uName = uName;
        this.mSG = mSG;
        this.iURL = iURL;
    }

    public String getuName() {
        return uName;
    }

    public void setuName(String uName) {
        this.uName = uName;
    }

    public String getmSG() {
        return mSG;
    }

    public void setmSG(String mSG) {
        this.mSG = mSG;
    }

    public String getiURL() {
        return iURL;
    }

    public void setiURL(String iURL) {
        this.iURL = iURL;
    }
}
