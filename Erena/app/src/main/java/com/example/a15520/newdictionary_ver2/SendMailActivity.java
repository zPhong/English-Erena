package com.example.a15520.newdictionary_ver2;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import de.cketti.mailto.EmailIntentBuilder;

public class SendMailActivity extends AppCompatActivity {

    Button btnSend;
    EditText edtTile,edtMess, edtEdSend;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_mail);

        edtTile = (EditText) findViewById(R.id.ed_tilte_mail);
        edtEdSend = (EditText) findViewById(R.id.ed_send_mail);
        edtMess = (EditText) findViewById(R.id.ed_mess_mail);
        btnSend = (Button)findViewById(R.id.btnGui);

        String s= GlobalVariable.getInstance().getUser().getEmail();
        edtEdSend.setText(s);
        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                edtMess.getText();
                edtTile.getText();
                EmailIntentBuilder.from(SendMailActivity.this)
                        .to("15520617@gm.uit.edu.vn")
                        .cc("15520617@gm.uit.edu.vn")
                        .bcc("15520617@gm.uit.edu.vn")
                        .subject(edtTile.getText().toString())
                        .body(edtMess.getText().toString())
                        .start();
            }
        });




        //Intent intent = getIntent();
        // Bundle bundle = intent.getBundleExtra("item");
        // edtTile.setText("[Giúp Đở][Chủ Đề: "+bundle.getString("Name")+" ]");
    }
}

