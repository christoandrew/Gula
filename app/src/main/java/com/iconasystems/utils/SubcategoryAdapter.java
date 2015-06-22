package com.iconasystems.utils;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.iconasystems.gula.R;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Raymo on 6/21/2015.
 */
public class SubcategoryAdapter extends BaseAdapter {
    ArrayList<HashMap<String, String>> dataList;
    Context _context;
    LayoutInflater inflater;
    private String SUB_CAT_ID = "subCat_id";
    private String SUB_CAT_NAME = "sub_category_name";
    private String SUB_CAT_IMAGE = "sub_cat_image";
    private String TAG_SUB_CATEGORIES = "subcategories";

    public SubcategoryAdapter(Context context, ArrayList<HashMap<String, String>> dataList) {
        this.dataList = dataList;
        this._context = context;
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
            convertView = inflater.inflate(R.layout.spinner_item, parent, false);
        }

        ViewHolder viewHolder = new ViewHolder();
        viewHolder.mCatId = (TextView) convertView.findViewById(R.id.cat_id);
        viewHolder.mCatName = (TextView) convertView.findViewById(R.id.cat_name);

        HashMap<String, String> category;
        category = dataList.get(position);
        String sub_cat_name = category.get(SUB_CAT_NAME);
        String sub_cat_id = category.get(SUB_CAT_ID);

        viewHolder.mCatId.setText(sub_cat_id);
        viewHolder.mCatName.setText(sub_cat_name);

        return convertView;
    }

    static class ViewHolder {
        public TextView mCatName;
        public TextView mCatId;

    }
}
