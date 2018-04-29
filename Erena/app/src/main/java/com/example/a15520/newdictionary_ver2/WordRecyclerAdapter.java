package com.example.a15520.newdictionary_ver2;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 15520 on 12/30/2017.
 */
public class WordRecyclerAdapter extends RecyclerView.Adapter<WordRecyclerAdapter.RecyclerViewHolder> {
    private final Typeface courierBd;
    private final Context context;
    int count = 0;
    private ArrayList<WordCharacter> listData = new ArrayList<>();
    private int type = 0;
    boolean checktime = false;
    boolean isCorrect = false;


    public WordRecyclerAdapter(ArrayList<WordCharacter> listData , Context context, int type ) {
        this.listData = listData;
        this.type = type;
        this.context = context;
        courierBd = Typeface.createFromAsset(this.context.getAssets(),"fonts/courbd.ttf");

    }
    public void setData(WordCharacter data , int position)
    {
        listData.set(position,data);
    }

    public void setListData(ArrayList<WordCharacter> listData) {
        this.listData = listData;
    }

    public String getData()
    {
        String _return = "";
        for(WordCharacter character : listData)
        {
            _return += character._character;
        }
        return _return;
    }
    @SuppressLint("ResourceAsColor")
    @Override
    public RecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View itemview = inflater.inflate(R.layout.worditem, parent, false);

        if(type == 0) {
            return new RecyclerViewHolder(itemview);
        }

        itemview.findViewById(R.id.tvChar).setBackgroundResource(R.drawable.lorry);
        TextView t = (TextView) itemview.findViewById(R.id.tvChar);
        t.setTextColor(R.color.Game3background);
        return new RecyclerViewHolder(itemview);
    }


    @Override
    public void onBindViewHolder(final RecyclerViewHolder holder, final int position) {
        holder.txtHeader.setText(listData.get(position)._character);
        if(listData.get(position)._position == 999)
        {
            if(!isCorrect)
                holder.txtHeader.setBackgroundResource(R.drawable.trainwrong);
            else
                holder.txtHeader.setBackgroundResource(R.drawable.trainright);
        }
        holder.txtHeader.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(onItemClickedListener!= null && (checktime ==false || listData.get(position)._position == 999))
                {
                    onItemClickedListener.onItemClick(listData.get(position),position);
                }
            }
        });
    }

    public void Add(WordCharacter selectedChar) {
        if(!checktime) {
            listData.set(count, selectedChar);
            count++;
            if(count == listData.size())
            {
                listData.add(new WordCharacter(999,""));
                checktime= true;
            }
        }

    }

    public void Remove(int position)
    {
        listData.remove(position);
        listData.add(new WordCharacter(0,""));
        count--;
    }

    public void checkCorrect(boolean check)
    {
        if(check)
        {
            isCorrect = true;
            listData.remove(count);

            listData.add(new WordCharacter(999,""));

        }
    }

    public void reset() {
        count = 0;
        checktime = false;
        isCorrect = false;
    }

    public interface OnItemClickedListener{
        void onItemClick(WordCharacter dataSnap , int position);
    }

    private OnItemClickedListener onItemClickedListener;

    public void setOnItemClickedListener(OnItemClickedListener onItemClickedListener)
    {
        this.onItemClickedListener = onItemClickedListener;
    }
    @Override
    public int getItemCount() {
        return listData.size();
    }

    public class RecyclerViewHolder extends RecyclerView.ViewHolder {
        TextView txtHeader;

        public RecyclerViewHolder(View itemView) {
            super(itemView);
            txtHeader = (TextView) itemView.findViewById(R.id.tvChar);
        }

    }
}




