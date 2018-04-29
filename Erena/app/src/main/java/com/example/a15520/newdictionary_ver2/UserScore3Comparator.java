package com.example.a15520.newdictionary_ver2;

import java.util.Comparator;

public class UserScore3Comparator implements Comparator<UserModel> {

            @Override
    public int compare(UserModel emp1, UserModel emp2) {
                if(Integer.parseInt(emp1.getScore3()) > Integer.parseInt(emp2.getScore3()))
                    return 1;
                if(Integer.parseInt(emp1.getScore3()) < Integer.parseInt(emp2.getScore3()))
                    return -1;
                return 0;
    }
}
