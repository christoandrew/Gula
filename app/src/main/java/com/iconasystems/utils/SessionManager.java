package com.iconasystems.utils;

import java.util.HashMap;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import com.iconasystems.gula.HomeActivity;

public class SessionManager {
    // Email address (make variable public to access from outside)
    public static final String KEY_EMAIL = "email";
    // Email address (make variable public to access from outside)
    public static final String KEY_PASSWORD = "password";
    // user id address (make variable public to access from outside)
    public static final String USER_ID = "userId";
    // User Full name
    public static final String FULL_NAME = "fullname";
    //User profile picture
    public static final String PROFILE_PHOTO = "profile_photo";
    // Shared pref file name
    private static final String PREF_NAME = "gulaAppPref";

    // User name (make variable public to access from outside)
    // public static final String KEY_NAME = "username";
    // All Shared Preferences Keys
    private static final String IS_LOGIN = "IsLoggedIn";
    // Shared Preferences
    SharedPreferences pref;
    // Editor for Shared preferences
    Editor editor;
    // Context
    Context _context;
    // Shared pref mode
    int PRIVATE_MODE = 0;

    // Constructor
    public SessionManager(Context context) {
        this._context = context;
        pref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();
    }

    /**
     * Create login session
     */
    public void createLoginSession(String email, String password, int userId, String fullname, String profile_photo) {
        // Storing login value as TRUE
        editor.putBoolean(IS_LOGIN, true);

        // Storing name in pref
        editor.putString(KEY_PASSWORD, password);

        // Storing email in pref
        editor.putString(KEY_EMAIL, email);

        //Storing user id in pref
        editor.putInt(USER_ID, userId);

        editor.putString(FULL_NAME, fullname);
        editor.putString(PROFILE_PHOTO, profile_photo);
        // commit changes
        editor.commit();
    }

    /**
     * Check login method wil check user login status
     * If false it will redirect user to login page
     * Else won't do anything
     */
    public boolean checkLogin() {
        // Check login status
        if (!this.isLoggedIn()) {
            return false;
            /*// user is not logged in redirect him to LoginActivity Activity
            Intent i = new Intent(_context, LoginActivity.class);
            // Closing all the Activities
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

            // Add new Flag to start new Activity
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

            // Staring LoginActivity Activity
            _context.startActivity(i);*/
        }
        return true;

    }


    /**
     * Get stored session data
     */
    public HashMap<String, String> getUserDetails() {
        HashMap<String, String> user = new HashMap<String, String>();

        // user email
        user.put(KEY_EMAIL, pref.getString(KEY_EMAIL, null));

        // user password
        user.put(KEY_PASSWORD, pref.getString(KEY_PASSWORD, null));

        // return user
        return user;
    }
    /*
     * get user id
     */

    public int getUserId() {
        return pref.getInt(USER_ID, 0);
    }

    /**
     * Clear session details
     */
    public void logoutUser() {
        // Clearing all data from Shared Preferences
        editor.clear();
        editor.commit();

        // After logout redirect user to LoginActivity Activity
        Intent i = new Intent(_context, HomeActivity.class);
        // Closing all the Activities
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        // Add new Flag to start new Activity
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        // Staring LoginActivity Activity
        _context.startActivity(i);
    }

    /**
     * Quick check for login
     * *
     */
    // Get LoginActivity State
    public boolean isLoggedIn() {
        return pref.getBoolean(IS_LOGIN, false);
    }
}
