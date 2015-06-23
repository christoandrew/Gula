package com.iconasystems.gula;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.iconasystems.Constants;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by akena on 6/19/2015.
 */
public class AdapterForAppCart extends BaseAdapter {
    private ArrayList<HashMap<String,String>> mItemList;
    private LayoutInflater mInflater;
    private boolean mShowCheckbox;
    Context context;

    public AdapterForAppCart(Context context, ArrayList<HashMap<String,String>> list, LayoutInflater inflater, boolean showCheckbox) {
        mItemList = list;
        mInflater = inflater;
        mShowCheckbox = showCheckbox;
        this.context = context;
    }

    @Override
    public int getCount() {
        return mItemList.size();
    }

    @Override
    public Object getItem(int position) {
        return mItemList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewItem item;

        if(mInflater == null){
            mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.app_cart_item, parent, false);
        }

        item = new ViewItem();

        item.mItemImage = (ImageView) convertView
                .findViewById(R.id.item_image);

        item.mItemName = (TextView) convertView
                .findViewById(R.id.item_name);

        item.mPrice = (TextView) convertView
                .findViewById(R.id.item_price);

        item.mSelectedItem = (CheckBox) convertView
                .findViewById(R.id.cart_item_check);

        HashMap<String,String> curItem = mItemList.get(position);


        Picasso.with(context)
                .load(Constants.UrlConstants.url_items_dir + "/" +curItem.get(Constants.NameConstants.TAG_ITEM_IMAGE))
                .placeholder(R.drawable.imgbg)
                .error(R.drawable.ic_launcher)
                .resize(700, 800)
                .into(item.mItemImage);

        item.mItemName.setText(curItem.get(Constants.NameConstants.TAG_ITEM_NAME));
        item.mPrice.setText("$"+curItem.get(Constants.NameConstants.TAG_ITEM_PRICE));

        if (!mShowCheckbox) {
            item.mSelectedItem.setVisibility(View.GONE);
        } else {
            if (curItem.get(Constants.NameConstants.TAG_ITEM_STATUS).equals("1"))
                item.mSelectedItem.setChecked(true);
            else
                item.mSelectedItem.setChecked(false);
        }

        return convertView;
    }

    private class ViewItem {
        ImageView mItemImage;
        TextView mItemName;
        TextView mPrice;
        CheckBox mSelectedItem;
    }
}
