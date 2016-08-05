package com.mikhailskiy.www.wovies.requests;

import android.net.Uri;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.mikhailskiy.www.wovies.helpers.Constants;
import com.mikhailskiy.www.wovies.helpers.SharedPrefUtil;
import com.mikhailskiy.www.wovies.helpers.States;
import com.mikhailskiy.www.wovies.managers.AppController;
import com.mikhailskiy.www.wovies.managers.RequestManager;
import com.mikhailskiy.www.wovies.models.UserAccountInfo;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONObject;

import timber.log.Timber;

/**
 * Created by Mikhail Valuyskiy on 14.10.2015.
 */
public class AccountRequest {

    private static final String BASE_URL = "http://api.themoviedb.org/3/account";
    private static final String API_KEY = "api_key";
    private static final String SESSION_ID = "session_id";

    private static RequestManager manager_;
    public AccountRequest(RequestManager manager){
        this.manager_ = manager;
    }

    public void getAccountInfo(){
        String url = createAccountUrl();
        sendAccountRequest(url);
    }

    /**
     * Creates url to get account info
     */
    private String createAccountUrl(){
        // Get session_id from SharedPreferences
        String sessionId = SharedPrefUtil.getSessionIdFromSharedPrefs();
        Uri uri = Uri.parse(BASE_URL).buildUpon()
                .appendQueryParameter(API_KEY,getApiKey())
                .appendQueryParameter(SESSION_ID,sessionId)
                .build();
        String url = uri.toString();
        Timber.d("Created URL: ", url);
        return url;
    }

    private void sendAccountRequest(String url){
        String tag = "session_id";
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(JsonObjectRequest.Method.GET,url,
                new Response.Listener<JSONObject>(){
                    @Override
                    public void onResponse(JSONObject response){
                        String responseString = response.toString();
                        UserAccountInfo account = getAccountFromJson(responseString);
                        manager_.sendMessage(manager_.obtainMessage(States.ACCOUNT_REQUEST_COMPLETED,account));
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        String errorMsg = error.getMessage();
                        Timber.e(error.getMessage());
                        manager_.sendMessage(manager_.obtainMessage(States.VOLLEY_REQUEST_FAILED, errorMsg));
                    }
                });
        AppController.getInstance().addToRequestQueue(jsonObjectRequest, tag);
    }

    private UserAccountInfo getAccountFromJson(String json){
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.setDateFormat("M/d/yy hh:mm a");
        Gson gson = gsonBuilder.create();
        UserAccountInfo userAccountInfo = new UserAccountInfo();
        userAccountInfo = gson.fromJson(json,UserAccountInfo.class);
        return userAccountInfo;
    }

    // Temporary method for getting api_key_value
    private static String getApiKey() {
        return Constants.API_KEY_VALUE;
    }

}
