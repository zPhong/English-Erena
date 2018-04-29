package com.example.a15520.newdictionary_ver2;

import android.graphics.pdf.PdfRenderer;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;

public class IdiomsActivity extends AppCompatActivity {

    private ArrayList<IdiomModel> _idioms = new ArrayList<>();
    private ListView _lvIdioms;
    private IdiomAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_idioms);


        _lvIdioms = (ListView) findViewById(R.id.lv_idioms);
        ReadData();
        adapter = new IdiomAdapter(_idioms, IdiomsActivity.this);
        _lvIdioms.setAdapter(adapter);




    }
    public void ReadData()
    {
        InputStream in = getResources().openRawResource(R.raw.idioms);
        String s = "";
        InputStreamReader inreader=new InputStreamReader(in);
        BufferedReader bufreader=new BufferedReader(inreader);

        if(in!=null)
        {
            try
            {
                while((s = bufreader.readLine())!=null)
                {
                   String[] str = s.split(":");
                   _idioms.add(new IdiomModel(str[0],str[1]));
                }
                in.close();

            }
            catch(IOException ex){
                Log.e("ERROR", ex.getMessage());
            }
        }
    }

}

