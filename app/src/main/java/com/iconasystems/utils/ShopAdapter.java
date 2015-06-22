package com.iconasystems.utils;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.iconasystems.gula.R;

import java.util.ArrayList;

/**
 * Created by Raymo on 6/9/2015.
 */
public class ShopAdapter extends BaseAdapter {
    ArrayList<Integer> mList;
    Context context;
    LayoutInflater layoutInflater;

    public ShopAdapter(ArrayList<Integer> mList, Context context) {
        this.mList = mList;
        this.context = context;
    }


    @Override
    public int getCount() {
        return this.mList.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder vh = new ViewHolder();
        if (layoutInflater == null) {
            layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }
        if (convertView == null) {
            convertView = layoutInflater.inflate(R.layout.shop_list_item, parent, false);
        }
        vh.imageView = (ImageView) convertView.findViewById(R.id.shop_category_photo);
        vh.imageView.setImageResource(mList.get(position));
        return convertView;
    }

    public static class ViewHolder {
        ImageView imageView;
    }
}
