package com.iconasystems.gula;

import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.iconasystems.Constants;
import com.iconasystems.utils.InterestedAdapter;
import com.iconasystems.utils.JSONParser;
import com.nostra13.universalimageloader.core.ImageLoader;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.lucasr.twowayview.widget.TwoWayView;
import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


/**
 * A placeholder fragment containing a simple view.
 */
public class DetailsFragment extends Fragment {
    String item_id;
    private TwoWayView mInterested;
    private TextView mProductName;
    private TextView mPrice;
    private TextView mDescription;
    private TextView mShopName;
    private ImageView mPhoto;
    private Button mPurchaseButton;
    private JSONParser jsonParser;
    public static ArrayList<HashMap<String, String>> mCartdataList = new ArrayList<>();

    public DetailsFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_details, container, false);
        mInterested = (TwoWayView) rootView.findViewById(R.id.interested_strip);
        mProductName = (TextView) rootView.findViewById(R.id.product_title);
        mPrice = (TextView) rootView.findViewById(R.id.price);
        mDescription = (TextView) rootView.findViewById(R.id.product_description);
        mShopName = (TextView) rootView.findViewById(R.id.shop_name);
        mPhoto = (ImageView) rootView.findViewById(R.id.item_photo);
        mPurchaseButton = (Button) rootView.findViewById(R.id.purchase_button);

        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        jsonParser = new JSONParser(getActivity());
        mInterested.setAdapter(new InterestedAdapter(getActivity()));

        Bundle bundle = getActivity().getIntent().getExtras();
        //item_id = bundle.getString(Constants.NameConstants.TAG_ITEM_ID);
        final int position =  bundle.getInt(Constants.NameConstants.INDEX);
        Log.d("ITEM POSITION","item position:"+position);


        item_id = ItemFragment.mItemsdataList.get(position).get(Constants.NameConstants.TAG_ITEM_ID);

       mPurchaseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                mCartdataList.add(ItemFragment.mItemsdataList.get(position));
                Intent cartActivity = new Intent(getActivity(),AppCart.class);
                startActivity(cartActivity);
                getActivity().finish();
            }
        });



        // Disable the add to cart button if the item is already in the cart
        if (mCartdataList.contains(ItemFragment.mItemsdataList.get(position))) {
            mPurchaseButton.setEnabled(false);
            mPurchaseButton.setText("Item in Cart");
            mPurchaseButton.setTextColor(Color.parseColor("#FFFFFF"));
        }


        new LoadDetails().execute();




    }

    class LoadDetails extends AsyncTask<String, String, String> {
        List<NameValuePair> data = new ArrayList<>();
        String product_name;
        String price;
        String description;
        String shop_name;
        String photo;
        String date_added;
        String url_get_details = Constants.UrlConstants.url_get_details;
        String image_url;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... params) {
            data.add(new BasicNameValuePair(Constants.NameConstants.TAG_ITEM_ID, item_id));
            JSONObject result = jsonParser.makeHttpRequest(url_get_details, "GET", data);
            try {
                int success = result.getInt(Constants.NameConstants.TAG_SUCCESS);
                if (success == 1) {
                    JSONArray details = result.getJSONArray(Constants.NameConstants.TAG_DETAILS);
                    JSONObject detailObj = details.getJSONObject(0);

                    price = detailObj.getString(Constants.NameConstants.TAG_ITEM_PRICE);
                    product_name = detailObj.getString(Constants.NameConstants.TAG_ITEM_NAME);
                    description = detailObj.getString(Constants.NameConstants.TAG_DESCRIPTION);
                    shop_name = detailObj.getString(Constants.NameConstants.TAG_SHOP);
                    photo = detailObj.getString(Constants.NameConstants.TAG_ITEM_IMAGE);
                    date_added = detailObj.getString(Constants.NameConstants.TAG_DATE_ADDED);

                    image_url = Constants.UrlConstants.url_items_dir + "/" + photo;


                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            mPrice.setText("UShs : " + price);
            mProductName.setText(product_name);
            mShopName.setText(shop_name);
            mDescription.setText(description);

            ImageLoader.getInstance().displayImage(image_url, mPhoto);
        }
    }
}
