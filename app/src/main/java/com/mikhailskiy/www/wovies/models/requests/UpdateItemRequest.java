package com.mikhailskiy.www.wovies.models.requests;

import android.net.Uri;

import com.mikhailskiy.www.wovies.helpers.Constants;
import com.mikhailskiy.www.wovies.helpers.SharedPrefUtil;

import timber.log.Timber;

/**
 * Created by Mikhail Valuyskiy on 23.10.2015.
 */
public abstract class UpdateItemRequest {


    protected final String BASE_URL = "http://api.themoviedb.org/3/account/";
    protected final String API_KEY = "api_key";
    protected final String SESSION_KEY = "session_id";

    // Returns tag, necessary for volley network operations
    public abstract String getTag();

    // Creates URL for GET request, such as get List of Movies
    public abstract String createGetUrl();

    // Creates URL for POST request, which is necessary for updating data
    // e.g.mark movie as favorite or add movie to watchlist
    public abstract String createPostUrl();

    // Returns status from object to manager (e.g State for manager)
    public abstract int getSuccessfulGetRequestStatus();

    // Returns media type for POST request (movie or tv)
    public abstract @Constants.MediaType String getMediaType();

    // Returns type of request (e.g favorite or watchlist)
    public abstract @Constants.ListType String getType();

    // Returns selected item id
    public abstract long getItemId();

    // Sets item id
    public abstract void setItemId(long itemId);

    // Sets the operation flag: true - add, false - delete
    // Used for adding or deleting item as post parameter
    // TRUE for adding item. FALSE for deleting item
    public abstract void setOperationFlag(boolean flag);

    // Returns the operation flag
    public abstract boolean getOperationFlag();

    // Checks is send request was successful
    public abstract boolean isRequestSuccesfull(@Constants.MoviesStatus String statusCode);

    // Returns status from object to manager
    public abstract int getSuccessfullPostRequestStatus();


    // Method for getting api_key value
    protected static String getApiKey() {
        return Constants.API_KEY_VALUE;
    }

    /**
     * Creates url to get list of movies
     */

    protected String createGetUrl(@Constants.ListType String listType, String param) {
        // Get session_id from SharedPrefs
        String sessionId = SharedPrefUtil.getSessionIdFromSharedPrefs();
        if (sessionId == null){

        }
        // Get user_id from SharedPrefs
        String userId = SharedPrefUtil.getAccountIdFromSharedPrefs();
        // Buld uri
        Uri uri = Uri.parse(BASE_URL).buildUpon()
                .appendPath(userId)
                .appendPath(listType.toString())
                .appendPath(param)
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
   protected String createPostUrl(@Constants.ListType String listType) {
        // Get session_id from SharedPrefs
        String sessionId = SharedPrefUtil.getSessionIdFromSharedPrefs();
        // Get user_id from SharedPrefs
        String userId = SharedPrefUtil.getAccountIdFromSharedPrefs();
        // Buld uri
        Uri uri = Uri.parse(BASE_URL).buildUpon()
                .appendPath(userId)
                .appendPath(listType.toString())
                .appendQueryParameter(API_KEY, getApiKey())
                .appendQueryParameter(SESSION_KEY, sessionId)
                .build();

        String url = uri.toString();
        Timber.d("Created URL", url);
        return url;
    }

}
