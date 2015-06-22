package com.iconasystems.utils;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.iconasystems.gula.R;

import java.util.ArrayList;

/**
 * Created by Raymo on 6/8/2015.
 */
public class InterestedAdapter extends RecyclerView.Adapter<InterestedAdapter.SimpleViewHolder> {
    private final ArrayList<Integer> mBackgroundColors;
    private Context context;

    public InterestedAdapter(Context context) {
        this.context = context;
        this.mBackgroundColors = new ArrayList<Integer>();
        mBackgroundColors.add(R.drawable.img1);

        mBackgroundColors.add(R.drawable.img3);
        mBackgroundColors.add(R.drawable.img4);
        mBackgroundColors.add(R.drawable.img5);
        mBackgroundColors.add(R.drawable.img6);
    }

    @Override
    public SimpleViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final View view = LayoutInflater.from(context).inflate(R.layout.interest_strip_item, parent, false);
        return new SimpleViewHolder(view);
    }


    @Override
    public int getItemCount() {
        return this.mBackgroundColors.size();
    }

    @Override
    public void onBindViewHolder(SimpleViewHolder holder, int position) {
        holder.setData(this.mBackgroundColors, position);
    }

    public class SimpleViewHolder extends RecyclerView.ViewHolder {
        ImageView mInterestedPhoto;
        TextView mInterestedName;

        public SimpleViewHolder(View itemView) {
            super(itemView);
            mInterestedPhoto = (ImageView) itemView.findViewById(R.id.interested_photo);
            mInterestedName = (TextView) itemView.findViewById(R.id.interested_name);
        }

        public void setData(ArrayList<Integer> list, int position) {
            mInterestedPhoto.setImageResource(list.get(position));
        }
    }

}
