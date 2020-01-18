package com.android.shoppingapp.Global;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.preference.PreferenceManager;

import com.android.shoppingapp.LoginActivity;

import java.util.HashMap;

public class SessionManager {
    public static final String POST_CODE_ID = "ID";
    private static final String TAG_TOKEN = "DeviceTocken";
    private static final String KEY_CURRENCY = "Currency";
    // Shared Preferences
    SharedPreferences pref;

    // Editor for Shared preferences
    Editor editor;


    // Context
    Context _context;

    // Shared pref mode
    int PRIVATE_MODE = 0;

    // Sharedpref file name
    private static final String PREF_NAME = "Karthik";

    // All Shared Preferences Keys
    private static final String IS_LOGIN = "IsLoggedIn";

    // User name (make variable public to access from outside)
    public static final String KEY_AUTH_TOKEN = "auth_token";

    //User Details

    public static final String KEY_USER_ID = "user_id";
    public static final String KEY_USER_NAME = "user_name";
    public static final String KEY_USER_EMAIL = "user_email";
    public static final String KEY_USER_IMAGE = "user_image";

    // Constructor
    public SessionManager(Context context) {
        this._context = context;
        pref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();
    }

    /**
     * Create login session
     */
    public void createLoginSession(int userId, String authToken, String userName, String userEmail,String userImage) {
        // Storing login value as TRUE
        editor.putBoolean(IS_LOGIN, true);

        // Storing name in pref
        editor.putString(KEY_USER_ID, String.valueOf(userId));
        editor.putString(KEY_AUTH_TOKEN, authToken);
        editor.putString(KEY_USER_NAME, userName);
        editor.putString(KEY_USER_EMAIL, userEmail);
        editor.putString(KEY_USER_IMAGE, userImage);
        // commit changes
        editor.commit();
    }

    public String getCurrency() {
        return PreferenceManager.getDefaultSharedPreferences(_context).getString(KEY_CURRENCY, "₦");
    }

    public void setCyrrency(String currency) {
        PreferenceManager.getDefaultSharedPreferences(_context).edit().putString(KEY_CURRENCY, currency).apply();
    }
    /**
     * Check login method wil check user login status
     * If false it will redirect user to login page
     * Else won't do anything
     */
    public void checkLogin() {
        // Check login status
        if (!this.isLoggedIn()) {
            // user is not logged in redirect him to Login Activity
            Intent i = new Intent(_context, LoginActivity.class);
            // Closing all the Activities
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            // Add new Flag to start new Activity
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            // Staring Login Activity
            _context.startActivity(i);
        }

    }

    /**
     * Get stored session data
     */
    public HashMap<String, String> getUserDetails() {
        HashMap<String, String> user = new HashMap<String, String>();

        user.put(KEY_USER_ID, pref.getString(KEY_USER_ID, null));
        user.put(KEY_AUTH_TOKEN, pref.getString(KEY_AUTH_TOKEN, null));
        user.put(KEY_USER_NAME, pref.getString(KEY_USER_NAME, null));
        user.put(KEY_USER_EMAIL, pref.getString(KEY_USER_EMAIL, null));
        user.put(KEY_USER_IMAGE, pref.getString(KEY_USER_IMAGE, ""));
        return user;
    }


    /**
     * Get stored session data
     * Content-Type=application/json, clientId=8056359277, authToken=6571731612, authId=20
     */
    public HashMap<String, String> getHeader() {
        HashMap<String, String> header = new HashMap<String, String>();
//		header.put("Content-Type", pref.getString("application/json", "application/json"));
        /*header.put("authId", pref.getString(KEY_USER_ID, KEY_USER_ID));
        header.put("authToken", pref.getString(KEY_AUTH_TOKEN, KEY_AUTH_TOKEN));*/


        header.put("authId", pref.getString(KEY_USER_ID, "1"));
        header.put("authToken", pref.getString(KEY_AUTH_TOKEN, "s4nbp5FibJpfEY9q"));

        return header;
    }

    /**
     * Clear session details
     */
    public void logoutUser() {
        // Clearing all data from Shared Preferences
        editor.clear();
        editor.commit();

        // After logout redirect user to Loing Activity
        Intent i = new Intent(_context, LoginActivity.class);
        i.putExtra(CONST.login,"Login");
        // Closing all the Activities
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        // Add new Flag to start new Activity
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        // Staring Login Activity
        _context.startActivity(i);
    }

    public void logout() {
        // Clearing all data from Shared Preferences
        editor.clear();
        editor.commit();

    }

    /**
     * Quick check for login
     **/
    // Get Login State
    public boolean isLoggedIn() {
        return pref.getBoolean(IS_LOGIN, false);
    }

    //this method will save the device token to shared preferences
    public boolean saveDeviceToken(String token) {
        SharedPreferences sharedPreferences = (_context).getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        Editor editor = sharedPreferences.edit();
        editor.putString(TAG_TOKEN, token);
        editor.apply();
        return true;
    }

    //this method will fetch the device token from shared preferences
    public String getDeviceToken() {
        SharedPreferences sharedPreferences = (_context).getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getString(TAG_TOKEN, "Testing");
    }
}
