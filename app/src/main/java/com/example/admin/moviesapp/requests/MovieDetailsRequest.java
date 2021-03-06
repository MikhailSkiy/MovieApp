package com.example.admin.moviesapp.requests;

import android.net.Uri;

import com.android.volley.Cache;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.admin.moviesapp.helpers.Constants;
import com.example.admin.moviesapp.helpers.States;
import com.example.admin.moviesapp.managers.AppController;
import com.example.admin.moviesapp.managers.RequestManager;
import com.example.admin.moviesapp.models.MovieDetails;
import com.example.admin.moviesapp.models.ProductionCompany;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.List;

import timber.log.Timber;

/**
 * Created by Mikhail on 02.09.2015.
 */
public class MovieDetailsRequest implements RequestFactory {

    //region Keys for building query
    private final String BASE_MOVIE_DETAILS_URL = "http://api.themoviedb.org/3/movie/";
    private final String API_KEY = "api_key";
    private final String LANGUAGE = "language";
    //endregion

    //region Values for building query
//    private final String LANGUAGE_VALUE = "ru";
    private final String LANGUAGE_VALUE = "en";
    //endregion

    private static RequestManager manager_;

    public MovieDetailsRequest(RequestManager manager){
        this.manager_ = manager;
    }

    public void postRequest(long id){
        String url = createMovieDetailsUrl(id);
        postGetRequest(url);
    }

    public static void getMovieObjects(String response){
        getMovieDetailsObject(response);
    }

    private static void getMovieDetailsObject(String response){
        MovieDetails movieDetails = getMovieDetailsObjectFromJson(response);
        manager_.sendMessage(manager_.obtainMessage(States.MOVIE_DETAILS_REQUEST_WAS_PARSED, movieDetails));
    }

    private String createMovieDetailsUrl(long id){
        String url = null;
        Uri builtUri = Uri.parse(BASE_MOVIE_DETAILS_URL).buildUpon()
                .appendPath(Long.toString(id))
                .appendQueryParameter(LANGUAGE, LANGUAGE_VALUE)
                .appendQueryParameter(API_KEY, getApiKey())
                .build();
        url = builtUri.toString();
        Timber.v(url);
        return url;
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
            manager_.sendMessage(manager_.obtainMessage(States.MOVIE_DETAILS_REQUEST_COMPLETED, response));
        } else {
            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            Timber.v("Data was not cashed");
                            String responseString = response.toString();
                            manager_.sendMessage(manager_.obtainMessage(States.MOVIE_DETAILS_REQUEST_COMPLETED, responseString));
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

    private static MovieDetails getMovieDetailsObjectFromJson(String response){
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.setDateFormat("M/d/yy hh:mm a");
        Gson gson = gsonBuilder.create();
        MovieDetails movieDetails = new MovieDetails();

        movieDetails = gson.fromJson(response,MovieDetails.class);
        Timber.v(movieDetails.getOriginalTitle());
        return movieDetails;
        // to be contined...
    }

    // Temporary method for getting api_key_value
    private String getApiKey() {
        return Constants.API_KEY_VALUE;
    }

}
