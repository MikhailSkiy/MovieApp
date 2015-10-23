package com.example.admin.moviesapp.requests;

import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.admin.moviesapp.helpers.Constants;
import com.example.admin.moviesapp.helpers.States;
import com.example.admin.moviesapp.managers.AppController;
import com.example.admin.moviesapp.managers.RequestManager;
import com.example.admin.moviesapp.models.Movie;
import com.example.admin.moviesapp.models.network.Response;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import timber.log.Timber;

/**
 * Created by Mikhail Valuyskiy on 14.10.2015.
 */

/**
 * User-specific request to get user's watchlist
 * For successful response, user should be logged in
 */
public class WatchlistRequest extends AbstarctMovieRequest {

    private final String WATCHLIST_KEY = "watchlist";

    private static RequestManager manager_;

    public WatchlistRequest(RequestManager manager) {
        this.manager_ = manager;
    }

    @Override
    public void sendGetRequest() {
        String url = createRequestUrl(WATCHLIST_KEY);
        sendWatchlistRequest(url);
    }

    @Override
    public void sendPostRequest(long movieId){
        String url = createPostRequestUrl(WATCHLIST_KEY);
        addMovieToWatchlist(url, movieId);
    }

    private void sendWatchlistRequest(String url) {
        String tag = "watchlist_request";
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(JsonObjectRequest.Method.GET, url,
                new com.android.volley.Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        String responseString = response.toString();
                        List<Movie> account = getMoviesFromJson(responseString);
                        manager_.sendMessage(manager_.obtainMessage(States.WATCHLIST_REQUEST_COMPLETED, account));
                    }
                },
                new com.android.volley.Response.ErrorListener() {
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
    private void addMovieToWatchlist(String url, final long movieId){
        final JSONObject body = new JSONObject();
        try{
            body.put("media_type","movie");
            body.put("media_id",Long.toString(movieId));
            body.put("watchlist",true);
        } catch (JSONException e){
            e.printStackTrace();
        }

        JsonObjectRequest stringRequest = new JsonObjectRequest(Request.Method.POST, url, new com.android.volley.Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Response watchlistResponse = getResponseFromJson(response.toString());
                if (watchlistResponse.statusCode.equals(Integer.toString(Constants.WATCHLIST_SUCCESS_CODE))) {
                    manager_.sendMessage(manager_.obtainMessage(States.MOVIE_MARKED_SUCCESSFULLY,watchlistResponse.statusMessage));
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
}
