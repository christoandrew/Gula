package com.iconasystems.gula;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.iconasystems.Constants;

import java.util.ArrayList;
import java.util.HashMap;


public class AppCart extends ActionBarActivity {

    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app_cart);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //Toast.makeText(AppCart.this,"Working "+DetailsFragment.mCartdataList.toString(),Toast.LENGTH_LONG).show();
        final ArrayList<HashMap<String,String>> cartList = DetailsFragment.mCartdataList;

        // Make sure to clear the selections
        for (int i = 0; i < cartList.size(); i++) {
            cartList.get(i).put(Constants.NameConstants.TAG_ITEM_STATUS,"0");
        }

        final ListView mCatList = (ListView) findViewById(R.id.app_cart_list);
        final AdapterForAppCart cartItemAdapter = new AdapterForAppCart(getBaseContext(),cartList, getLayoutInflater(),
                true);
        mCatList.setAdapter(cartItemAdapter);

        mCatList.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {

                HashMap<String,String> selectedItem = cartList.get(position);
                if (selectedItem.get(Constants.NameConstants.TAG_ITEM_STATUS).equals("1"))
                    selectedItem.put(Constants.NameConstants.TAG_ITEM_STATUS,"0");
                else
                    selectedItem.put(Constants.NameConstants.TAG_ITEM_STATUS,"1");

                cartItemAdapter.notifyDataSetInvalidated();

            }
        });

        Button mRemoveItem = (Button) findViewById(R.id.remove_item);
        mRemoveItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Loop through and remove all the products that are selected
                // Loop backwards so that the remove works correctly
                for (int i = cartList.size() - 1; i >= 0; i--) {

                    if (cartList.get(i).get(Constants.NameConstants.TAG_ITEM_STATUS).equals("1")) {
                        //totalPrice-=Double.parseDouble(cartList.get(i).item_price);
                        cartList.remove(i);
                    }
                }
                //String newPrice = Double.toString(totalPrice);

                //mNumItems.setText("Items: "+cartList.size());

                //mTotalPrice.setText(newPrice);

                cartItemAdapter.notifyDataSetChanged();
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_app_cart, menu);
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
}
