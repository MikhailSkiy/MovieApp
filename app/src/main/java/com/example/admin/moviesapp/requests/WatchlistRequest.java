package com.example.admin.moviesapp.requests;

import android.net.Uri;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.admin.moviesapp.helpers.Constants;
import com.example.admin.moviesapp.helpers.SharedPrefUtil;
import com.example.admin.moviesapp.helpers.States;
import com.example.admin.moviesapp.managers.AppController;
import com.example.admin.moviesapp.managers.RequestManager;
import com.example.admin.moviesapp.models.Movie;
import com.example.admin.moviesapp.models.UserAccountInfo;

import org.json.JSONObject;

import java.util.List;

import timber.log.Timber;

/**
 * Created by Mikhail Valuyskiy on 14.10.2015.
 */
public class WatchlistRequest extends AbstarctMovieRequest {

    private final String WATCHLIST_KEY = "watchlist";
    private final String MOVIES_KEY = "movies";

    private static RequestManager manager_;

    public WatchlistRequest(RequestManager manager) {
        this.manager_ = manager;
    }

    @Override
    public void sendHttpRequest() {
        String url = createWatchlistUrl();
        sendWatchlistRequest(url);
    }

    /**
     * Creates url to get watchlist of current user
     */
    private String createWatchlistUrl() {
        // Get session_id from SharedPrefs
        String sessionId = SharedPrefUtil.getSessionIdFromSharedPrefs();
        // Get user_id from SharedPrefs
        String userId = SharedPrefUtil.getAccountIdFromSharedPrefs();
        // Buld uri
        Uri uri = Uri.parse(BASE_URL).buildUpon()
                .appendPath(userId)
                .appendPath(WATCHLIST_KEY)
                .appendPath(MOVIES_KEY)
                .appendQueryParameter(API_KEY, getApiKey())
                .appendQueryParameter(SESSION_KEY, sessionId)
                .build();

        String url = uri.toString();
        Timber.d("Created URL", url);
        return url;
    }

    private void sendWatchlistRequest(String url) {
        String tag = "session_id";
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(JsonObjectRequest.Method.GET, url,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        String responseString = response.toString();
                        List<Movie> account = getMoviesFromJson(responseString);
                        manager_.sendMessage(manager_.obtainMessage(States.WATCHLIST_REQUEST_COMPLETED, account));
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
