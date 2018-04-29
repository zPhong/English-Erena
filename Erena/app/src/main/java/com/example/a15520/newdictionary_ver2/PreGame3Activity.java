package com.example.a15520.newdictionary_ver2;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PreGame3Activity extends AppCompatActivity {
    private ListView _lvTopic;
    private ArrayList<String> _arrayTopic;
    private Game3Adapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pre_game3);

//        readData();

        _lvTopic = (ListView) findViewById(R.id.lvTopic);

        _arrayTopic = GlobalVariable.getInstance()._listTopic;

        adapter = new Game3Adapter(_arrayTopic,this);

        _lvTopic.setAdapter(adapter);

        _lvTopic.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Bundle bundle = new Bundle();
                bundle.putString("Name",_arrayTopic.get(i));

                Intent t = new Intent(PreGame3Activity.this,Game3Activity.class);
                t.putExtra("Topic",bundle);
                startActivity(t);
                finish();
            }
        });
    }

    public void readData()
    {
        //region Load Data tu txt
        String dictdata = new String();
        String data;
        InputStream in;
        in= getResources().openRawResource(R.raw.dictee);

        InputStreamReader inreader=new InputStreamReader(in);
        BufferedReader bufreader=new BufferedReader(inreader);
        StringBuilder builder=new StringBuilder();
        if(in!=null)
        {
            try
            {
                while((data = bufreader.readLine())!=null)
                {
                    builder.append(data);
                    builder.append("\n");

                }
                in.close();
                dictdata = builder.toString();
            }
            catch(IOException ex){
                Log.e("ERROR", ex.getMessage());
            }
        }
        //endregion
        //region create hashmap
        dictdata = dictdata.replace("\n\n\n","\n\n");
        String[] fullWords = dictdata.split(System.lineSeparator() + System.lineSeparator());

        for(String str : fullWords)
        {
            String[] t = str.split("\n");
            if(t.length != 2)
                Toast.makeText(this,str,Toast.LENGTH_SHORT).show();
            else
                if(str.contains("-spectator"))
                    Toast.makeText(this,str,Toast.LENGTH_SHORT).show();
                GlobalVariable.getInstance().EE.Add(t[0].replace("-",""),t[1].replace("!",""));


        }
        //endregion





    }

}
