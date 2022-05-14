package com.example.myfoodapp.models;

public class ratingModel {
    String userId;
    int rate;

    public ratingModel() {
    }

    public ratingModel(String userId, int rate) {
        this.userId = userId;
        this.rate = rate;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public int getRate() {
        return rate;
    }

    public void setRate(int rate) {
        this.rate = rate;
    }
}
