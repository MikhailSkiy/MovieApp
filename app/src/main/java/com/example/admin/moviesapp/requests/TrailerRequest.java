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
import com.example.admin.moviesapp.models.Trailer;
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
 * Created by Mikhail on 06.09.2015.
 */
public class TrailerRequest implements RequestFactory{

    //region Keys for building query
    private final String BASE_URL = "http://api.themoviedb.org/3/movie/";
    private final String VIDEOS = "videos";
    private final String API_KEY = "api_key";
    //endregion

    private static RequestManager manager_;

    public TrailerRequest(RequestManager manager){
        this.manager_ = manager;
    }

    public void postRequest(long id){
        String url = createTrailerUrl(id);
        Timber.v("Created Trailers Url", url);
        postGetRequest(url);
    }

    // Creates url for trailers request
    private String createTrailerUrl(long id){
        String url = null;
        Uri builtUri = Uri.parse(BASE_URL).buildUpon()
                .appendEncodedPath(Long.toString(id))
                .appendEncodedPath(VIDEOS)
                .appendQueryParameter(API_KEY,getApiKey())
                .build();
        Timber.v(builtUri.toString());
        url = builtUri.toString();
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
                manager_.sendMessage(manager_.obtainMessage(States.TRAILERS_REQUEST_COMPLETED, response));
            } else {
                JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url,
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                Timber.v("Data was not cashed");
                                String responseString = response.toString();
                                manager_.sendMessage(manager_.obtainMessage(States.TRAILERS_REQUEST_COMPLETED, responseString));
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

    public static void getTrailersList(String trailerServerResponse){
        List<Trailer> trailers = getTrailersFromJson(trailerServerResponse);
        manager_.sendMessage(manager_.obtainMessage(States.TRAILERS_REQUEST_WAS_PARSED, trailers));
    }

    private static List<Trailer> getTrailersFromJson(String response){
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.setDateFormat("M/d/yy hh:mm a");
        Gson gson = gsonBuilder.create();

        Type listType = new TypeToken<List<Trailer>>() {
        }.getType();
        List<Trailer> trailers = new ArrayList<Trailer>();

        JsonParser parser = new JsonParser();
        JsonObject jsonObject = parser.parse(response).getAsJsonObject();
        JsonElement resultElement = jsonObject.get("results");
        trailers = (List<Trailer>)gson.fromJson(resultElement,listType);
        return trailers;
    }

    private String getApiKey() {
        return Constants.API_KEY_VALUE;
    }

}
