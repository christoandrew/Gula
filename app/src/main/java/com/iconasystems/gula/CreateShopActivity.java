package com.iconasystems.gula;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.iconasystems.Constants;
import com.iconasystems.utils.CategoryAdapter;
import com.iconasystems.utils.JSONParser;
import com.iconasystems.utils.NavigationBuilder;
import com.iconasystems.utils.SessionManager;
import com.mikepenz.materialdrawer.Drawer;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class CreateShopActivity extends ActionBarActivity {
    ArrayList<HashMap<String, String>> sub_categories;
    private Toolbar toolbar;
    private SessionManager sessionManager;
    private ProgressDialog progressDialog;
    private EditText mShopname;
    private EditText mEmail;
    private EditText mDescription;
    private EditText mLocation;
    private EditText mPhone;
    private Button mCreateShop;
    private LayoutInflater inflater;
    private Spinner category;
    private Spinner subcategory;
    private JSONParser jsonParser;
    private ArrayList<HashMap<String, String>> dataList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_shop);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        dataList = new ArrayList<>();

        category = (Spinner) findViewById(R.id.category);
        // subcategory = (Spinner) findViewById(R.id.subcategory);

        category.setPrompt("Select a Category");

        category.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                String category = ((TextView) view.findViewById(R.id.cat_id)).getText().toString();
                //Toast.makeText(MainActivity.this, category, Toast.LENGTH_LONG).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        mShopname = (EditText) findViewById(R.id.add_shop_name);
        mDescription = (EditText) findViewById(R.id.add_shop_description);
        mEmail = (EditText) findViewById(R.id.add_shop_email);
        mPhone = (EditText) findViewById(R.id.add_shop_phone);
        mLocation = (EditText) findViewById(R.id.add_shop_location);
        mCreateShop = (Button) findViewById(R.id.create_shop);

        mCreateShop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new RegisterShop().execute();
            }
        });

        sessionManager = new SessionManager(CreateShopActivity.this);
        jsonParser = new JSONParser(this);
        progressDialog = new ProgressDialog(this);

        new LoadCategory().execute();

        NavigationBuilder navigationBuilder = new NavigationBuilder(this, toolbar);
        Drawer drawer = navigationBuilder.getDrawerWithHeader();

        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        drawer.getActionBarDrawerToggle().setDrawerIndicatorEnabled(true);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_create_shop, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    class RegisterShop extends AsyncTask<String, Integer, String> {
        List<NameValuePair> data = new ArrayList<>();
        String url_create_shop = Constants.UrlConstants.url_create_shop;
        String shop_name = mShopname.getText().toString();
        String description = mDescription.getText().toString();
        String location = mLocation.getText().toString();
        String email = mLocation.getText().toString();
        String phone = mPhone.getText().toString();
        private HashMap<String, String> userDetails = sessionManager.getUserDetails();
        private String user_id = userDetails.get(Constants.NameConstants.USER_ID);

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog.setIndeterminate(true);
            progressDialog.setMessage("Creating Shop...");
            progressDialog.setCancelable(false);
            progressDialog.show();

        }

        @Override
        protected String doInBackground(String... params) {
            data.add(new BasicNameValuePair("user_id", "" + sessionManager.getUserId()));
            data.add(new BasicNameValuePair("cat_id", "1"));
            data.add(new BasicNameValuePair(Constants.NameConstants.TAG_SHOP_NAME, shop_name));
            data.add(new BasicNameValuePair(Constants.NameConstants.TAG_DESCRIPTION, description));
            data.add(new BasicNameValuePair(Constants.NameConstants.TAG_LOCATION, location));
            data.add(new BasicNameValuePair(Constants.NameConstants.TAG_EMAIL, email));
            data.add(new BasicNameValuePair(Constants.NameConstants.TAG_PHONE, phone));

            JSONObject result = jsonParser.makeHttpRequest(url_create_shop, "POST", data);

            try {
                int success = result.getInt(Constants.NameConstants.TAG_SUCCESS);
                final String message = result.getString("message");

                if (success == 1) {
                    Intent openShop = new Intent(CreateShopActivity.this, ShopActivity.class);
                    startActivity(openShop);
                } else {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(CreateShopActivity.this, message, Toast.LENGTH_LONG).show();
                        }
                    });
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            progressDialog.dismiss();


        }
    }

    class LoadCategory extends AsyncTask<String, String, String> {
        List<NameValuePair> data = new ArrayList<>();
        private String cat_name;
        private String cat_id;
        private String cat_image;


        @Override
        public void onPreExecute() {
            // mProgress.setVisibility(View.VISIBLE);
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
            category.setAdapter(new CreateShopActivity.CategoryAdapter(CreateShopActivity.this, dataList));
        }
    }

    class AddShopItems extends AsyncTask<String, Integer, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog.setIndeterminate(true);
            progressDialog.setMessage("Adding Item...");
            progressDialog.setCancelable(false);
            progressDialog.show();

        }

        @Override
        protected String doInBackground(String... params) {
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            progressDialog.dismiss();
        }
    }

    public class CategoryAdapter extends BaseAdapter {
        ArrayList<HashMap<String, String>> dataList;
        Context _context;
        LayoutInflater inflater;

        public CategoryAdapter(Context context, ArrayList<HashMap<String, String>> dataList) {
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
            String cat_name = category.get(Constants.NameConstants.TAG_CAT_NAME);
            String cat_id = category.get(Constants.NameConstants.TAG_CAT_ID);

            viewHolder.mCatId.setText(cat_id);
            viewHolder.mCatName.setText(cat_name);

            return convertView;
        }

        class ViewHolder {
            public TextView mCatName;
            public TextView mCatId;

        }
    }

}
