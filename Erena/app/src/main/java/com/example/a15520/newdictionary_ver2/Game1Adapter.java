package com.example.a15520.newdictionary_ver2;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;


public class Game1Adapter extends BaseAdapter {
    private ArrayList<String> array;
    private Context context;
    private Typeface courierBd ;
    public Game1Adapter(ArrayList<String> array, Context context)
    {
        this.array = array;

        this.context = context;

        courierBd = Typeface.createFromAsset(context.getAssets(),"fonts/courbd.ttf");

    }

    public void Add(String text)
    {
        array.add(text);
    }

    @Override
    public int getCount() {
        return array.size();
    }

    @Override
    public Object getItem(int i) {
        return array.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }
    private class ViewHolder
    {
        TextView word;

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = null;
        String word = array.get(position);
        ViewHolder holder;
        if (convertView == null)
        {
            v = LayoutInflater.from(context).inflate(R.layout.list_word, parent, false);
            holder= new ViewHolder();
            holder.word = (TextView) v.findViewById(R.id.tvWordGame1);
            holder.word.setTypeface(courierBd);

            holder.word.setTextSize(50 - (word.length() - 4)*2);
            holder.word.setText(word);

            v.setTag(holder);

        }
        else
        {
            v = convertView;
            holder = (ViewHolder) v.getTag();
            holder.word.setTextSize(50 - (word.length() - 4)*2);
            holder.word.setTypeface(courierBd);

            holder.word.setText(word);
            v.setTag(holder);
        }
        return v;
    }
}
