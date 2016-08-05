package com.mikhailskiy.www.wovies.requests;

import android.net.Uri;

import com.android.volley.Cache;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.mikhailskiy.www.wovies.helpers.Constants;
import com.mikhailskiy.www.wovies.helpers.States;
import com.mikhailskiy.www.wovies.managers.AppController;
import com.mikhailskiy.www.wovies.managers.RequestManager;
import com.mikhailskiy.www.wovies.models.CastDetails;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONObject;

import java.io.UnsupportedEncodingException;

import timber.log.Timber;

/**
 * Created by Mikhail Valuyskiy on 08.09.2015.
 */
public class CastDetailsRequest  {

    //region Keys for building query
    private final String BASE_CAST_DETAILS_REQUEST_URL = "http://api.themoviedb.org/3/person/";
    private final String API_KEY = "api_key";
    //endregion

    private static RequestManager manager_;

    public CastDetailsRequest(RequestManager manager){
        this.manager_ = manager;
    }

    public void postRequest(String castId) {
        String url = createCastDetailsUrl(castId);
        postGetRequest(url);
    }

    private String createCastDetailsUrl(String castId) {
        String url = null;
        Uri builtUri = Uri.parse(BASE_CAST_DETAILS_REQUEST_URL).buildUpon()
                .appendPath(castId)
                .appendQueryParameter(API_KEY, getApiKey())
                .build();
        url = builtUri.toString();
        Timber.v(url);
        return url;
    }

    private void getCastDetailsObject(String response){
        CastDetails castDetails = getCastDetailsFromJson(response);
        // Send event through eventBus
    }

    private CastDetails getCastDetailsFromJson(String response){
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.setDateFormat("M/d/yy hh:mm a");
        Gson gson = gsonBuilder.create();
        CastDetails castDetails = new CastDetails();

        castDetails = gson.fromJson(response,CastDetails.class);
        Timber.v(castDetails.getName());
        return castDetails;
    }



    // Temporary method for getting api_key_value
    private String getApiKey() {
        return Constants.API_KEY_VALUE;
    }

    // use Volley for sending request
    private void postGetRequest(final String url) {
        String tag = "movie_details_request";
        // Check if there is saved data in the cache
        // If so, there is no need to send same request, just take it from cache
        // Otherwise, send request
        String response = getDataFromCache(url);
        if (response != null) {
            Timber.v("Data was cashed");
            CastDetails cashedCastDetails = getCastDetailsFromJson(response);
            manager_.sendMessage(manager_.obtainMessage(States.CAST_DETAILS_REQUEST_COMPLETED, cashedCastDetails));
        } else {
            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            Timber.v("Data was not cashed");
                            String responseString = response.toString();
                            CastDetails returnedCastDetails = getCastDetailsFromJson(responseString);
                            manager_.sendMessage(manager_.obtainMessage(States.CAST_DETAILS_REQUEST_COMPLETED, returnedCastDetails));
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
    }

    private String getDataFromCache(String url) {
        Cache.Entry retrievedData = retrieveDataFromCache(url);
        String data = null;
        if (retrievedData != null) {
            try {
                data = new String(retrievedData.data, "UTF-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
        return data;
    }

    private Cache.Entry retrieveDataFromCache(String url) {
        Cache cache = AppController.getInstance().getRequestQueue().getCache();
        Cache.Entry entry = cache.get(url);
        return entry;
    }
}



