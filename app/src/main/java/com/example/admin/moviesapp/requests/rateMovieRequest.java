package com.example.admin.moviesapp.requests;

import android.net.Uri;

import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.admin.moviesapp.helpers.Constants;
import com.example.admin.moviesapp.helpers.SharedPrefUtil;
import com.example.admin.moviesapp.helpers.States;
import com.example.admin.moviesapp.managers.AppController;
import com.example.admin.moviesapp.managers.RequestManager;
import com.example.admin.moviesapp.models.network.Response;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import timber.log.Timber;

/**
 * Created by Mikhail on 28.10.15.
 */
public class RateMovieRequest {

    private static final String BASE_URL = "http://api.themoviedb.org/3/movie";
    private static final String API_KEY = "api_key";
    private static final String SESSION_ID = "session_id";
    private static final String RATING = "rating";

    private static RequestManager manager_;
    public RateMovieRequest (RequestManager manager){
        this.manager_ = manager;
    }

    public void setMovieRating(long movieId, double rating){
        String url = createRatingUrl(movieId);
        sendRatingRequest(url, rating);
    }

    private String createRatingUrl(long movieId){
        // Get session_id from SharedPreferences
        String sessionId = SharedPrefUtil.getSessionIdFromSharedPrefs();
        Uri uri = Uri.parse(BASE_URL).buildUpon()
                .appendPath(Double.toString(movieId))
                .appendPath(RATING)
                .appendQueryParameter(API_KEY,getApiKey())
                .appendQueryParameter(SESSION_ID,sessionId)
                .build();

        String url = uri.toString();
        Timber.d("Created Url: ", url);
        return url;
    }

    private void sendRatingRequest(String url, double rating){

        final JSONObject body = new JSONObject();
        try{
            body.put("value",Double.toString(rating));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest stringRequest = new JsonObjectRequest(Request.Method.POST, url, new com.android.volley.Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Response watchlistResponse = getResponseFromJson(response.toString());
                if (watchlistResponse.statusCode.equals(Integer.toString(Constants.WATCHLIST_SUCCESS_CODE))) {
                    manager_.sendMessage(manager_.obtainMessage(States.MOVIE_RATED_SUCCESSFULLY,watchlistResponse.statusMessage));
                }
            }
        }, new com.android.volley.Response.ErrorListener() {
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
    protected Response getResponseFromJson(String json){
        GsonBuilder gsonBuilder = new GsonBuilder();
        Gson gson = gsonBuilder.create();
        Response response = new Response();
        response = gson.fromJson(json,Response.class);
        return response;
    }

    private String getApiKey() {
        return Constants.API_KEY_VALUE;
    }

}
