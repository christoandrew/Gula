package com.iconasystems.utils;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.iconasystems.Constants;
import com.iconasystems.gula.R;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;
import java.util.HashMap;


public class CategoryAdapter extends BaseAdapter {
    private final DisplayImageOptions options;
    ArrayList<HashMap<String, String>> dataList;
    Context _context;
    LayoutInflater inflater;

    public CategoryAdapter(Context context, ArrayList<HashMap<String, String>> dataList) {
        this.dataList = dataList;
        this._context = context;

        options = new DisplayImageOptions.Builder()
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .considerExifParams(true)
                .build();
    }

    @Override
    public int getCount() {
        return dataList.size();
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
        if (inflater == null) {
            inflater = (LayoutInflater) _context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        if (convertView == null) {
            convertView = inflater.inflate(R.layout.cat_item, parent, false);
        }

        ViewHolder viewHolder = new ViewHolder();
        viewHolder.mCatId = (TextView) convertView.findViewById(R.id.cat_id);
        viewHolder.mCatName = (TextView) convertView.findViewById(R.id.cat_name);
        viewHolder.mCatPhoto = (ImageView) convertView.findViewById(R.id.shop_category_photo);

        HashMap<String, String> category;
        category = dataList.get(position);
        String cat_name = category.get(Constants.NameConstants.TAG_CAT_NAME);
        String cat_id = category.get(Constants.NameConstants.TAG_CAT_ID);
        String image = category.get(Constants.NameConstants.TAG_CAT_IMAGE);
        String catImage = Constants.UrlConstants.url_cat_image + "/" + image;

        viewHolder.mCatId.setText(cat_id);
        viewHolder.mCatName.setText(cat_name);

        ImageLoader.getInstance().displayImage(catImage, viewHolder.mCatPhoto, options);

        return convertView;
    }

    static class ViewHolder {
        public ImageView mCatPhoto;
        public TextView mCatName;
        public TextView mCatId;

    }
}
