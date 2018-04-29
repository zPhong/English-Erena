package com.example.a15520.newdictionary_ver2;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Random;

import static android.view.inputmethod.EditorInfo.IME_ACTION_DONE;
import static android.view.inputmethod.EditorInfo.IME_ACTION_NEXT;
import static android.view.inputmethod.EditorInfo.IME_ACTION_PREVIOUS;

public class Game1Activity extends AppCompatActivity {
    private EditText _etPlayer;
    private TextView _tvScore , _tvTime;
    private String _endWord;
    private ArrayList<String> _wordUsed;
    private int _score,_count;
    private CountDownTimer _timer;
    private boolean _firstWord = true;
    private int _wrongCount;
    private boolean _isShowingDialog;
    private ArrayList<String>[] _listbyFirstChar;
    private ListView _lvWord;
    private Game1Adapter adapter;
    private ConstraintLayout _layoutCity;
    private ConstraintLayout _layoutTree;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_test);

        createListAI();
        Typeface courierBd = Typeface.createFromAsset(getAssets(),"fonts/courbd.ttf");
        _etPlayer = (EditText)findViewById(R.id.etPlayer);

        _lvWord = (ListView)findViewById(R.id.lvWordGame1);

        _layoutCity = (ConstraintLayout)findViewById(R.id.layoutCity);
        _layoutTree = (ConstraintLayout)findViewById(R.id.layoutTree);

        _tvScore = (TextView)findViewById(R.id.score);
        _tvScore.setTypeface(courierBd);
        _tvScore.setText("0");
        _tvTime = (TextView)findViewById(R.id.time);
        _tvTime.setTypeface(courierBd);

        _score = 0;
        _wordUsed = new ArrayList<>();

        adapter = new Game1Adapter(_wordUsed,this);
        _lvWord.setAdapter(adapter);


        _count = 120;
        // random first word

                Random r = new Random();
                int i = r.nextInt(GlobalVariable.getInstance().get_listEV().size());
                _etPlayer.setText(GlobalVariable.getInstance().get_listEV().get(i));
                doClick(false);



        _etPlayer.addTextChangedListener(new TextWatcher() {
            int mPreviousLength;
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                mPreviousLength = charSequence.length();
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                Boolean mBackSpace = mPreviousLength > editable.length();
                if(mBackSpace == true && editable.length() == 0)
                {
                    _etPlayer.setText(_endWord);
                    if(!_endWord.equals(""))
                        _etPlayer.setSelection(1);
                }


            }
        });

        _etPlayer.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                boolean handled = false;
                if (i == IME_ACTION_DONE || i == IME_ACTION_NEXT) {
                  /* Write your logic here that will be executed when user taps next button */

                    doClick(true);
                    doClick(false);

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

        _timer = new CountDownTimer(121000, 1000){
            public void onTick(long millisUntilFinished ){
                _count--;
                if(_count < 0) {
                    _count = 0;
                   if(_isShowingDialog == true) {
                       CustomDialog_Next cdd = new CustomDialog_Next(Game1Activity.this,_score,1);
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

    private void createListAI() {
        _listbyFirstChar = new ArrayList[26];
        for(int i = 0; i < 26 ;i++)
        {
            _listbyFirstChar[i] = new ArrayList<>();
        }
        String[] specialChar = {"'"," ","."};
        for(String str : GlobalVariable.getInstance().get_listEV())
        {
            boolean _isValid = true;
            for(String character : specialChar)
            {
                if(str.contains(character) == true) {
                    _isValid = false;
                    break;
                }
                if(str.length() < 1)
                {
                    _isValid = false;
                    break;
                }
            }

            if(_isValid)
            {
                _listbyFirstChar[(int)str.charAt(0) - 97].add(str);
            }
        }

    }


    private void doClick(boolean _isUser) {

        String text =_etPlayer.getText().toString().trim();
        if(_isUser == false && _firstWord == false ) {
          text = findNextWord();
        }
        if(_firstWord == true)
            _firstWord = false;
        if(GlobalVariable.getInstance().get_listEV().contains(text) == true
                && _wordUsed.contains(text) == false && text.length() > 1 )
        {

            _endWord = text.substring(text.length() - 1);
            _wordUsed.add(0,text);
            adapter.notifyDataSetChanged();
//            _lvWord.setAdapter(adapter);
            _lvWord.setAnimation(AnimationUtils.loadAnimation(getApplicationContext(), R.anim.blink));

            if (_isUser == true && _firstWord == false) {
                //update previous action

                _tvScore.setAnimation(AnimationUtils.loadAnimation(getApplicationContext(),R.anim.blink));
                _score += 5  + (_wordUsed.size()/12); // 3 word will increase bonus 1

                if(_wordUsed.size() > 8) {
                    ConstraintLayout.LayoutParams layoutParams = (ConstraintLayout.LayoutParams) _layoutCity.getLayoutParams();
                    layoutParams.topMargin += 100;
                    layoutParams.bottomMargin -= 100;

                    _layoutCity.setLayoutParams(layoutParams);

                    ConstraintLayout.LayoutParams layoutParams2 = (ConstraintLayout.LayoutParams) _layoutTree.getLayoutParams();
                    layoutParams.topMargin += 100;
                    layoutParams.bottomMargin -= 100;

                    _layoutTree.setLayoutParams(layoutParams2);
                }

                _tvScore.setText("Score : " + String.valueOf(_score));
            }

            _etPlayer.setText(_endWord);
            if (!_endWord.equals(""))
                _etPlayer.setSelection(1);
        }
        else
        {
            _wrongCount++;
          _etPlayer.setText(_endWord);
            if (!_endWord.equals(""))
                _etPlayer.setSelection(1);
            _count -= _wrongCount*2;
        }
//        hide keyboard
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(_etPlayer.getWindowToken(), 0);
        _etPlayer.requestFocus();
        //imm.toggleSoftInput(InputMethodManager.SHOW_FORCED,0);

    }

    public String findNextWord()
    {
        int pos = (int)_endWord.charAt(0) - 97;
        for(int i  = _listbyFirstChar[pos].size(); i > -1; i-- ){
            Random r = new Random();
            int index = r.nextInt(i);
            if(_wordUsed.contains(_listbyFirstChar[pos].get(index)) == false)
            {
                String t = _listbyFirstChar[pos].get(index);
                _listbyFirstChar[pos].remove(index);
                return t;
            }

        }

        return "";
    }

}