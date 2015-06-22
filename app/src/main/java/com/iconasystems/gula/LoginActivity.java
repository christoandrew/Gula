package com.iconasystems.gula;


import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.iconasystems.Constants;
import com.iconasystems.utils.JSONParser;
import com.iconasystems.utils.SessionManager;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class LoginActivity extends ActionBarActivity {

    // JSON Node names
    private static final String TAG_SUCCESS = "success";
    private static final String TAG_MESSAGE = "message";
    private static final String TAG_USERID = "userId";
    TextView mReg;
    JSONParser jsonParser = new JSONParser(this);
    SessionManager session;
    EditText mEmail;
    EditText mPassword;
    // Progress Dialog
    private ProgressDialog pDialog;
    // url to create new product
    private String url_login_user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        session = new SessionManager(getApplicationContext());

        if (session.checkLogin()) {
            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        }

        setContentView(R.layout.activity_login);

        url_login_user = Constants.UrlConstants.url_user_login;

        mEmail = (EditText) findViewById(R.id.logEmail);
        mPassword = (EditText) findViewById(R.id.logPassword);

        Button mLogin = (Button) findViewById(R.id.login);

        // button click event
        mLogin.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                // creating new product in background thread
                new LoginUser().execute();
            }
        });

        mReg = (TextView) findViewById(R.id.reg);
        mReg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(i);
            }
        });
    }

    /**
     * Background Async Task to Create new product
     */
    class LoginUser extends AsyncTask<String, String, String> {

        /**
         * Before starting background thread Show Progress Dialog
         */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(LoginActivity.this);
            pDialog.setMessage("Logging in...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }

        /**
         * Register user
         */
        protected String doInBackground(String... args) {
            String email = mEmail.getText().toString();
            String password = mPassword.getText().toString();

            // Building Parameters
            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("email", email));
            params.add(new BasicNameValuePair("password", password));

            // getting JSON Object
            // Note that create product url accepts POST method
            JSONObject json = jsonParser.makeHttpRequest(url_login_user,
                    "POST", params);

            // check log cat for response
            Log.d("Create Response", json.toString());

            // check for success tag
            try {
                int success = json.getInt(TAG_SUCCESS);
                String message = json.getString(TAG_MESSAGE);
                int userId = json.getInt(TAG_USERID);
                String full_name = json.getString(Constants.NameConstants.FULL_NAME);
                String photo = json.getString("profile_photo");

                if (success == 1) {
                    // user registered successfully
                    session.createLoginSession(email, password, userId, full_name, photo);

                    Intent i = new Intent(getApplicationContext(), CategoryActivity.class);
                    i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

                    // Add new Flag to start new Activity
                    i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

                    finishAffinity();

                    startActivity(i);

                    // closing this screen
                    finish();
                } else if (success == 0 && message.equals("Required field(s) is missing")) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getApplicationContext(), "Field(s) required", Toast.LENGTH_LONG).show();
                        }
                    });

                } else {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getApplicationContext(), "Check your credentials", Toast.LENGTH_LONG).show();
                        }
                    });
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

            return null;
        }

        /**
         * After completing background task Dismiss the progress dialog
         * *
         */
        protected void onPostExecute(String file_url) {
            // dismiss the dialog once done
            pDialog.dismiss();
        }

    }
}
