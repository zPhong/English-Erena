package com.example.a15520.newdictionary_ver2;

import java.util.Comparator;

public class UserScore2Comparator implements Comparator<UserModel> {

            @Override
    public int compare(UserModel emp1, UserModel emp2) {
                if(Integer.parseInt(emp1.getScore2()) > Integer.parseInt(emp2.getScore2()))
                    return 1;
                if(Integer.parseInt(emp1.getScore2()) < Integer.parseInt(emp2.getScore2()))
                    return -1;
                return 0;
    }
}
