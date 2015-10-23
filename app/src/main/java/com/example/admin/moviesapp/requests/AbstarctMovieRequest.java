package com.example.admin.moviesapp.requests;

/**
 * Created by Mikhail Valuyskiy on 14.10.2015.
 */

import android.net.Uri;

import com.example.admin.moviesapp.helpers.Constants;
import com.example.admin.moviesapp.helpers.SharedPrefUtil;
import com.example.admin.moviesapp.models.Movie;
import com.example.admin.moviesapp.models.network.Response;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import timber.log.Timber;

/**
 * The basic request for requests connected with movies
 */
public abstract class AbstarctMovieRequest {
    protected final String BASE_URL = "http://api.themoviedb.org/3/account/";
    protected final String API_KEY = "api_key";
    protected final String MOVIES_KEY = "movies";
    protected final String SESSION_KEY = "session_id";

    public abstract void sendGetRequest();
    public abstract void sendPostRequest(long movieId);


    /**
     * Creates url to get list of movies
     * @param requestSpecificKey - the key, which depend on the request
     * @return
     */
   protected String createRequestUrl(String requestSpecificKey) {
        // Get session_id from SharedPrefs
        String sessionId = SharedPrefUtil.getSessionIdFromSharedPrefs();
        // Get user_id from SharedPrefs
        String userId = SharedPrefUtil.getAccountIdFromSharedPrefs();
        // Buld uri
        Uri uri = Uri.parse(BASE_URL).buildUpon()
                .appendPath(userId)
                .appendPath(requestSpecificKey)
                .appendPath(MOVIES_KEY)
                .appendQueryParameter(API_KEY, getApiKey())
                .appendQueryParameter(SESSION_KEY, sessionId)
                .build();

        String url = uri.toString();
        Timber.d("Created URL", url);
        return url;
    }

    /**
     * Creates url for POST request to
     * add selected movie in favorite list or to watchlist
     * @param requestSpecificKey Can be "favorite" or "watchlist"
     */
    protected String createPostRequestUrl(String requestSpecificKey) {
        // Get session_id from SharedPrefs
        String sessionId = SharedPrefUtil.getSessionIdFromSharedPrefs();
        // Get user_id from SharedPrefs
        String userId = SharedPrefUtil.getAccountIdFromSharedPrefs();
        // Buld uri
        Uri uri = Uri.parse(BASE_URL).buildUpon()
                .appendPath(userId)
                .appendPath(requestSpecificKey)
                .appendQueryParameter(API_KEY, getApiKey())
                .appendQueryParameter(SESSION_KEY, sessionId)
                .build();

        String url = uri.toString();
        Timber.d("Created URL", url);
        return url;
    }



    /**
     * Parse json result and get status_code and status_message
     * @param json
     * @return session_id
     */
    protected Response getResponseFromJson(String json){
        GsonBuilder gsonBuilder = new GsonBuilder();
        Gson gson = gsonBuilder.create();
        Response response = new Response();
        response = gson.fromJson(json,Response.class);
        return response;
    }

}
