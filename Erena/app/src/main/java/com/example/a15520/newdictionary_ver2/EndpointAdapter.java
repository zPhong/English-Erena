package com.example.a15520.newdictionary_ver2;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by LÊHOÀNGNAM on 02-1-18.
 */



public class EndpointAdapter extends BaseAdapter{

    private ArrayList<Endpoint> arrayEndpoint;
    private Context context;

    public EndpointAdapter(ArrayList<Endpoint> arrayEndpoint, Context context)
    {
        this.arrayEndpoint = arrayEndpoint;

        this.context = context;
    }

    public  void Clear()
    {
        arrayEndpoint.clear();
    }

    public void Add(Endpoint endpoint)
    {
        arrayEndpoint.add(endpoint);
    }

    public Endpoint getEndpointbyPosition(int pos)
    {
        return arrayEndpoint.get(pos);
    }

    public  void removeEndpoint(Endpoint endpoint)
    {
        arrayEndpoint.remove(endpoint);
    }

    @Override
    public int getCount() {
        return arrayEndpoint.size();
    }

    @Override
    public Object getItem(int i) {
        return arrayEndpoint.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }
    private class ViewHolder
    {
        TextView endpoint;
        Button btnConnect;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = null;
        Endpoint endpoint= arrayEndpoint.get(position);
        ViewHolder holder;
        if (convertView == null)
        {
            v = LayoutInflater.from(context).inflate(R.layout.list_idiom, parent, false);
            holder= new ViewHolder();
            holder.endpoint= (TextView) v.findViewById(R.id.tv_idiom);
            holder.endpoint =(TextView) v.findViewById(R.id.tv_mean_idiom);
            holder.endpoint.setText(endpoint.toString());
            v.setTag(holder);

        }
        else
        {
            v = convertView;
            holder = (ViewHolder) v.getTag();
            //holder.avatar= (CircleImageView) v.findViewById(R.id.ivAvatarTab3UserList);
            // holder.username= (TextView) v.findViewById(R.id.tvUserNameTab3UserList);
            //holder.recentchat= (TextView) v.findViewById(R.id.tvRecentChatTab3UserList);
            //holder.time = (TextView) v.findViewById(R.id.tvTimeTab3UserList);
            holder.endpoint.setText(endpoint.toString());
            v.setTag(holder);
        }
        return v;
    }
}
