package com.example.a15520.newdictionary_ver2;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.ColorDrawable;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;


public class Game3Activity extends AppCompatActivity {

    private ArrayList<String> listWord = new ArrayList<>();
    private RecyclerView _rvHint , _rvAnswer;
    WordRecyclerAdapter _adapterHint , _adapterAnswer;
    private TextView _tvHint , _tvScore , _tvTime ;
    private String topic=  new String();
    private String _answer;
    private ArrayList<WordCharacter> controlList = new ArrayList<>();
    private int _score,_count;
    private CountDownTimer _timer;
    private boolean _isShowingDialog;
    private AnimationDrawable _animation;
    private int _wrongCount = 0;
    private TextView tvChange;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game3);


        Intent intent = getIntent();
        Bundle bundle = intent.getBundleExtra("Topic");
        topic = bundle.getString("Name");

        _score = 0;

        _count = 180;

        tvChange = (TextView) findViewById(R.id.tvChange);
        tvChange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                _wrongCount++;
                _count -= _wrongCount*2;

                Toast.makeText(Game3Activity.this,_answer,Toast.LENGTH_SHORT).show();
                NewQuiz();

            }
        });

        _tvScore = (TextView)findViewById(R.id.score3);
        _tvScore.setText("0");
        _tvTime = (TextView)findViewById(R.id.time3);

        _tvHint = (TextView)findViewById(R.id.tvHint3);
        listWord = GlobalVariable.getInstance().getTopicMap().get(topic);

        _rvHint = (RecyclerView) findViewById(R.id.rvHint);
        _rvAnswer = (RecyclerView) findViewById(R.id.rvAnswer);

        _adapterHint = new  WordRecyclerAdapter(createData(),this,0);
        _adapterAnswer = new WordRecyclerAdapter(createAnswerSlot(),this,1);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);

        _rvHint.setLayoutManager(layoutManager);
        _rvHint.setAdapter(_adapterHint);

        LinearLayoutManager layoutManager1 = new LinearLayoutManager(getApplicationContext());
        layoutManager1.setOrientation(LinearLayoutManager.HORIZONTAL);

        _rvAnswer.setLayoutManager(layoutManager1);
        _rvAnswer.setAdapter(_adapterAnswer);


        _adapterHint.setOnItemClickedListener(new WordRecyclerAdapter.OnItemClickedListener() {
            @Override
            public void onItemClick(WordCharacter dataSnap , int position) {
                if(!dataSnap._character.equals("")) {

                    _adapterHint.setData(new WordCharacter(0,""),position);
                    controlList.add(dataSnap);
                    _rvHint.setAdapter(_adapterHint);
                    _rvHint.scrollToPosition(dataSnap._position);

                    _adapterAnswer.Add(dataSnap);
                    _rvAnswer.setAdapter(_adapterAnswer);
                    _rvAnswer.scrollToPosition(_adapterAnswer.count - 1);

                    if(_adapterAnswer.count == _answer.length())
                        Answer();
                }
            }
        });

        _adapterAnswer.setOnItemClickedListener(new WordRecyclerAdapter.OnItemClickedListener() {
            @Override
            public void onItemClick(WordCharacter dataSnap , int position) {
                    if(!dataSnap._character.equals("")) {
                        _adapterHint.setData(dataSnap,controlList.get(controlList.indexOf(dataSnap))._position);
                        int pos = controlList.get(controlList.indexOf(dataSnap))._position;
                        controlList.remove(dataSnap);
                        _rvHint.setAdapter(_adapterHint);
                        _rvHint.scrollToPosition(pos);

                        _adapterAnswer.Remove(position);
                        _rvAnswer.setAdapter(_adapterAnswer);
                        _rvAnswer.scrollToPosition(_adapterAnswer.count);


                    }
                    else
                    {
                        if (dataSnap._position == 999)
                        {
                            NewQuiz();
                        }
                    }

            }
        });

        _isShowingDialog = true;

        _timer = new CountDownTimer(181000, 1000){
            public void onTick(long millisUntilFinished ){
                _count--;
                if(_count < 0) {
                    _count = 0;
                    if(_isShowingDialog == true) {
                        CustomDialog_Next cdd = new CustomDialog_Next(Game3Activity.this,_score,3);
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

    private void Answer() {


        if(_adapterAnswer.getData().equals(_answer)) {
            controlList.clear();
            _score += 20;
            _tvScore.setText(String.valueOf(_score));
            listWord.remove(_answer);
            if (listWord.isEmpty())
                _count = 0;

            _adapterAnswer.checkCorrect(true);
        }
        else {
            _wrongCount++;
            _count -= _wrongCount*2;

            Toast.makeText(Game3Activity.this,_answer,Toast.LENGTH_SHORT).show();
        }


        }

        public void NewQuiz()
        {
            _adapterHint.setListData(createData());
            _adapterAnswer.setListData(createAnswerSlot());
            _adapterAnswer.reset();
            _rvHint.setAdapter(_adapterHint);
            _rvAnswer.setAdapter(_adapterAnswer);



        }
    private ArrayList<WordCharacter> createAnswerSlot() {
        int i = _answer.length();
        ArrayList<WordCharacter> _return = new ArrayList<>();
        for(int t = 0 ; t < i ; t++)
        {
            _return.add(new WordCharacter(0,""));
        }
        return _return;
    }



    private ArrayList<WordCharacter> createData() {
        Random r = new Random();
        if(listWord.size() == 0) {
            _count = 0;
            return null;
        }
        int i = r.nextInt(listWord.size());
        String word = listWord.get(i);
        while (word.length() == 0)
        {
            i = r.nextInt(listWord.size());
            word = listWord.get(i);
        }
        _answer = word;
        String hint = new String();
        String txt = new String();
//        if(topic.equals("Entertainment") || topic.equals("Food and drink"))
//            hint = GlobalVariable.getInstance().EE.Find(word);
//        else {
            String[] paths = GlobalVariable.getInstance().getEV().Find(word).split("\n");
            for (String path : paths) {
                String check = "";

                if (path.length() >= 2) {
                    check = path.substring(0, 2);
                }
                if (check.equals("- ")) {
                    hint = path.replace("$", "");
                    break;
                }
            }
        //}
        txt = "<div><font color=\"" + "#ff0000" + "\" size=\"30\">" + "Topic : " + topic  + "</font></div>" +
                "<p><span style=\"color: #3366ff; font-size: 18pt;\"><strong>Hint : length "+ _answer.length() + "</strong></span></p>" +
                "<p><span style=\"font-size: 15pt;\"><strong>" + hint + "</strong></span></p>";

        _tvHint.setText(Html.fromHtml(txt));

        ArrayList<String> chars;
        String[] temp = word.toLowerCase().split("");

        // Random character in Word


        for (int j = 0 ; j < 10 ; j++)
        {
            int index1= r.nextInt(word.length());
            int index2= index1;
            while (index1 == index2)
                index2= r.nextInt(word.length());

            String swap = temp[index1];
            temp[index1] = temp[index2];
            temp[index2] = swap;

        }
//


            chars = new ArrayList<String>(Arrays.asList(temp));
            chars.remove("");

        ArrayList<WordCharacter> _return = new ArrayList<>();
        for (int f = 0 ; f < chars.size();f++) {
            _return.add(new WordCharacter(f,chars.get(f)));
        }

        return _return;
    }
}



class WordCharacter
{
    public int _position = 0;

    public WordCharacter(int _position, String _character) {
        this._position = _position;
        this._character = _character;
    }

    public String _character = "";

}