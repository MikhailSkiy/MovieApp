package com.example.admin.moviesapp.requests;

import android.net.Uri;

import com.android.volley.Cache;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.admin.moviesapp.helpers.States;
import com.example.admin.moviesapp.managers.AppController;
import com.example.admin.moviesapp.managers.RequestManager;
import com.example.admin.moviesapp.models.Movie;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;

import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import timber.log.Timber;

/**
 * Created by Mikhail Valuyskiy on 01.09.2015.
 */
public class MovieRequest extends GeneralRequest {

    //region Keys for building query
    private String BASE_URL = "http://api.themoviedb.org/3/discover/movie?";
    private String SORT_BY = "sort_by";
    private String WITH_GENRES = "with_genres";
    private String API_KEY = "api_key";

    //endregion

    //region Values for building query
    private String POPULARITY_DESC = "popularity.desc";
    private String API_KEY_VALUE = "";
    //endregion

    private RequestManager manager_;

    public MovieRequest(RequestManager manager) {
        this.manager_ = manager;
    }

    public void postRequest() {
        String url = createMoviesUrl();
        Timber.v("Created URL", url);
        postGetRequest(url);
    }

    public void getMoviesList(String response) {
        List<Movie> moviesList = getMoviesFromJson(response);
        manager_.sendMessage(manager_.obtainMessage(States.MOVIES_REQUEST_WAS_PARSED, moviesList));
    }

    // Creates url for movies request
    private String createMoviesUrl() {
        String url = null;
        Uri builtUri = Uri.parse(BASE_URL).buildUpon()
                .appendQueryParameter(SORT_BY, POPULARITY_DESC)
                .appendQueryParameter(API_KEY, getApiKey())
                .build();
        url = builtUri.toString();
        Timber.v(url);
        return url;
    }

    // use Volley for sending request
    private void postGetRequest(final String url) {
        String tag = "movie_request";
        // Check if there is saved data in the cache
        // If so, there is no need to send same request, just take it from cache
        // Otherwise, send request
        String response = getDataFromCache(url);
        if (response != null) {
            Timber.v("Data was cashed");
            manager_.sendMessage(manager_.obtainMessage(States.MOVIES_REQUEST_COMPLETED, response));
        } else {
            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            Timber.v("Data was not cashed");
                            String responseString = response.toString();
                            manager_.sendMessage(manager_.obtainMessage(States.MOVIES_REQUEST_COMPLETED, responseString));
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

    // use GSON for parsing request
    private List<Movie> getMoviesFromJson(String response) {

        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.setDateFormat("M/d/yy hh:mm a");
        Gson gson = gsonBuilder.create();

        Type listType = new TypeToken<List<Movie>>() {
        }.getType();
        List<Movie> movies = new ArrayList<Movie>();

        JsonParser parser = new JsonParser();
        JsonObject jsonObject = parser.parse(response).getAsJsonObject();
        JsonElement resultsElement = jsonObject.get("results");
        movies = (List<Movie>) gson.fromJson(resultsElement, listType);


        return movies;
    }

    // after that, when we got path of image, make request by means of Picasso for getting cover

    // Temporary method for getting api_key_value
    private String getApiKey() {
        return API_KEY_VALUE;
    }

}
