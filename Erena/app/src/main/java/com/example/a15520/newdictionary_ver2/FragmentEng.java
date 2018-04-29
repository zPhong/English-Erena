package com.example.a15520.newdictionary_ver2;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.speech.tts.TextToSpeech;
import android.support.v4.app.Fragment;
import android.text.Spannable;
import android.text.style.ForegroundColorSpan;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Locale;

import static android.app.Activity.RESULT_OK;
import static android.view.inputmethod.EditorInfo.IME_ACTION_DONE;

/**
 * Created by 15520 on 12/22/2017.
 */

public class FragmentEng extends Fragment {


    //region Tab1
    private EditText _etWord1;
    private Button _btnSearch1, _btnSpeak;
    private TextView _tvMean1;

    private Button _btnAudio;
    private TextToSpeech voice;
    //endregion
    private final int REQ_CODE_SPEECH_INPUT = 100;


    public FragmentEng() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.dict_tab1fragment, container, false);

        Typeface helvetica = Typeface.createFromAsset(getActivity().getAssets(),"fonts/HelveticaWorld-Regular.ttf");
        Typeface inconsolata = Typeface.createFromAsset(getActivity().getAssets(),"fonts/Inconsolata.otf");

        //region Tab1


        voice = new TextToSpeech(getActivity().getApplicationContext(), new TextToSpeech.OnInitListener()
        {
            @Override
            public void onInit(int i) {
                if(i != TextToSpeech.ERROR)
                    voice.setLanguage(Locale.UK);
            }
        });


        _etWord1 = (EditText)rootView.findViewById(R.id.etWord1);
        _etWord1.setTypeface(inconsolata);
        _tvMean1 = (TextView)rootView.findViewById(R.id.tvMean1);
        _tvMean1.setTypeface(helvetica);
        _btnSearch1 = (Button)rootView.findViewById(R.id.btnSearch1);
        _btnAudio = (Button)rootView.findViewById(R.id.btnAudio);
        _btnSpeak = (Button)rootView.findViewById((R.id.btnSpeaker));






        _btnSearch1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                doClickBtbSearch1();
            }
        });
        _etWord1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                _etWord1.setText("");

            }
        });

        _etWord1.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                boolean handled = false;
                if (i == IME_ACTION_DONE) {
                  /* Write your logic here that will be executed when user taps next button */
                    doClickBtbSearch1();

                    handled = true;
                }
                return handled;

            }
        });


        _btnAudio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                promptSpeechInput();
            }
        });
        _btnSpeak.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                voice.speak(_etWord1.getText().toString(),TextToSpeech.QUEUE_FLUSH,null);
            }
        });
        _btnSearch1.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                Resources res = getResources();
                Drawable img;
                if(motionEvent.getAction() == MotionEvent.ACTION_UP) {

                    img = res.getDrawable(android.R.drawable.ic_menu_search);
                    view.setBackground(img);

                }
                if(motionEvent.getAction() == MotionEvent.ACTION_DOWN) {

                    img = res.getDrawable(android.R.drawable.ic_search_category_default);
                    view.setBackground(img);

                }
                return false;
            }
        });
        //endregion
        // Inflate the layout for this fragment
        return rootView;
    }

    private void doClickBtbSearch1() {

        String text = _etWord1.getText().toString().trim();
        String mean;
        _tvMean1.setText("");

        mean = GlobalVariable.getInstance().getEV().Find(text);
        OutputWord(mean, _tvMean1);
        //hide keyboard
        InputMethodManager imm = (InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(_etWord1.getWindowToken(), 0);


    }





    private void promptSpeechInput() {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT,
                getString(R.string.speech_prompt));
        try {
            startActivityForResult(intent, REQ_CODE_SPEECH_INPUT);
        } catch (ActivityNotFoundException a) {
//            Toast.makeText(MainActivity.this,
//                    getString(R.string.speech_not_supported),
//                    Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Receiving speech input
     * */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case REQ_CODE_SPEECH_INPUT: {
                if (resultCode == RESULT_OK && null != data) {

                    ArrayList<String> result = data
                            .getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    _etWord1.setText(result.get(0));
                }
                break;
            }

        }
    }
    //region Read Data and Output


    public void OutputWord(String word,TextView tvOutput)
    {
        String[] paths = word.split("\n");
        for(String path : paths)
        {
            String check = "";

            if(path.length() >=2 )
            {
                check = path.substring(0,2);
            }
            switch (check)
            {

                case "* ":appendColoredText(tvOutput,"\n" + path.replace("$",""), Color.BLUE); break;
                case "- ": appendColoredText(tvOutput,"\n" + path.replace("$",""),Color.WHITE); break;
                case "!$": appendColoredText(tvOutput,"\n" + path.substring(2).replace("$",""),Color.RED); break;
                case "=$": appendColoredText(tvOutput,"\n" + path.substring(2).replace("$",""),Color.CYAN); break;
                case "--": appendColoredText(tvOutput,"\n" +path,Color.WHITE);
                default:
                    appendColoredText(tvOutput,"\n" + path.replace("@","").replace("$",""),Color.WHITE); break;
            }
        }

    }
    public static void appendColoredText(TextView tv, String text, int color) {
        int start = tv.getText().length();
        tv.append(text);
        int end = tv.getText().length();

        Spannable spannableText = (Spannable) tv.getText();
        spannableText.setSpan(new ForegroundColorSpan(color), start, end, 0);
    }
    //endregion

}
