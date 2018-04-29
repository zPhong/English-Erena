package com.example.a15520.newdictionary_ver2;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.TransitionDrawable;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;

import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Random;

import static android.view.inputmethod.EditorInfo.IME_ACTION_DONE;
import static android.view.inputmethod.EditorInfo.IME_ACTION_PREVIOUS;

public class Game2Activity extends AppCompatActivity {
    private static String _htmlTextViewMe = new String();
    private EditText _etAnswer;
    private TextView _tvTextMe,  _tvScore , _tvTime , _tvHint;
    private String _hint;
    private ArrayList<String> _wordUsed;
    private int _score,_count , _maxCount;
    //list of SUFFIXES word;
    private String[] _listHint = {"sion","tion", "per" , "nal" , "tor" , "ter" , "ish", "ive" , "end" ,"ment","ance","ence","ity","ist","ness","tal","able","less","ful",};
    private CountDownTimer _timer;
    private boolean _isShowingDialog;
    private int _wrongCount = 0 ;
    private ConstraintLayout _layout;
    private AnimationDrawable _animation;
    private int _life = 4;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game2);

        Typeface courbd = Typeface.createFromAsset(getAssets(),"fonts/courbd.ttf");

        _etAnswer = (EditText)findViewById(R.id.etAnswer);

        _tvTextMe = (TextView)findViewById(R.id.tvWord2);

        _htmlTextViewMe = "";

        _tvTime = (TextView)findViewById(R.id.tvTime2);
        _tvTime.setTypeface(courbd);

        _score = 0;

        _count = 180;
        _maxCount = 3;

        _tvHint = (TextView)findViewById(R.id.tvHint2);
        _tvHint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(_life > 0) {
                    setHint();
                    _life--;
                }
            }
        });
        setHint();

        _wordUsed = new ArrayList<>();

        _layout = findViewById(R.id.drawer_layout);


        _animation = (AnimationDrawable) _layout.getBackground();
        _animation.setEnterFadeDuration(200);
        _animation.setExitFadeDuration(200);
        _animation.start();




        _etAnswer.setOnEditorActionListener(new TextView.OnEditorActionListener()
        {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                boolean handled = false;
                if (i == IME_ACTION_DONE) {
                  /* Write your logic here that will be executed when user taps next button */

                    CheckAndScore();
                    handled = true;
                }
                if(i == IME_ACTION_PREVIOUS)
                {
                    finish();
                    handled = true;
                }


                return handled;

            }
        });
        _isShowingDialog = true;

        _timer = new CountDownTimer(181000, 1000){
            public void onTick(long millisUntilFinished ){
                _count--;
                if(_count < 0) {
                    _count = 0;
                    if(_isShowingDialog == true) {
                        CustomDialog_Next cdd = new CustomDialog_Next(Game2Activity.this,_score,2);
                        cdd.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                        cdd.setCancelable(false);

                        cdd.show();
                    }
                    _isShowingDialog = false;

                }
                _tvTime.setAnimation(AnimationUtils.loadAnimation(getApplicationContext(),R.anim.blink));

                _tvTime.setText(String.valueOf(_count/60) + ":" + String.valueOf(_count%60));

            }
            public  void onFinish(){


                _timer.cancel();

            }
        }.start();


    }

    public void setHint()
    {
        //_stage++;
        Random r = new Random();
        int i = r.nextInt(_listHint.length);

        _hint = _listHint[i];
        _tvHint.setText(_hint);
    }

    public void CheckAndScore()
    {
        String str = _etAnswer.getText().toString().trim();
        if(GlobalVariable.getInstance().get_listEV().contains(str) == true
            && _wordUsed.contains(str) == false
            && str.contains(_hint) == true
            && str.length() > _hint.length()) {
            _htmlTextViewMe = _htmlTextViewMe + strParagraphHTML(str, "white", "red");
            _tvTextMe.setText(Html.fromHtml(_htmlTextViewMe));
            _wordUsed.add(str);
            _score = 1 + bonusScore();
            //_tvScore.setText(String.valueOf(_score));

            if (_wordUsed.size() % _maxCount == 0) {
                setHint();
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(_etAnswer.getWindowToken(), 0);
                _etAnswer.requestFocus();
                imm.toggleSoftInput(InputMethodManager.SHOW_FORCED,0);

            }


            //_layout.getLayoutParams().height +=  20;
        }
        else
        {
            _wrongCount++;
            _count -= _wrongCount*2;
        }
        _etAnswer.setText("");
    }

    @NonNull
    private String strParagraphHTML(String str, String other, String hint) {
        String[] otherWord = str.split(_hint);
        String _return = "<div>";

        if(str.startsWith(_hint) == true)
        {
            _return += strColorHTML(_hint,hint);
        }

        for(int i = 0; i < otherWord.length  ; i++)
        {
            if(otherWord[i].equals("") == false) {
                _return += strColorHTML(otherWord[i], other);
                if (i + 1 < otherWord.length) {
                    _return += strColorHTML(_hint, hint);
                }
            }
        }

        if(str.endsWith(_hint) == true)
        {
            _return += strColorHTML(_hint,hint);
        }

        return _return + "</div>";
    }

    private int bonusScore() {
        int size = _etAnswer.getText().length();
        switch (size)
        {
            case 3 :case 4 :case  5:
                {
                 return 3*(size - _hint.length());
                }
            case 6 :case 7 :
                {
                 return 4*(size - _hint.length());
                }
            case 8 :case 9:
                {
                 return  4*(size - _hint.length() + 3);
                }
        default:
                 return  5* (size - _hint.length());
        }

    }

    public String strColorHTML(String text, String color)
    {
        return "<font color=\"" + color + "\">" + text+ "</font>";
    }

}
