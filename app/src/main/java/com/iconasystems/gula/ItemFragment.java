package com.iconasystems.gula;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.etsy.android.grid.StaggeredGridView;
import com.iconasystems.Constants;
import com.iconasystems.common.logger.Log;
import com.iconasystems.utils.ItemsAdapter;
import com.iconasystems.utils.JSONParser;


import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ItemFragment extends Fragment {
    private StaggeredGridView gridView;
    private ProgressBar mProgressBar;
    //private ItemsAdapter mAdapter;
    public static ArrayList<HashMap<String, String>> mItemsdataList = new ArrayList<>();
    private JSONParser jsonParser;
    private String sub_cat_id;
    private boolean mHasRequestedMore;


    private ArrayList<String> mData;

    public ItemFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_item, container, false);
        gridView = (StaggeredGridView) rootView.findViewById(R.id.grid_view);
        mProgressBar = (ProgressBar) rootView.findViewById(R.id.item_progress);
        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Bundle bundle = getActivity().getIntent().getExtras();
        //sub_cat_id = bundle.getString(Constants.NameConstants.TAG_SUB_CAT_ID);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                String item_id = ((TextView) view.findViewById(R.id.item_id)).getText().toString();
                Intent openDetails = new Intent(getActivity(), DetailsActivity.class);
                openDetails.putExtra(Constants.NameConstants.TAG_ITEM_ID, item_id);
                openDetails.putExtra(Constants.NameConstants.INDEX,position);
                startActivity(openDetails);

            }
        });
        jsonParser = new JSONParser(getActivity());
        new LoadItems().execute();


    }

    class LoadItems extends AsyncTask<String, Integer, ArrayList<HashMap<String, String>>> {
        List<NameValuePair> data = new ArrayList<>();

        String item_name;
        String description;
        String image;
        String price;
        String item_id;
        String shop_id;


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mProgressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected ArrayList<HashMap<String, String>> doInBackground(String... params) {
            data.add(new BasicNameValuePair(Constants.NameConstants.TAG_SUB_CAT_ID, "1"));
            String url = Constants.UrlConstants.url_get_by_sub_category;
            JSONObject result = jsonParser.makeHttpRequest(url, "GET", data);
            Log.i("SubcatId", sub_cat_id);
            try {
                int success = result.getInt(Constants.NameConstants.TAG_SUCCESS);

                if (success == 1) {
                    JSONArray items = result.getJSONArray(Constants.NameConstants.TAG_ITEMS);

                    mItemsdataList.clear();

                    for (int i = 0; i < items.length(); i++) {
                        final JSONObject item = items.getJSONObject(i);
                        /*getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(getActivity(), item.toString(), Toast.LENGTH_LONG).show();
                            }
                        });*/
                        item_name = item.getString(Constants.NameConstants.TAG_ITEM_NAME);
                        description = item.getString(Constants.NameConstants.TAG_DESCRIPTION);
                        image = item.getString(Constants.NameConstants.TAG_ITEM_IMAGE);
                        item_id = item.getString(Constants.NameConstants.TAG_ITEM_ID);
                        shop_id = item.getString(Constants.NameConstants.TAG_SHOP);
                        price = item.getString(Constants.NameConstants.TAG_PRICE);

                        HashMap<String, String> map = new HashMap<>();

                        map.put(Constants.NameConstants.TAG_ITEM_ID, item_id);
                        map.put(Constants.NameConstants.TAG_DESCRIPTION, description);
                        map.put(Constants.NameConstants.TAG_ITEM_NAME, item_name);
                        map.put(Constants.NameConstants.TAG_ITEM_IMAGE, image);

                        map.put(Constants.NameConstants.TAG_ITEM_PRICE, price);
                        map.put(Constants.NameConstants.TAG_ITEM_STATUS,"0");

                        mItemsdataList.add(map);
                    }

                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return mItemsdataList;
        }

        @Override
        protected void onPostExecute(ArrayList<HashMap<String, String>> result) {
            super.onPostExecute(result);
            mProgressBar.setVisibility(View.GONE);

            gridView.setAdapter(new ItemsAdapter(getActivity(), result));
        }
    }
}
