package com.example.admin.moviesapp.requests;

/**
 * Created by Mikhail Valuyskiy on 15.10.2015.
 */

import android.net.Uri;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.admin.moviesapp.helpers.SharedPrefUtil;
import com.example.admin.moviesapp.helpers.States;
import com.example.admin.moviesapp.managers.AppController;
import com.example.admin.moviesapp.managers.RequestManager;
import com.example.admin.moviesapp.models.Movie;
import com.example.admin.moviesapp.models.UserAccountInfo;
import com.example.admin.moviesapp.models.network.MarkAsFavoriteResponse;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import timber.log.Timber;

/**
 * User-specific request to get list of favorite movies
 * For successful response, user should be logged in
 */
public class FavoriteRequest extends AbstarctMovieRequest {

    private final String FAVORITES_KEY = "favorite";

    private static RequestManager manager_;

    public FavoriteRequest(RequestManager manager) {
        this.manager_ = manager;
    }

    /**
     * Sends GET request to get list of favorite movies
     */
    @Override
    public void sendGetRequest() {
        String url = createRequestUrl(FAVORITES_KEY);
        getListOfFavoriteMovies(url);
    }

    /**
     * Sends POST request to mark selected movie as favorite
     */
    @Override
    public void sendPostRequest(long movieId){
        String url = createPostRequestUrl(FAVORITES_KEY);
        markMovieAsFavorite(url, movieId);
    }


    /**
     * Sends GET request to get list of favorite movies
     */
    private void getListOfFavoriteMovies(String url) {
        String tag = "favorite_request";

             JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(JsonObjectRequest.Method.GET, url,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        String responseString = response.toString();
                        List<Movie> account = getMoviesFromJson(responseString);
                        manager_.sendMessage(manager_.obtainMessage(States.FAVORITES_REQUEST_COMPLETED, account));
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
     * Sends POST request to mark movie as favorite by given movieId
     * @param url basic url for saving movie as favorite
     * @param movieId id of selected movie
     */
    private void markMovieAsFavorite(String url, final long movieId){
        final JSONObject body = new JSONObject();
        try{
            body.put("media_type","movie");
            body.put("media_id",Long.toString(movieId));
            body.put("favorite",true);
        } catch (JSONException e){
            e.printStackTrace();
        }

        JsonObjectRequest stringRequest = new JsonObjectRequest(Request.Method.POST, url, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                MarkAsFavoriteResponse markAsFavoriteResponse = getResponseFromJson(response.toString());
                if (markAsFavoriteResponse.statusCode.equals("12")) {
                    manager_.sendMessage(manager_.obtainMessage(States.MOVIE_MARKED_SUCCESSFULLY,markAsFavoriteResponse.statusMessage));
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }) {

            @Override
            public byte[] getBody() {
                return body.toString().getBytes();
            }

            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("Accept","application/json");
                params.put("Content-Type", "application/json");
                return params;
            }
        };

        AppController.getInstance().addToRequestQueue(stringRequest);
    }

    /**
     * Parse json result and get status_code and status_message
     * @param json
     * @return session_id
     */
    private MarkAsFavoriteResponse getResponseFromJson(String json){
        GsonBuilder gsonBuilder = new GsonBuilder();
        Gson gson = gsonBuilder.create();
        MarkAsFavoriteResponse response = new MarkAsFavoriteResponse();
        response = gson.fromJson(json,MarkAsFavoriteResponse.class);
        return response;
    }

}
