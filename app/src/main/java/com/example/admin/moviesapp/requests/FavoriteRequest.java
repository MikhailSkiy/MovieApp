package com.example.admin.moviesapp.requests;

/**
 * Created by Mikhail Valuyskiy on 15.10.2015.
 */

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.admin.moviesapp.helpers.States;
import com.example.admin.moviesapp.managers.AppController;
import com.example.admin.moviesapp.managers.RequestManager;
import com.example.admin.moviesapp.models.Movie;

import org.json.JSONObject;

import java.util.List;

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

    @Override
    public void sendHttpRequest() {
        String url = createRequestUrl(FAVORITES_KEY);
        sendFavoriteRequest(url);
    }

    private void sendFavoriteRequest(String url) {
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
}
