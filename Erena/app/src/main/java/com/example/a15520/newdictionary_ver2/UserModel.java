package com.example.a15520.newdictionary_ver2;


import android.support.annotation.NonNull;

import java.util.Comparator;

public class UserModel {
    private String Url = new String();
    private String Name = new String();
    private String Score1 = new String();
    private String Score2 = new String();
    private String Score3 = new String();

    public String getScore1() {
        return Score1;
    }

    public void setScore1(String score1) {
        Score1 = score1;
    }

    public String getScore2() {
        return Score2;
    }

    public void setScore2(String score2) {
        Score2 = score2;
    }

    public String getScore3() {
        return Score3;
    }

    public void setScore3(String score3) {
        Score3 = score3;
    }

    public UserModel(String url, String name, String score1, String score2, String score3, String email, String rank) {

        Url = url;
        Name = name;
        Score1 = score1;
        Score2 = score2;
        Score3 = score3;
        Email = email;
        Rank = rank;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        this.Email = email;
    }

    private String Email = new String();
    private String Rank = "1";



    public UserModel() {

    }

    public String getUrl() {
        return Url;
    }

    public void setUrl(String url) {
        this.Url = url;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getRank() {
        return Rank;
    }

    public void setRank(String rank) {
        this.Rank = rank;
    }

}

