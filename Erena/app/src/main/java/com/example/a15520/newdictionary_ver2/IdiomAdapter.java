package com.example.a15520.newdictionary_ver2;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.support.constraint.ConstraintLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.mikhaellopez.circularimageview.CircularImageView;

import java.util.ArrayList;


public class IdiomAdapter extends BaseAdapter {
    private ArrayList<IdiomModel> arrayIdiom;
    private Context context;

    public IdiomAdapter(ArrayList<IdiomModel> arrayIdiom, Context context)
    {
        this.arrayIdiom = arrayIdiom;

        this.context = context;
    }


    @Override
    public int getCount() {
        return arrayIdiom.size();
    }

    @Override
    public Object getItem(int i) {
        return arrayIdiom.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }
    private class ViewHolder
    {
        TextView idiom;
        TextView mean;
        LinearLayout layout;

    }

    @SuppressLint("ResourceType")
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = null;
        IdiomModel idiom = arrayIdiom.get(position);
        ViewHolder holder;
        if (convertView == null)
        {
            v = LayoutInflater.from(context).inflate(R.layout.list_idiom, parent, false);
            holder= new ViewHolder();
            holder.idiom= (TextView) v.findViewById(R.id.tv_idiom);
            holder.mean =(TextView) v.findViewById(R.id.tv_mean_idiom);
            holder.layout = (LinearLayout) v.findViewById(R.id.idiomlayout);

            holder.idiom.setText(idiom.get_idiom());
            holder.mean.setText(idiom.get_mean());

            if(position%2 != 0)
            {
                holder.layout.setBackgroundResource(R.color.White);
                holder.idiom.setTextColor(R.color.ColorTheme);
                holder.mean.setTextColor(R.color.ColorTheme);
            }
            else
            {

                holder.layout.setBackgroundResource(R.color.ColorTheme);
                holder.idiom.setTextColor(R.color.White);
                holder.mean.setTextColor(R.color.White);
            }
            v.setTag(holder);

        }
        else
        {
            v = convertView;
            holder = (ViewHolder) v.getTag();

            holder.idiom.setText(idiom.get_idiom());
            holder.mean.setText(idiom.get_mean());

            if(position%2 != 0)
            {
                holder.layout.setBackgroundResource(R.color.White);
                holder.idiom.setTextColor(R.color.ColorTheme);
                holder.mean.setTextColor(R.color.ColorTheme);
            }
            else
            {

                holder.layout.setBackgroundResource(R.color.ColorTheme);
                holder.idiom.setTextColor(R.color.White);
                holder.mean.setTextColor(R.color.White);
            }
            v.setTag(holder);
        }
        return v;
    }
}
