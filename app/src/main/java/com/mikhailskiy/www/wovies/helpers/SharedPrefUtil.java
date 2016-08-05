package com.mikhailskiy.www.wovies.helpers;

/**
 * Created by Mikhail Valuyskiy on 14.10.2015.
 */

import android.content.Context;
import android.content.SharedPreferences;

import com.mikhailskiy.www.wovies.R;
import com.mikhailskiy.www.wovies.activities.MainActivity;

/**
 * Utility class for saving and getting values from SharedPreferences
 */
public class SharedPrefUtil {

    // Saves account_id in shared preferences
    public static void saveAccountIdInSharedPrefs(long accountId){
        SharedPreferences.Editor preferences = getAuthSharedPrefsEditor();
        preferences.putString(MainActivity.getContextOfApplication().getString(R.string.account_id),Long.toString(accountId));
        preferences.commit();
    }

    // Returns account_id from shared preferences
    public static String getAccountIdFromSharedPrefs(){
        SharedPreferences preferences = getAuthSharedPrefs();
        String accountId = preferences.getString(MainActivity.getContextOfApplication().getString(R.string.account_id),null);
        return accountId;
    }

    // Saves request_token in shared preferences
    public static void saveRequestTokenInSharedPrefs(String requestToken) {
        SharedPreferences.Editor preferences = getAuthSharedPrefsEditor();
        preferences.putString(MainActivity.getContextOfApplication().getString(R.string.request_token), requestToken);
        preferences.commit();
    }

    // Returns request_token from shared preferences
    public static String getRequestTokenFromSharedPrefs() {
        SharedPreferences preferences = getAuthSharedPrefs();
        String requestToken = preferences.getString(MainActivity.getContextOfApplication().getString(R.string.request_token), null);
        return requestToken;
    }

    // Saves session_id in shared preferences
    public static void saveSessionIdInSharedPrefs(String sessionId) {
        SharedPreferences.Editor preferences = getAuthSharedPrefsEditor();
        preferences.putString(MainActivity.getContextOfApplication().getString(R.string.session_id), sessionId);
        preferences.commit();
    }

    // Returns session_id from shared preferences
    public static String getSessionIdFromSharedPrefs() {
        SharedPreferences preferences = getAuthSharedPrefs();
        String sessionId = preferences.getString(MainActivity.getContextOfApplication().getString(R.string.session_id), null);
        return sessionId;
    }

    // Returns authentification sharedpreferences to
    // get authentification data like request_toke or session_id
    private static SharedPreferences getAuthSharedPrefs() {
        SharedPreferences preferences = MainActivity.getContextOfApplication().getSharedPreferences(MainActivity.getContextOfApplication().getString(R.string.auth), Context.MODE_PRIVATE);
        return preferences;
    }

    // Returns authentification sharedpreferences.editor
    // for writing data in shared preferences
    private static SharedPreferences.Editor getAuthSharedPrefsEditor() {
        SharedPreferences.Editor preferences = MainActivity.getContextOfApplication().getSharedPreferences(MainActivity.getContextOfApplication().getString(R.string.auth), Context.MODE_PRIVATE).edit();
        return preferences;
    }

    public static boolean isUserLogedIn(){
        String sessionId = getSessionIdFromSharedPrefs();
        if (sessionId == null){
            return false;
        }
        else {
            return true;
        }
    }
}
