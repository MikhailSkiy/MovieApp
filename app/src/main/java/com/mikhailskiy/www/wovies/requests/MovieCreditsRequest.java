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
import com.mikhailskiy.www.wovies.models.MovieCredits;
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
 * Created by Mikhail Valuyskiy on 08.09.2015.
 */
public class MovieCreditsRequest {

    // http://api.themoviedb.org/3/person/2524/movie_credits?api_key=0bd95c30f721d1e94381142dc1ce3d50

    private static RequestManager manager_;

    //region Keys for building query
    private final String BASE_MOVIE_CREDITS_URL = "http://api.themoviedb.org/3/person/";
    private final String API_KEY = "api_key";
    private final String MOVIE_CREDITS = "movie_credits";
    //endregion

    public MovieCreditsRequest(RequestManager manager){
       this.manager_ = manager;
    }

    public void postRequest(String id){
        String url =  createMovieCreditsUrl(id);
        postGetRequest(url);
    }

    public void getMovieCreditsObject(String response){
        List<MovieCredits> movieCredits = getMovieCreditsObjectFromJson(response);
        Timber.v(Integer.toString(movieCredits.size()));
        // update adapter

    }

    private List<MovieCredits> getMovieCreditsObjectFromJson(String response) {
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.setDateFormat("M/d/yy hh:mm a");
        Gson gson = gsonBuilder.create();

        Type listType = new TypeToken<List<MovieCredits>>() {
        }.getType();
        List<MovieCredits> movieCreditsList = new ArrayList<MovieCredits>();

        JsonParser parser = new JsonParser();
        JsonObject jsonObject = parser.parse(response).getAsJsonObject();
        JsonElement resultElement = jsonObject.get("cast");
        movieCreditsList = (List<MovieCredits>) gson.fromJson(resultElement, listType);
        return movieCreditsList;
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
            List<MovieCredits> movieCreditsFromCache = getMovieCreditsObjectFromJson(response);
            manager_.sendMessage(manager_.obtainMessage(States.MOVIE_CREDITS_REQUEST_COMPLETED, movieCreditsFromCache));
        } else {
            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            Timber.v("Data was not cashed");
                            String responseString = response.toString();
                            List<MovieCredits> movieCredits = getMovieCreditsObjectFromJson(responseString);
                            manager_.sendMessage(manager_.obtainMessage(States.MOVIE_CREDITS_REQUEST_COMPLETED, movieCredits));
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


    private String createMovieCreditsUrl(String id){
        String url = null;
        Uri builtUri = Uri.parse(BASE_MOVIE_CREDITS_URL).buildUpon()
                .appendPath(id)
                .appendPath(MOVIE_CREDITS)
                .appendQueryParameter(API_KEY, getApiKey())
                .build();
        url = builtUri.toString();
        Timber.v(url);
        return url;
    }

    // Temporary method for getting api_key_value
    private String getApiKey() {
        return Constants.API_KEY_VALUE;
    }
}
