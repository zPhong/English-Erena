package com.example.a15520.newdictionary_ver2;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
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

import static android.view.inputmethod.EditorInfo.IME_ACTION_DONE;

/**
 * Created by 15520 on 12/22/2017.
 */

public class FragmentViet extends Fragment {

    //region Tab2

    private EditText _etWord2;
    private Button _btnSearch2;
    private TextView _tvMean2;
    //endregion
    public FragmentViet() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.dict_tab2fragment, container, false);

        Typeface helvetica = Typeface.createFromAsset(getActivity().getAssets(),"fonts/HelveticaWorld-Regular.ttf");

        //region tab2

        _etWord2 = (EditText)rootView.findViewById(R.id.etWord2);
        _etWord2.setTypeface(helvetica);
        _tvMean2 = (TextView)rootView.findViewById(R.id.tvMean2);
        _tvMean2.setTypeface(helvetica);
        _btnSearch2 = (Button)rootView.findViewById(R.id.btnSearch2);




        _btnSearch2.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                doClickBtbSearch2();
            }
        });
        _etWord2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                _etWord2.setText("");

            }
        });
        _btnSearch2.setOnTouchListener(new View.OnTouchListener() {
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

        _etWord2.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                boolean handled = false;
                if (i == IME_ACTION_DONE) {
                  /* Write your logic here that will be executed when user taps next button */
                    doClickBtbSearch2();

                    handled = true;
                }
                return handled;

            }
        });
//endregion
        // Inflate the layout for this fragment

        return rootView;
    }

    private void doClickBtbSearch2() {

        String text = _etWord2.getText().toString().trim();
        String mean;
        _tvMean2.setText("");

        mean = GlobalVariable.getInstance().getVE().Find(text);
        OutputWord(mean, _tvMean2);
        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(_etWord2.getWindowToken(), 0);


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
