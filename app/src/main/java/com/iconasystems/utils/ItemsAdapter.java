package com.iconasystems.utils;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

import android.content.Context;
import android.util.Log;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.etsy.android.grid.util.DynamicHeightImageView;
import com.etsy.android.grid.util.DynamicHeightTextView;
import com.iconasystems.Constants;
import com.iconasystems.gula.R;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.squareup.picasso.Picasso;

/**
 * ADAPTER
 */

public class ItemsAdapter extends BaseAdapter {
    private static final SparseArray<Double> sPositionHeightRatios = new SparseArray<Double>();
    private final DisplayImageOptions options;
    private Random mRandom;
    private LayoutInflater mLayoutInflater;
    private Context _context;
    private ArrayList<HashMap<String, String>> dataList;

    public ItemsAdapter(Context context, ArrayList<HashMap<String, String>> dataList) {
        mLayoutInflater = LayoutInflater.from(context);
        this._context = context;
        this.dataList = dataList;
        this.mRandom = new Random();

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
    public View getView(final int position, View convertView, ViewGroup parent) {

        ViewHolder vh = new ViewHolder();
        if (mLayoutInflater == null) {
            mLayoutInflater = (LayoutInflater) _context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }
        if (convertView == null) {
            convertView = mLayoutInflater.inflate(R.layout.grid_item, parent, false);
        }

        vh.photo = (DynamicHeightImageView) convertView.findViewById(R.id.photo);
        // vh.mDescription = (DynamicHeightTextView) convertView.findViewById(R.id.item_name_grid);
        vh.mItemName = (DynamicHeightTextView) convertView.findViewById(R.id.item_name_grid);
        vh.mItemId = (TextView) convertView.findViewById(R.id.item_id);
        vh.mPrice = (TextView) convertView.findViewById(R.id.item_price_grid);

        double positionHeight = getPositionRatio(position);
        vh.photo.setHeightRatio(positionHeight);

        HashMap<String, String> item;
        item = dataList.get(position);

        String item_name = item.get(Constants.NameConstants.TAG_ITEM_NAME);
        String description = item.get(Constants.NameConstants.TAG_DESCRIPTION);
        String price = item.get(Constants.NameConstants.TAG_ITEM_PRICE);
        String item_id = item.get(Constants.NameConstants.TAG_ITEM_ID);
        String imageUrl = Constants.UrlConstants.url_items_dir + "/" + item.get(Constants.NameConstants.TAG_ITEM_IMAGE);

        vh.mItemId.setText(item_id);
        vh.mPrice.setText(price);
        vh.mItemName.setText(item_name);

        ImageLoader.getInstance().displayImage(imageUrl, vh.photo, options);
        return convertView;
    }

    private double getPositionRatio(final int position) {
        double ratio = sPositionHeightRatios.get(position, 0.0);
        // if not yet done generate and stash the columns height
        // in our real world scenario this will be determined by
        // some match based on the known height and width of the image
        // and maybe a helpful way to get the column height!
        if (ratio == 0) {
            ratio = getRandomHeightRatio();
            sPositionHeightRatios.append(position, ratio);
        }

        return ratio;
    }

    private double getRandomHeightRatio() {
        return (mRandom.nextDouble() / 2.0) + 1.0; // height will be 1.0 - 1.5 the width
    }

    static class ViewHolder {
        DynamicHeightImageView photo;
        DynamicHeightTextView mItemName;
        DynamicHeightTextView mDescription;
        TextView mPrice;
        TextView mItemId;
    }
}