package com.iconasystems.gula;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.iconasystems.Constants;
import com.iconasystems.common.logger.Log;
import com.iconasystems.utils.ItemsAdapter;
import com.iconasystems.utils.JSONParser;
import com.iconasystems.utils.NavigationBuilder;
import com.iconasystems.utils.ShopAdapter;
import com.iconasystems.utils.ShopsAdapter;
import com.mikepenz.materialdrawer.Drawer;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class MyShopsActivity extends BaseActivity {
    private Toolbar toolbar;
    private ProgressBar mProgressBar;
    private ListView mShops;
    private JSONParser jsonParser;
    private ArrayList<HashMap<String, String>> mDataList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_shops);

        mProgressBar = (ProgressBar) findViewById(R.id.progressBar);
        mShops = (ListView) findViewById(R.id.shop_list);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mShops.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String shop_id = ((TextView) view.findViewById(R.id.shop_item_id)).getText().toString();
                String shop_name = ((TextView) view.findViewById(R.id.shop_name)).getText().toString();
                Intent openShop = new Intent(MyShopsActivity.this, ShopActivity.class);
                openShop.putExtra(Constants.NameConstants.TAG_SHOP_ID, shop_id);
                openShop.putExtra(Constants.NameConstants.TAG_SHOP_NAME, shop_name);
                startActivity(openShop);
            }
        });


        jsonParser = new JSONParser(this);
        mDataList = new ArrayList<>();

        NavigationBuilder navigationBuilder = new NavigationBuilder(this, toolbar);
        Drawer drawer = navigationBuilder.getDrawerWithHeader();

        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        drawer.getActionBarDrawerToggle().setDrawerIndicatorEnabled(true);

        new LoadShops().execute();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds shops to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_my_shops, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem shop) {
        // Handle action bar shop clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = shop.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        if (id == R.id.action_add_shop) {
            Intent createShop = new Intent(MyShopsActivity.this, CreateShopActivity.class);
            startActivity(createShop);
        }

        return super.onOptionsItemSelected(shop);
    }

    class LoadShops extends AsyncTask<String, Integer, ArrayList<HashMap<String, String>>> {
        List<NameValuePair> data = new ArrayList<>();

        String shop_name;
        String description;
        String image;
        String category;
        String location;
        String shop_id;


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mProgressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected ArrayList<HashMap<String, String>> doInBackground(String... params) {
            data.add(new BasicNameValuePair(Constants.NameConstants.TAG_USER_ID, "1"));
            String url = Constants.UrlConstants.url_get_my_shops;
            JSONObject result = jsonParser.makeHttpRequest(url, "GET", data);

            try {
                int success = result.getInt(Constants.NameConstants.TAG_SUCCESS);

                if (success == 1) {
                    JSONArray shops = result.getJSONArray(Constants.NameConstants.TAG_SHOPS);

                    for (int i = 0; i < shops.length(); i++) {
                        JSONObject shop = shops.getJSONObject(i);

                        shop_name = shop.getString(Constants.NameConstants.TAG_SHOP_NAME);
                        description = shop.getString(Constants.NameConstants.TAG_DESCRIPTION);
                        image = shop.getString(Constants.NameConstants.TAG_SHOP_IMAGE);
                        location = shop.getString(Constants.NameConstants.TAG_LOCATION);
                        category = shop.getString(Constants.NameConstants.TAG_CATEGORY);
                        shop_id = shop.getString(Constants.NameConstants.TAG_SHOP);

                        HashMap<String, String> map = new HashMap<>();

                        map.put(Constants.NameConstants.TAG_DESCRIPTION, description);
                        map.put(Constants.NameConstants.TAG_SHOP_NAME, shop_name);
                        map.put(Constants.NameConstants.TAG_SHOP_IMAGE, image);
                        map.put(Constants.NameConstants.TAG_LOCATION, location);
                        map.put(Constants.NameConstants.TAG_CATEGORY, category);
                        map.put(Constants.NameConstants.TAG_SHOP_ID, shop_id);

                        mDataList.add(map);
                    }

                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return mDataList;
        }

        @Override
        protected void onPostExecute(ArrayList<HashMap<String, String>> result) {
            super.onPostExecute(result);
            mProgressBar.setVisibility(View.GONE);

            mShops.setAdapter(new ShopsAdapter(MyShopsActivity.this, result));
        }
    }
}
