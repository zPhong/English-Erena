package com.example.a15520.newdictionary_ver2;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class Game3GuideActivity extends AppCompatActivity {

    Button _btnStartGame;
    TextView _tvGuide;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game3_guide);

        _btnStartGame = (Button) findViewById(R.id.btnStartGame3);

        _btnStartGame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Game3GuideActivity.this, PreGame3Activity.class);
                startActivity(intent);
                finish();
            }
        });

        //region add Guide for textView by HTML
        _tvGuide = (TextView) findViewById(R.id.tvGame3Guide);
        String htmlsource =
                "<h2 style=\"text-align: center;\"><span style=\"color: #fc7677;\"><strong>Guide for ChooChoo-Train</strong></span></h2>\n" +
                        "<p><span style=\"color: #000000;\"><strong>1.All the lorries are SCRAMBLED and you will have to rearrange them for the train to depart.</strong></span></p>\n" +
                        "<p><span style=\"color: #000000;\"><strong>2. To rearrange lorries, for an amount of time   tap on red tiles in the wished left to right order.</strong></span></p>\n" +
                        "<p><span style=\"color: #000000;\"><strong>3. If the answer is incorrect or the time runs out, you will be given the correct answer at the bottom of the screen.</strong></span></p>\n" +
                        "<p><span style=\"color: #000000;\"><strong>4. If you want to rearrange different train, just simply tap on the head of the present one.</strong></span></p>\n" +
                        "<p>&nbsp;</p>\n" +
                        "<p>&nbsp;</p>\n" +
                        "<p style=\"text-align: center;\"><span style=\"text-align: center; color: #00ff00;\"><span style=\"color: #000000;\"><strong><span style=\"color: #ff0000;\"><span style=\"color: #000000;\"><span style=\"color: #ff0000;\"><span style=\"color: #000000;\">Have fun and get Best Score</span></span></span></span></strong></span></span></p>\n" +
                        "<pre id=\"tw-target-text\" class=\"tw-data-text tw-ta tw-text-medium\" dir=\"ltr\" data-placeholder=\"Translation\" data-fulltext=\"\">&nbsp;</pre>\n" +
                        "<p>&nbsp;</p>";


        _tvGuide.setText(Html.fromHtml(htmlsource));
        //endregion
    }
}