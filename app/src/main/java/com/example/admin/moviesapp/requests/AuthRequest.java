package com.example.admin.moviesapp.requests;

import android.net.Uri;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.admin.moviesapp.helpers.Constants;
import com.example.admin.moviesapp.helpers.States;
import com.example.admin.moviesapp.managers.AppController;
import com.example.admin.moviesapp.managers.RequestManager;

import org.json.JSONObject;

import timber.log.Timber;

/**
 * Created by Mikhail Valuyskiy on 08.10.2015.
 */
public class AuthRequest {

    private final String BASE_URL = "http://api.themoviedb.org/3/authentication/token/new";
    private final String API_KEY = "api_key";

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
        Timber.v("Created token URL: ", url);
        requestToken(url);
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
        return url;
    }

    /**
     * Sends request to get request toekn for authentication
     * @param url
     */
    private void requestToken(final String url){
        String tag = "request_token";
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(JsonObjectRequest.Method.GET,url,
                new Response.Listener<JSONObject>(){
                    @Override
                public void onResponse(JSONObject response){
                        String responseString = response.toString();
                        manager_.sendMessage(manager_.obtainMessage(States.TOKEN_REQUEST_RECEIVED,responseString));
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

    // Temporary method for getting api_key_value
    private String getApiKey() {
        return Constants.API_KEY_VALUE;
    }

}
