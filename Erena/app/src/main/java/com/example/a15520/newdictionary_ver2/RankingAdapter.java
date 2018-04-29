package com.example.a15520.newdictionary_ver2;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.mikhaellopez.circularimageview.CircularImageView;


import java.util.ArrayList;


public class RankingAdapter extends BaseAdapter {
    private final Typeface courierBd;
    private ArrayList<UserModel> arrayRanking;
    private Context context;
    private int type = 1;

    public  RankingAdapter(ArrayList<UserModel> arrayRanking, Context context , int type)
    {
        this.arrayRanking = arrayRanking;

        this.context = context;
        this.type = type;
        courierBd = Typeface.createFromAsset(this.context.getAssets(),"fonts/courbd.ttf");

    }


    @Override
    public int getCount() {
        return arrayRanking.size();
    }

    @Override
    public Object getItem(int i) {
        return arrayRanking.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }
    private class ViewHolder
    {
        TextView username;
        TextView score;
        ImageView rank;

    }
    public int getRankResid(String rank)
    {
        int id = 1 ;
        switch (rank)
        {
            case "1": id = R.drawable.ic_rank_1;break;
            case "2": id = R.drawable.ic_rank_2;break;
            case "3": id = R.drawable.ic_rank_3;break;
            case "4": id = R.drawable.ic_rank_4;break;
            case "5": id = R.drawable.ic_rank_5;break;
            case "6": id = R.drawable.ic_rank_6;break;
            case "7": id = R.drawable.ic_rank_7;break;
            case "8": id = R.drawable.ic_rank_8;break;
            case "9": id = R.drawable.ic_rank_9;break;
            default: id = R.drawable.ic_rank_1;break;
        }
        return  id;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = null;
        UserModel ranking = arrayRanking.get(position);
        ViewHolder holder;
        if (convertView == null)
        {
            v = LayoutInflater.from(context).inflate(R.layout.item_ranking, parent, false);
            holder= new ViewHolder();
            holder.username= (TextView) v.findViewById(R.id.tv_username_ranking);
            holder.score =(TextView) v.findViewById(R.id.tv_score_ranking);
            holder.rank = (ImageView) v.findViewById(R.id.iv_ranking);

            holder.username.setTypeface(courierBd);
            holder.score.setTypeface(courierBd);

            holder.username.setText(ranking.getName());
            if(type == 1)
                 holder.score.setText(ranking.getScore1().toString());
            if(type == 2)
                holder.score.setText(ranking.getScore2().toString());
            if(type == 3)
                holder.score.setText(ranking.getScore3().toString());

            String t = ranking.getRank();
            holder.rank.setImageResource(getRankResid(t));//ranking.getRank()));
            v.setTag(holder);

        }
        else
        {
            v = convertView;
            holder = (ViewHolder) v.getTag();


            holder.username.setTypeface(courierBd);
            holder.score.setTypeface(courierBd);

            holder.username.setText(ranking.getName());
            if(type == 1)
                holder.score.setText(ranking.getScore1().toString());
            if(type == 2)
                holder.score.setText(ranking.getScore2().toString());
            if(type == 3)
                holder.score.setText(ranking.getScore3().toString());

            String t = ranking.getRank();
            holder.rank.setImageResource(getRankResid(t));//ranking.getRank()));
            v.setTag(holder);
        }
        return v;
    }
}
