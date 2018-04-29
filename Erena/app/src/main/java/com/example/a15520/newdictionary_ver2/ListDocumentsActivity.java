package com.example.a15520.newdictionary_ver2;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

public class ListDocumentsActivity extends AppCompatActivity {

    ListView _gvTopic;
    ArrayList<IdiomModel> _listTopic = new ArrayList<>();
    IdiomAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_documents);

        _gvTopic = (ListView) findViewById(R.id.gridTopic);

        _listTopic.add(new IdiomModel("Clauses", "Description"));//,R.drawable.clauses));
        _listTopic.add(new IdiomModel("IrregularVerbs", "Description"));//,R.drawable.irregularverbs));
        _listTopic.add(new IdiomModel("Phrases", "Description"));//,R.drawable.phrases));
        _listTopic.add(new IdiomModel("Popular", "Description"));//,R.drawable.popular));
        _listTopic.add(new IdiomModel("Sentences", "Description"));//,R.drawable.sentences));
        _listTopic.add(new IdiomModel("Tenses", "Description"));//,R.drawable.tenses));
        _listTopic.add(new IdiomModel("Topics", "Description"));//,R.drawable.topics));
        _listTopic.add(new IdiomModel("Words", "Description"));//,R.drawable.words));

        adapter = new IdiomAdapter(_listTopic, this);
        _gvTopic.setAdapter(adapter);

        _gvTopic.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Toast.makeText(ListDocumentsActivity.this,"Loading Document",Toast.LENGTH_SHORT).show();
                Bundle bundle = new Bundle();
                bundle.putString("Name",_listTopic.get(i).get_idiom());
                Intent t = new Intent(ListDocumentsActivity.this, DocumentActivity.class);
                t.putExtra("Topic",bundle);
                startActivity(t);
            }
        });


    }
}
