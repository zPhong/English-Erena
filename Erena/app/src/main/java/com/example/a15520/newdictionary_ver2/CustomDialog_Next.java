package com.example.a15520.newdictionary_ver2;

import android.app.Activity;
import android.app.Dialog;
import android.app.UiAutomation;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

/**
 * Created by 15520 on 12/08/2017.
 */

public class CustomDialog_Next extends Dialog implements
        android.view.View.OnClickListener {

    public Activity c;
    public Dialog d;
    public Button _btnNext;
    private TextView _tvHighScore, _tvScore;
    int score , id;

    public CustomDialog_Next(Activity a, int score , int sourceID) {
        super(a);
        // TODO Auto-generated constructor stub
        this.c = a;
        this.score = score;
        id = sourceID;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_score_next);
        _btnNext = (Button) findViewById(R.id.btn_next);

        _btnNext.setOnClickListener(this);
        _btnNext.setAnimation(AnimationUtils.loadAnimation(c.getApplicationContext(),R.anim.blink));
        Typeface digitaltech = Typeface.createFromAsset(c.getAssets(),"fonts/Digitaltech.ttf");
        Typeface bungee = Typeface.createFromAsset(c.getAssets(),"fonts/Bungee-Regular.ttf");

        _btnNext.setTypeface(bungee);


        _tvScore = (TextView)findViewById(R.id.game1score);
        _tvScore.setText(String.valueOf(score));
        _tvScore.setAnimation(AnimationUtils.loadAnimation(c.getApplicationContext(),R.anim.anim_slideup));

        _tvScore.setTypeface(digitaltech);
        _tvHighScore = (TextView)findViewById(R.id.highScore);
        _tvHighScore.setTypeface(digitaltech);


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_next: {
                Intent intent = new Intent(c, RankingActivity.class);
                //add score test
                String dataColumm = "";
                switch (id) {
                    case 1: dataColumm = "Score1";break;
                    case 2 :dataColumm = "Score2";break;
                    case 3 :dataColumm = "Score3";break;
                }

                String uid = GlobalVariable.getInstance().getUser().getUid();
                final DatabaseReference mUserRef = FirebaseDatabase.getInstance().getReference("UserInfo").
                        child(uid).child(dataColumm);

                if(GlobalVariable.getInstance().getUser() != null) {
                    for (UserModel user : GlobalVariable.getInstance().get_arrayUser()) {
                        if (user.getEmail().equals(GlobalVariable.getInstance().getUser().getEmail()) == true) {
                            switch (id) {
                                case 1:{
                                    if (Integer.parseInt(user.getScore1()) < score) {
                                        String t = String.valueOf(score);
                                        mUserRef.setValue(t);
                                    }
                                };break;
                                case 2:{
                                    if (Integer.parseInt(user.getScore2()) < score) {
                                        String t = String.valueOf(score);
                                        mUserRef.setValue(t);
                                    }
                                };break;case 3:{
                                    if (Integer.parseInt(user.getScore3()) < score) {
                                        String t = String.valueOf(score);
                                        mUserRef.setValue(t);
                                    }
                                };break;

                            }
                        }

                    }

                }
                c.finish();
                c.startActivity(intent);
            }
            break;

            default:
                break;
        }
    }
}
