package com.example.admin.moviesapp.models.requests;

import android.net.Uri;

import com.example.admin.moviesapp.helpers.Constants;
import com.example.admin.moviesapp.helpers.SharedPrefUtil;
import com.example.admin.moviesapp.helpers.States;

import timber.log.Timber;

/**
 * Created by Mikhail Valuyskiy on 23.10.2015.
 */
public class FavoriteMovieRequest extends UpdateItemRequest {

    private final String TAG = "FavoriteMovieRequest";
    private final String FAVORITES_KEY = "favorite";
    protected final String MOVIES_KEY = "movies";

     private long itemId_;

    @Override
    public String getTag() {
        return TAG;
    }

    /**
     * Creates url to get list of movies
     */
    @Override
    public String createGetUrl() {
        // Get session_id from SharedPrefs
        String sessionId = SharedPrefUtil.getSessionIdFromSharedPrefs();
        // Get user_id from SharedPrefs
        String userId = SharedPrefUtil.getAccountIdFromSharedPrefs();
        // Buld uri
        Uri uri = Uri.parse(BASE_URL).buildUpon()
                .appendPath(userId)
                .appendPath(FAVORITES_KEY)
                .appendPath(MOVIES_KEY)
                .appendQueryParameter(API_KEY, getApiKey())
                .appendQueryParameter(SESSION_KEY, sessionId)
                .build();

        String url = uri.toString();
        Timber.d("Created URL", url);
        return url;
    }

    /**
     * Creates url for POST request to
     * add selected movie in favorite list or to watchlist
     * Can be "favorite" or "watchlist"
     */
    @Override
    public String createPostUrl() {
        // Get session_id from SharedPrefs
        String sessionId = SharedPrefUtil.getSessionIdFromSharedPrefs();
        // Get user_id from SharedPrefs
        String userId = SharedPrefUtil.getAccountIdFromSharedPrefs();
        // Buld uri
        Uri uri = Uri.parse(BASE_URL).buildUpon()
                .appendPath(userId)
                .appendPath(FAVORITES_KEY)
                .appendQueryParameter(API_KEY, getApiKey())
                .appendQueryParameter(SESSION_KEY, sessionId)
                .build();

        String url = uri.toString();
        Timber.d("Created URL", url);
        return url;
    }

    @Override
    public int getSuccessfulGetRequestStatus() {
        return States.FAVORITES_REQUEST_COMPLETED;
    }

    @Override
    public String getMediaType() {
        return "movie";
    }

    @Override
    public String getType() {
        return "favorite";
    }

    @Override
    public long getItemId() {
        return this.itemId_;
    }

    @Override
    public void setItemId(long itemId) {
        itemId_ = itemId;
    }

    @Override
    public boolean isRequestSuccesfull(String statusCode) {
        if (statusCode.equals(Integer.toString(Constants.FAVORITE_SUCCESS_CODE))) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public int getSuccessfullPostRequestStatus() {
        return States.MOVIE_MARKED_SUCCESSFULLY;
    }
}
