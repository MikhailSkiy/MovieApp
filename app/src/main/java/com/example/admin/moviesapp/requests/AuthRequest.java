package com.example.admin.moviesapp.requests;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.admin.moviesapp.R;
import com.example.admin.moviesapp.activities.MainActivity;
import com.example.admin.moviesapp.helpers.Constants;
import com.example.admin.moviesapp.helpers.SharedPrefUtil;
import com.example.admin.moviesapp.helpers.States;
import com.example.admin.moviesapp.managers.AppController;
import com.example.admin.moviesapp.managers.RequestManager;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import org.json.JSONObject;

import timber.log.Timber;

/**
 * Created by Mikhail Valuyskiy on 08.10.2015.
 */
public class AuthRequest {

    private static final String BASE_URL = "http://api.themoviedb.org/3/authentication/token/new";
    private static final String REDIRECTION_URL = "https://www.themoviedb.org/authenticate/";
    private static final String SESSION_ID_BASE_URL = "http://api.themoviedb.org/3/authentication/session/new";
    private static final String REQUEST_TOKEN_KEY = "request_token";
    private static final String API_KEY = "api_key";

    private static RequestManager manager_;

    public AuthRequest(RequestManager manager){
        this.manager_ = manager;
    }

    /**
     * Creates url to get request token
     * And sends request to get token request
     */
    public void getRequestToken(){
        String url = createTokenUrl();
        requestToken(url);
    }

    /**
     * Creates url for redirection user to the authentification page
     */
    public static String getRedirectionUrl(String token) {
        String redirectionUrl = createRedirectionUrl(token);
        return redirectionUrl;
    }

    public static void sendSessionIdRequest() {
        // Get token from shared preferences
        String requestToken = SharedPrefUtil.getRequestTokenFromSharedPrefs();
        // Create Url
        String url = createSessionRequestUrl(requestToken);
        // Send request
        getSessionId(url);
    }

    private static String createSessionRequestUrl(String token){
        Uri uri = Uri.parse(SESSION_ID_BASE_URL).buildUpon()
                .appendQueryParameter(API_KEY,getApiKey())
                .appendQueryParameter(REQUEST_TOKEN_KEY, token)
                .build();
        String sessionRequestUrl = uri.toString();
        Timber.v("Created session_id URL",sessionRequestUrl);
        return sessionRequestUrl;
    }

    /**
     * Creates url to get request token
     * @return url
     */
    private String createTokenUrl(){
        Uri uri = Uri.parse(BASE_URL).buildUpon()
                .appendQueryParameter(API_KEY,getApiKey())
                .build();
        String url = uri.toString();
        Timber.v("Created token URL: ", url);
        return url;
    }

    private static String createRedirectionUrl(String token){
        Uri uri = Uri.parse(REDIRECTION_URL).buildUpon()
                .appendEncodedPath(token)
                .appendQueryParameter(API_KEY,getApiKey())
                .build();
        String url = uri.toString();
        Timber.v("Created redirect URL: ",url);
        return url;
    }

    /**
     * Sends request to get request token for authentication
     * @param url
     */
    private void requestToken(final String url){
        String tag = "request_token";
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(JsonObjectRequest.Method.GET,url,
                new Response.Listener<JSONObject>(){
                    @Override
                public void onResponse(JSONObject response){
                        String responseString = response.toString();
                        String token = getTokenFromJson(responseString);
                        manager_.sendMessage(manager_.obtainMessage(States.TOKEN_REQUEST_RECEIVED,token));
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

    /**
     * Parse json result and get request_token from it
     * @param json
     * @return request_token
     */
    private String getTokenFromJson(String json){
        JsonElement jsonElement = new JsonParser().parse(json);
        JsonObject jsonObject = jsonElement.getAsJsonObject();
        String token = jsonObject.get("request_token").toString().replace("\"","");
        return token;
    }

    /**
     * Sends request to get session_id
     * @return String session_id
     */
    private static void getSessionId(final String url){
        String tag = "session_id_request";
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(JsonObjectRequest.Method.GET,url,
                new Response.Listener<JSONObject>(){
                    @Override
                    public void onResponse(JSONObject response){
                        String responseString = response.toString();
                        String session_id = getSessionIdFromJson(responseString);
                        manager_.sendMessage(manager_.obtainMessage(States.SESSION_ID_RECEIVED,session_id));
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

    /**
     * Parse json result and get session_id from it
     * @param json
     * @return session_id
     */
    private static String getSessionIdFromJson(String json){
        JsonElement jsonElement = new JsonParser().parse(json);
        JsonObject jsonObject = jsonElement.getAsJsonObject();
        String sessionId = jsonObject.get("session_id").toString().replace("\"","");
        return sessionId;
    }

    // Temporary method for getting api_key_value
    private static String getApiKey() {
        return Constants.API_KEY_VALUE;
    }

}
