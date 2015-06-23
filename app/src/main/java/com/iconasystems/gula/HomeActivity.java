package com.iconasystems.gula;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.iconasystems.utils.SessionManager;


public class HomeActivity extends AppCompatActivity implements View.OnClickListener {
    private SessionManager session;
    private Button mLoginBtn;
    private Button mSignupBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        session = new SessionManager(this);

        if (session.checkLogin()) {
            Intent intent = new Intent(HomeActivity.this, CategoryActivity.class);
            startActivity(intent);
            finish();
        }

        mLoginBtn = (Button) findViewById(R.id.login_button);
        mSignupBtn = (Button) findViewById(R.id.signup_button);

        mLoginBtn.setOnClickListener(this);
        mSignupBtn.setOnClickListener(this);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_home, menu);
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

    @Override
    public void onClick(View v) {
        int id = v.getId();

        switch (id) {
            case R.id.signup_button:
                Intent signupIntent = new Intent(HomeActivity.this, RegisterActivity.class);
                startActivity(signupIntent);
                break;
            case R.id.login_button:
                Intent loginIntent = new Intent(HomeActivity.this, LoginActivity.class);
                startActivity(loginIntent);
                break;
            default:
                return;
        }
    }
}
