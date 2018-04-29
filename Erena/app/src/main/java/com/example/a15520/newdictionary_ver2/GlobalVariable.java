package com.example.a15520.newdictionary_ver2;

import android.content.Context;
import android.util.Log;

import com.google.firebase.auth.FirebaseUser;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by 15520 on 12/02/2017.
 */

public class GlobalVariable {
    private static GlobalVariable mInstance= null;
    private ArrayList<String> _listEV;
    private ArrayList<UserModel> _arrayUser;
    private FirebaseUser user = null;
    private DictionaryWord VE;
    private DictionaryWord EV;
    public DictionaryWord EE;
    public ArrayList<String> _listTopic;


    private HashMap<String,ArrayList<String>> topicMap;

    public HashMap<String, ArrayList<String>> getTopicMap() {
        return topicMap;
    }

    public ArrayList<UserModel> get_arrayUser() {
        return _arrayUser;
    }

    public GlobalVariable() {
        _arrayUser = new ArrayList<>();
        topicMap = new HashMap<>();
        _listTopic = new ArrayList<>();

        EE = new DictionaryWord();
    }

    public static synchronized GlobalVariable getInstance(){
        if(null == mInstance){
            mInstance = new GlobalVariable();
        }
        return mInstance;
    }


    public void set_arrayUser(ArrayList<UserModel> _arrayUser) {
        this._arrayUser = _arrayUser;
    }

    public FirebaseUser getUser() {
        return user;
    }


    public DictionaryWord getVE() {
        return VE;
    }

    public void setVE(DictionaryWord VE) {
        this.VE = VE;
    }

    public DictionaryWord getEV() {
        return EV;
    }

    public void setEV(DictionaryWord EV) {
        this.EV = EV;
    }


    public ArrayList<String> get_listEV() {
        return _listEV;
    }

    public void set_listEV(ArrayList<String> _listEV) {
        this._listEV = _listEV;
    }


    public void setUser(FirebaseUser user) {
        this.user = user;
    }




}
