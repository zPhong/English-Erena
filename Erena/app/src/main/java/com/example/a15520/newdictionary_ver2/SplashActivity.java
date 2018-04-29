package com.example.a15520.newdictionary_ver2;

/**
 * Created by 15520 on 12/01/2017.
 */

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.ListView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by AbhiAndroid
 */

public class SplashActivity extends Activity {

    Handler handler;
    private DictionaryWord VE;
    ArrayList<String> _listwordVE;
    private DictionaryWord EV;
    ArrayList<String> _listwordEV;
    private DatabaseReference mUserRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_screen);

        handler=new Handler();



        handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    Intent intent=new Intent(SplashActivity.this,LoginActivity.class);
                    startActivity(intent);
                    EV = new DictionaryWord();
                    _listwordEV = readData(1);
                    //
                    GlobalVariable.getInstance().set_listEV(_listwordEV);
                    // chia list thanh cac list nho theo do dai tu
                    GlobalVariable.getInstance().setEV(EV);
                    VE = new DictionaryWord();
                    _listwordVE = readData(0);
                    GlobalVariable.getInstance().setVE(VE);
                    createTopicMap();
                    LoadListUser();
                    finish();
            }
        },3000);

    }

    public void LoadListUser()
    {
        mUserRef = FirebaseDatabase.getInstance().getReference("UserInfo");

        final ArrayList<UserModel> _listID = new ArrayList<>();
        mUserRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                // _listID.add(dataSnapshot.getValue(UserModel.class));
                UserModel pos = dataSnapshot.getValue(UserModel.class);
                _listID.add(pos);
                GlobalVariable.getInstance().set_arrayUser(_listID);

            }
            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }
    public ArrayList<String> readData(int type)
    {
        //region Load Data tu txt
        String dictdata = new String();
        String data;
        InputStream in;
        if(type == 1)
            in= getResources().openRawResource(R.raw.dictev);
        else
            in= getResources().openRawResource(R.raw.dictve);

        InputStreamReader inreader=new InputStreamReader(in);
        BufferedReader bufreader=new BufferedReader(inreader);
        StringBuilder builder=new StringBuilder();
        if(in!=null)
        {
            try
            {
                while((data = bufreader.readLine())!=null)
                {
                    builder.append(data);
                    builder.append("\n");

                }
                in.close();
                dictdata = builder.toString();
            }
            catch(IOException ex){
                Log.e("ERROR", ex.getMessage());
            }
        }
        //endregion
        //region create hashmap

        ArrayList<String> word = new ArrayList<>();
        String[] fullWords = dictdata.split(System.lineSeparator() + System.lineSeparator());
        String name = new String();
        String regex = "(?<=@)[\\S\\D ]+?(?= /)";
        Pattern pattern = Pattern.compile(regex);
        for(String oneWord : fullWords) {
            try {
                Matcher m = pattern.matcher(oneWord);
                if(m.find())
                    name = m.group();
                word.add(name);
                if(type ==1)
                    if (EV.IsContain(name) == false)
                        EV.Add(name, oneWord+"\n---------------");
                if(type == 0)
                    if (VE.IsContain(name) == false)
                        VE.Add(name, oneWord+"\n---------------");
            } catch (Exception e) {
                Log.e("ERROR", e.getMessage());
            }
        }
        //endregion




        return word;
    }

    public void createTopicMap()
    {
        if(!GlobalVariable.getInstance().getTopicMap().isEmpty())
            return;

        String data;
        InputStream in;

        in= getResources().openRawResource(R.raw.tagname);

        InputStreamReader inreader=new InputStreamReader(in);
        BufferedReader bufreader=new BufferedReader(inreader);


        if(in!=null)
        {
            try
            {
                while((data = bufreader.readLine())!=null)
                {
                    createtopicMapItem(data);
                    GlobalVariable.getInstance()._listTopic.add(data);
                }
                in.close();
            }
            catch(IOException ex){
                Log.e("ERROR", ex.getMessage());
            }
        }
    }

    private void createtopicMapItem(String key)
    {
        String data;
        InputStream in = null;
        switch (key) {
            case "BodyAndAppearance" :
            {
                in =  getResources().openRawResource(R.raw.topicbodyandappearance);
                break;
            }
            case "BusinessAndWork" :
            {
                in = getResources().openRawResource(R.raw.topicbusinessandwork);
                break;
            }
            case "CultureAndSociety" :
            {
                in = getResources().openRawResource(R.raw.topiccultureandsociety);
                break;
            }
            case "Education" :
            {
                in = getResources().openRawResource(R.raw.topiceducation);
                break;
            }
            case "Entertainment" :
            {
                in = getResources().openRawResource(R.raw.topiceducation);
                break;
            }
            case "FoodAndDrink" :
            {
                in = getResources().openRawResource(R.raw.topicfoodanddrink);
                break;
            }
            case "ScienceAndTechnology" :
            {
                in = getResources().openRawResource(R.raw.topicscienceandtechnology);
                break;
            }

            case "Relationship" :
            {
                in = getResources().openRawResource(R.raw.topicrelationship);
                break;
            }
            case "Sport" :
            {
                in = getResources().openRawResource(R.raw.topicsport);
                break;
            }

        }
        InputStreamReader inreader=new InputStreamReader(in);
        BufferedReader bufreader=new BufferedReader(inreader);
        ArrayList<String> topicItemData = new ArrayList<>();
        if(in!=null)
        {
            try
            {
                while((data = bufreader.readLine())!=null)
                {
                    topicItemData.add(data.replace("-","").trim());

                }
                in.close();
            }
            catch(IOException ex){
                Log.e("ERROR", ex.getMessage());
            }
        }
        if(!topicItemData.isEmpty())
            GlobalVariable.getInstance().getTopicMap().put(key,topicItemData);
    }
}