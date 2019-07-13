package com.joy.thejsblog;

class Love{
    private String loveStatus, key;
    private int love;

    public Love() {
    }

    public Love(String loveStatus, String key, int love) {
        this.loveStatus = loveStatus;
        this.key = key;
        this.love = love;
    }

    public String getLoveStatus() {
        return loveStatus;
    }

    public void setLoveStatus(String loveStatus) {
        this.loveStatus = loveStatus;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public int getLove() {
        return love;
    }

    public void setLove(int love) {
        this.love = love;
    }
}
