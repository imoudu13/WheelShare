package com.example.projectstep4;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RatingBar;
import android.widget.TextView;

import java.util.List;

public class MyAdapter extends BaseAdapter {
    private List<ListModel> dataList;
    private LayoutInflater inflater;

    public MyAdapter(Context context, List<ListModel> dataList) {
        this.dataList = dataList;
        this.inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return dataList.size();
    }

    @Override
    public Object getItem(int position) {
        return dataList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;

        if (convertView == null) {
            convertView = inflater.inflate(R.layout.item_layout, parent, false);
            holder = new ViewHolder();
            holder.textName = convertView.findViewById(R.id.textView12);
            holder.ratingBar = convertView.findViewById(R.id.ratingBar);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        ListModel data = dataList.get(position);
        holder.textName.setText(data.getName());
        holder.ratingBar.setRating(data.getStarRating());

        return convertView;
    }

    static class ViewHolder {
        TextView textName;
        RatingBar ratingBar;
    }
}
