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

/**
 * Created by Raymo on 6/21/2015.
 */
public class ShopsAdapter extends BaseAdapter {
    private final DisplayImageOptions options;
    private Context context;
    private ArrayList<HashMap<String, String>> dataList;
    private LayoutInflater inflater;

    public ShopsAdapter(Context context, ArrayList<HashMap<String, String>> dataList) {
        this.context = context;
        this.dataList = dataList;

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
        ViewHolder viewHolder = new ViewHolder();
        if (inflater == null) {
            inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.shop_item, parent, false);
        }

        viewHolder.mDescription = (TextView) convertView.findViewById(R.id.shop_description);
        viewHolder.mLocation = (TextView) convertView.findViewById(R.id.shop_location);
        viewHolder.mShopName = (TextView) convertView.findViewById(R.id.shop_name);
        viewHolder.mShopCategory = (TextView) convertView.findViewById(R.id.shop_category);
        viewHolder.mShopPhoto = (ImageView) convertView.findViewById(R.id.shop_image);
        viewHolder.mShopId = (TextView) convertView.findViewById(R.id.shop_item_id);

        HashMap<String, String> map;
        map = dataList.get(position);

        String shop_name = map.get(Constants.NameConstants.TAG_SHOP_NAME);
        String category = map.get(Constants.NameConstants.TAG_CATEGORY);
        String shop_id = map.get(Constants.NameConstants.TAG_SHOP_ID);
        String description = map.get(Constants.NameConstants.TAG_DESCRIPTION);
        String location = map.get(Constants.NameConstants.TAG_LOCATION);
        String image = map.get(Constants.NameConstants.TAG_SHOP_IMAGE);
        String image_url = Constants.UrlConstants.url_shop_images + "/" + image;

        viewHolder.mShopName.setText(shop_name);
        viewHolder.mLocation.setText(location);
        viewHolder.mDescription.setText(description);
        viewHolder.mShopCategory.setText(category);
        viewHolder.mShopId.setText(shop_id);

        ImageLoader.getInstance().displayImage(image_url, viewHolder.mShopPhoto, options);

        return convertView;
    }

    static class ViewHolder {
        ImageView mShopPhoto;
        TextView mShopName;
        TextView mDescription;
        TextView mLocation;
        TextView mShopCategory;
        TextView mShopId;
    }
}
