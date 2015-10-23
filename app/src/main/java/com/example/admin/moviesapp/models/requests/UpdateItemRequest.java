package com.example.admin.moviesapp.models.requests;

import android.net.Uri;

import com.example.admin.moviesapp.helpers.Constants;
import com.example.admin.moviesapp.helpers.SharedPrefUtil;
import com.example.admin.moviesapp.models.network.Response;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

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

    // Returns media type for POST reqest (movie or tv)
    public abstract String getMediaType();

    // Returns type of request (e.g favotire or watchlist)
    public abstract String getType();

    // Returns selected item id
    public abstract long getItemId();

    // Set item id
    public abstract void setItemId(long itemId);

    // Checks is send request was successful
    public abstract boolean isRequestSuccesfull(String statusCode);

    // Returns status from object to manager
    public abstract int getSuccessfullPostRequestStatus();


    // Method for getting api_key value
    protected static String getApiKey() {
        return Constants.API_KEY_VALUE;
    }

}
