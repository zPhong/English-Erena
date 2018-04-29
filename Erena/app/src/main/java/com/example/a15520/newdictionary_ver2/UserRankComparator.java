package com.example.a15520.newdictionary_ver2;

import android.content.Intent;

import java.util.Comparator;

public class UserRankComparator implements Comparator<UserModel> {

            @Override
    public int compare(UserModel emp1, UserModel emp2) {
        if(Integer.parseInt(emp1.getRank()) > Integer.parseInt(emp2.getRank()))
            return 1;
        if(Integer.parseInt(emp1.getRank()) < Integer.parseInt(emp2.getRank()))
            return -1;
        return 0;
    }
}
