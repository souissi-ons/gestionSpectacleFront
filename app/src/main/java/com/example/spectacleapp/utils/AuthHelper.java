package com.example.spectacleapp.utils;

import android.content.Context;
import android.content.SharedPreferences;

public class AuthHelper {
    private static final String PREFS_NAME = "AuthPrefs";
    private static final String KEY_AUTH_TOKEN = "auth_token";
    private static final String KEY_USER_EMAIL = "user_email";
    private static final String KEY_USER_ID = "user_id";


    private final SharedPreferences sharedPreferences;

    public AuthHelper(Context context) {
        sharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
    }


    public void saveAuthData(String token, String email, long userId) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(KEY_AUTH_TOKEN, token);
        editor.putString(KEY_USER_EMAIL, email);
        editor.putLong(KEY_USER_ID, userId);
        editor.apply();
    }


    public long getUserId() {
        return sharedPreferences.getLong(KEY_USER_ID, -1);
    }

    public String getAuthToken() {
        return sharedPreferences.getString(KEY_AUTH_TOKEN, null);
    }

    public String getUserEmail() {
        return sharedPreferences.getString(KEY_USER_EMAIL, null);
    }

    public boolean isUserLoggedIn() {
        return getAuthToken() != null;
    }

    public void clearUserData() {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.remove("user_id");
        editor.remove("is_logged_in");
        editor.remove("auth_token");
        editor.apply();
    }
}