package com.example.admin.moviesapp.requests;

/**
 * Created by Mikhail Valuyskiy on 14.10.2015.
 */

import android.net.Uri;

import com.example.admin.moviesapp.helpers.Constants;
import com.example.admin.moviesapp.helpers.SharedPrefUtil;
import com.example.admin.moviesapp.models.CommonMovie;
import com.example.admin.moviesapp.models.Movie;
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

    public abstract void sendHttpRequest();


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
     * Returns list of movies from json
     * @param jsonResponse - response from server
     * @return list of Movie objects
     */
    protected List<Movie> getMoviesFromJson(String jsonResponse){
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.setDateFormat("M/d/yy hh:mm a");
        Gson gson = gsonBuilder.create();

        Type listType = new TypeToken<List<Movie>>() {
        }.getType();
        List<Movie> movies = new ArrayList<Movie>();

        JsonParser parser = new JsonParser();
        JsonObject jsonObject = parser.parse(jsonResponse).getAsJsonObject();
        JsonElement resultsElement = jsonObject.get("results");
        movies = (List<Movie>) gson.fromJson(resultsElement, listType);
        return movies;
    }

    // Method for getting api_key value
    protected static String getApiKey() {
        return Constants.API_KEY_VALUE;
    }

}
