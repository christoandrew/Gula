package com.iconasystems.gula;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.etsy.android.grid.StaggeredGridView;
import com.iconasystems.Constants;
import com.iconasystems.utils.ItemsAdapter;
import com.iconasystems.utils.JSONParser;

import org.apache.http.NameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


/**
 * A placeholder fragment containing a simple view.
 */
public class MainFragment extends Fragment {
    StaggeredGridView gridView;
    private boolean mHasRequestedMore;
    private ItemsAdapter mAdapter;
    private Toolbar toolbar;
    private ProgressBar mProgressBar;
    private JSONParser jsonParser;
    private ArrayList<String> mData;
    private JSONArray itemsArray;
    private ArrayList<HashMap<String, String>> itemsList;


    public MainFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);
        gridView = (StaggeredGridView) rootView.findViewById(R.id.grid_view);
        mProgressBar = (ProgressBar) rootView.findViewById(R.id.progressBar);
        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        itemsList = new ArrayList<>();
        jsonParser = new JSONParser(getActivity());
        new LoadAllItems().execute();

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String item_id = ((TextView) view.findViewById(R.id.item_id)).getText().toString();
                Intent openDetails = new Intent(getActivity(), DetailsActivity.class);
                openDetails.putExtra(Constants.NameConstants.TAG_ITEM_ID, item_id);
                startActivity(openDetails);
            }
        });
    }

    class LoadAllItems extends AsyncTask<String, Integer, String> {
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
        protected String doInBackground(String... params) {
            // data.add(new BasicNameValuePair(Constants.NameConstants.))
            String url = Constants.UrlConstants.url_get_all_items;
            JSONObject result = jsonParser.makeHttpRequest(url, "GET", data);

            try {
                int success = result.getInt(Constants.NameConstants.TAG_SUCCESS);

                if (success == 1) {
                    JSONArray items = result.getJSONArray(Constants.NameConstants.TAG_ITEMS);
                    for (int i = 0; i < items.length(); i++) {
                        JSONObject item = items.getJSONObject(i);
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

                        itemsList.add(map);

                    }

                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            mProgressBar.setVisibility(View.GONE);

            gridView.setAdapter(new ItemsAdapter(getActivity(), itemsList));
        }
    }

}
