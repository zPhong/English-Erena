package com.example.a15520.newdictionary_ver2;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;


public class Game1GuideActivity extends Activity {
    private Button _btnStartGame;
    private TextView _tvGuide;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.game1_guide_dialog);

        _btnStartGame = (Button) findViewById(R.id.btnStartGame1);

        _btnStartGame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Game1GuideActivity.this, SwtichGame1Activity.class);
                startActivity(intent);
                finish();
            }
        });

        //region add Guide for textView by HTML
        _tvGuide =(TextView )findViewById(R.id.game1_guide);
        String htmlsource =
        "<h2 style=\"text-align: center;\"><span style=\"color: #fc7677;\"><strong>Guide for Babel Tower</strong></span></h2>\n" +
                "<p><span style=\"color: #000000;\"><strong>1. Co-op to build a tower so high that touch pierce through the sky</strong></span></p>\n" +
                "<p><span style=\"color: #000000;\"><strong>2. Every composed word have to begin with the character that is the last of the word given by AI/opponent users.</strong></span></p>\n" +
                "<p><span style=\"color: #000000;\"><strong>3. Type to the white box the answer, if correct, the tower will get higher a little bit and you gain score. If not, you get punished by losing time. You will have 2 minutes to start with.</strong></span></p>\n" +
                "<p><span style=\"color: #000000;\"><strong>&nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp;Ex:&nbsp; - AI's answer :&nbsp; Lik<span style=\"color: #db1360;\">e</span></strong></span></p>\n" +
                "<p>&nbsp;</p>\n" +
                "<p>&nbsp;</p>\n" +
                "<p style=\"text-align: center;\"><span style=\"text-align: center; color: #00ff00;\"><span style=\"color: #000000;\"><strong><span style=\"color: #ff0000;\"><span style=\"color: #000000;\"><span style=\"color: #ff0000;\"><span style=\"color: #000000;\">Have fun and get Best Score</span></span></span></span></strong></span></span></p>\n" +
                "<pre id=\"tw-target-text\" class=\"tw-data-text tw-ta tw-text-medium\" dir=\"ltr\" data-placeholder=\"Translation\" data-fulltext=\"\">&nbsp;</pre>\n" +
                "<p>&nbsp;</p>";

        _tvGuide.setText(Html.fromHtml(htmlsource));
        //endregion
    }
}
