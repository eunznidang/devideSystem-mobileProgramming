package com.example.devide;

import android.net.Uri;

public class User {
    private String password;
    private String userName;
    private String email;
    private String userID;
    private String college;
    private String weekPlan;
    private String report;


    public User() {
    }

    public User(String userID, String email, String password, String userName, String college, String weekPlan, String report) {
        this.userID = userID;
        this.password = password;
        this.userName = userName;
        this.email = email;
        this.college = college;
        this.weekPlan = weekPlan;
        this.report = report;
    }

    public String getUserID() {
        return userID;
    }

    public String getPassword() {
        return password;
    }

    public String getUserName() {
        return userName;
    }

    public String getEmail() {
        return email;
    }

    public String getCollege() {
        return college;
    }

    public String getWeekPlan() {
        return weekPlan;
    }


    public String getReport() {
        return report;
    }

}
