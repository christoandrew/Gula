package com.iconasystems.gula;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.TextView;

import com.iconasystems.Constants;
import com.iconasystems.utils.JSONParser;
import com.iconasystems.utils.NavigationBuilder;
import com.iconasystems.utils.SubcategoryAdapter;
import com.mikepenz.materialdrawer.Drawer;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class SubcategoryActivity extends BaseActivity {
    private Toolbar toolbar;
    private String cat_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subcategory);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        NavigationBuilder navigationBuilder = new NavigationBuilder(this, toolbar);
        Drawer drawer = navigationBuilder.getDrawerWithHeader();

        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        drawer.getActionBarDrawerToggle().setDrawerIndicatorEnabled(true);

        Bundle bundle = getIntent().getExtras();
        cat_id = bundle.getString(Constants.NameConstants.TAG_CAT_ID);
        new LoadSubcategories().execute();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_subcategory, menu);
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

    class LoadSubcategories extends AsyncTask<Integer, Void, View> {
        private final static String url_get_sub_categories = "http://baala-online.netii.net/gula/get_sub_categories.php";
        // Creating JSON Parser object
        JSONParser jParser = new JSONParser(SubcategoryActivity.this);
        JSONArray subCategoryArray = null;
        String sub_cat_id;
        String sub_cat_name;
        String sub_cat_image;
        ArrayList<HashMap<String, String>> sub_categories = new ArrayList<HashMap<String, String>>();
        // Progress Dialog
        private ProgressDialog pDialog;
        private String SUB_CAT_ID = "subCat_id";
        private String SUB_CAT_NAME = "sub_category_name";
        private String SUB_CAT_IMAGE = "sub_cat_image";
        private String TAG_SUB_CATEGORIES = "subcategories";
        private String TAG_SUCCESS = "success";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(SubcategoryActivity.this);
            pDialog.setMessage("Loading ...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }


        @Override
        protected View doInBackground(Integer... args) {
            // Building Parameters
            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("cat_id", cat_id));
            // getting JSON string from URL
            JSONObject json = jParser.makeHttpRequest(url_get_sub_categories,
                    "GET", params);
            // Check your log cat for JSON reponse
            Log.d("sub categories: ", json.toString());

            try {
                // Checking for SUCCESS TAG
                int success = json.getInt(TAG_SUCCESS);

                if (success == 1) {
                    subCategoryArray = json.getJSONArray(TAG_SUB_CATEGORIES);

                    for (int i = 0; i < subCategoryArray.length(); i++) {
                        JSONObject info = subCategoryArray.getJSONObject(i);

                        // Storing each json item in variable
                        sub_cat_id = info.getString(SUB_CAT_ID);
                        sub_cat_name = info.getString(SUB_CAT_NAME);
                        sub_cat_image = info.getString(SUB_CAT_IMAGE);

                        // creating new HashMap
                        HashMap<String, String> map = new HashMap<String, String>();

                        // adding each child node to HashMap key => value
                        map.put(Constants.NameConstants.TAG_SUB_CAT_ID, sub_cat_id);
                        map.put(SUB_CAT_NAME, sub_cat_name);
                        map.put(SUB_CAT_IMAGE, sub_cat_image);

                        // adding HashList to ArrayList
                        sub_categories.add(map);


                    }
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }


            return (GridView) findViewById(R.id.gridview);

        }

        // Once complete, see if ImageView is still around and set bitmap.
        @Override
        protected void onPostExecute(View v) {
            pDialog.dismiss();
            ((GridView) v).setAdapter(new SubcategoryAdapter(SubcategoryActivity.this, sub_categories));

            ((GridView) v).setOnItemClickListener(new AdapterView.OnItemClickListener() {
                public void onItemClick(AdapterView<?> parent, View v,
                                        final int position, long id) {
                    String sub_cat_id = ((TextView) v.findViewById(R.id.sub_cat_id)).getText().toString();
                    Intent i = new Intent(SubcategoryActivity.this, ItemActivity.class);
                    i.putExtra(Constants.NameConstants.TAG_SUB_CAT_ID, sub_cat_id);
                    startActivity(i);

                }
            });
        }
    }

}
