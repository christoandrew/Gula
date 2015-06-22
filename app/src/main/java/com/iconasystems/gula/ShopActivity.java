package com.iconasystems.gula;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.iconasystems.Constants;
import com.iconasystems.utils.NavigationBuilder;
import com.mikepenz.materialdrawer.Drawer;


public class ShopActivity extends AppCompatActivity {
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        String shop_name = getIntent().getStringExtra(Constants.NameConstants.TAG_SHOP_NAME);
        toolbar.setTitle(shop_name);

        NavigationBuilder navigationBuilder = new NavigationBuilder(this, toolbar);
        Drawer drawer = navigationBuilder.getDrawerWithHeader();

        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        drawer.getActionBarDrawerToggle().setDrawerIndicatorEnabled(true);

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_shop, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        switch (id) {

        }
        return super.onOptionsItemSelected(item);
    }
}
