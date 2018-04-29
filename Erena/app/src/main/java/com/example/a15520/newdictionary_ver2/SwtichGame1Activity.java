package com.example.a15520.newdictionary_ver2;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

public class SwtichGame1Activity extends AppCompatActivity {
    Button btnTo2P;
    Button btnToComputer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_swtich_game1);
        btnTo2P=(Button) findViewById(R.id.btnTo2P);
        btnToComputer=(Button) findViewById(R.id.btnToComputer);
        btnTo2P.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(SwtichGame1Activity.this,ListEndpointActivity.class);
                startActivity(i);
                finish();
            }
        });
        btnToComputer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(SwtichGame1Activity.this,Game1Activity.class);
                startActivity(i);
                finish();
            }
        });
    }
}

