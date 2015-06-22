package com.iconasystems.gula;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.iconasystems.Constants;
import com.iconasystems.common.logger.Log;
import com.iconasystems.customchoicelist.Cheeses;
import com.iconasystems.utils.CategoryAdapter;
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
public class CategoryFragment extends Fragment {
    ListView listView;
    private LayoutInflater layoutInflater;
    private String item;
    private Button getItems;
    private JSONParser jsonParser;
    private ProgressBar mProgress;
    private ArrayList<HashMap<String, String>> dataList;
    private String sub_cat_id;

    public CategoryFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView;
        rootView = inflater.inflate(R.layout.fragment_category, container, false);
        listView = (ListView) rootView.findViewById(R.id.categories);
        mProgress = (ProgressBar) rootView.findViewById(R.id.cat_progress);
        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        jsonParser = new JSONParser(getActivity());
        dataList = new ArrayList<>();

        new LoadCategory().execute();

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String cat_id = ((TextView) view.findViewById(R.id.cat_id)).getText().toString();
                Intent openSubcat = new Intent(getActivity(), SubcategoryActivity.class);
                openSubcat.putExtra(Constants.NameConstants.TAG_CAT_ID, cat_id);
                startActivity(openSubcat);
            }
        });

    }

    class LoadCategory extends AsyncTask<String, String, String> {
        List<NameValuePair> data = new ArrayList<>();
        private String cat_name;
        private String cat_id;
        private String cat_image;


        @Override
        public void onPreExecute() {
            mProgress.setVisibility(View.VISIBLE);
        }

        @Override
        protected String doInBackground(String... params) {

            JSONObject result = jsonParser.makeHttpRequest(Constants.UrlConstants.url_get_categories, "GET", data);
            try {
                int success = result.getInt(Constants.NameConstants.TAG_SUCCESS);
                if (success == 1) {
                    JSONArray categories = result.getJSONArray(Constants.NameConstants.TAG_CATEGORIES);
                    for (int i = 0; i < categories.length(); i++) {
                        JSONObject categoryObj = categories.getJSONObject(i);
                        cat_name = categoryObj.getString(Constants.NameConstants.TAG_CAT_NAME);
                        cat_id = categoryObj.getString(Constants.NameConstants.TAG_CAT_ID);
                        cat_image = categoryObj.getString(Constants.NameConstants.TAG_CAT_IMAGE);

                        HashMap<String, String> catHash;

                        catHash = new HashMap<>();
                        catHash.put(Constants.NameConstants.TAG_CAT_ID, cat_id);
                        catHash.put(Constants.NameConstants.TAG_CAT_IMAGE, cat_image);
                        catHash.put(Constants.NameConstants.TAG_CAT_NAME, cat_name);

                        dataList.add(catHash);
                    }

                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        public void onPostExecute(String result) {
            super.onPostExecute(result);
            mProgress.setVisibility(View.GONE);
            listView.setAdapter(new CategoryAdapter(getActivity(), dataList));
        }
    }

}
