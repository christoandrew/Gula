package com.iconasystems.gula;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.iconasystems.Constants;
import com.iconasystems.utils.JSONParser;
import com.iconasystems.utils.SessionManager;
import com.mikepenz.materialdrawer.Drawer;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class RegisterActivity extends ActionBarActivity {
    // JSON Node names
    private static final String TAG_SUCCESS = "success";
    private static final String TAG_USERID = "userId";
    // url to create new product
    private static String url_register_user;
    JSONParser jsonParser = new JSONParser(this);
    SessionManager session;
    EditText mFullName;
    EditText mUsername;
    EditText mEmail;
    EditText mPassword;
    private Toolbar toolbar;
    private Drawer result;
    // Progress Dialog
    private ProgressDialog pDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        session = new SessionManager(getApplicationContext());
        if (session.checkLogin()) {
            Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        }
        setContentView(R.layout.activity_register);


        // Edit Text
        mFullName = (EditText) findViewById(R.id.full_name);
        mUsername = (EditText) findViewById(R.id.username);
        mEmail = (EditText) findViewById(R.id.email);
        mPassword = (EditText) findViewById(R.id.password);

        // Create button
        Button mSignup = (Button) findViewById(R.id.signup);

        // button click event
        mSignup.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                // creating new product in background thread
                new RegisterUser().execute();
            }
        });
    }

    /**
     * Background Async Task to Create new product
     */
    class RegisterUser extends AsyncTask<String, String, String> {
        String url_register_user = Constants.UrlConstants.url_register;

        /**
         * Before starting background thread Show Progress Dialog
         */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
           /* pDialog = new ProgressDialog(RegisterActivity.this);
            pDialog.setMessage("Signing up..");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();*/
        }

        /**
         * Register user
         */
        protected String doInBackground(String... args) {
            String fullName = mFullName.getText().toString();
            String username = mUsername.getText().toString();
            String email = mEmail.getText().toString();
            String password = mPassword.getText().toString();

            // Building Parameters
            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("username", username));
            params.add(new BasicNameValuePair("email", email));
            params.add(new BasicNameValuePair("password", password));
            params.add(new BasicNameValuePair("full_name", fullName));

            // getting JSON Object
            // Note that create product url accepts POST method
            JSONObject json = jsonParser.makeHttpRequest(url_register_user,
                    "POST", params);

            // check log cat for response
            Log.d("Create Response", json.toString());

            // check for success tag
            try {
                int success = json.getInt(TAG_SUCCESS);
                int userId = json.getInt(TAG_USERID);
                String photo = json.getString(Constants.NameConstants.PROFILE_PHOTO);

                if (success == 1) {
                    // user registered successfully
                    session.createLoginSession(email, password, userId, fullName, photo);

                    Intent i = new Intent(getApplicationContext(), ItemActivity.class);
                    i.putExtra(Constants.NameConstants.TAG_USER_ID, userId);

                    i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

                    // Add new Flag to start new Activity
                    i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

                    finishAffinity();

                    startActivity(i);

                    // closing this screen
                    finish();
                } else {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getApplicationContext(), "Field(s) required", Toast.LENGTH_LONG).show();
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
            // pDialog.dismiss();
        }

    }
}
