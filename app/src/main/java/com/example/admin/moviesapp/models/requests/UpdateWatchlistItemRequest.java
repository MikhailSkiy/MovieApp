package com.example.admin.moviesapp.models.requests;

import com.example.admin.moviesapp.helpers.Constants;

import timber.log.Timber;


/**
 * Created by Mikhail on 26.10.15.
 */
public abstract class UpdateWatchlistItemRequest extends UpdateItemRequest {

    protected final String MOVIES_KEY = "movies";

    /**
     * Creates url to get watchlist
     */
    @Override
    public String createGetUrl(){
        String url = createGetUrl(Constants.WATCHLIST,MOVIES_KEY);
        Timber.d("Created GET URL", url);
        return url;
    }

    /**
     * Creates url for POST request to
     * add selected movie in favorite list or to watchlist
     * Can be "favorite" or "watchlist"
     */
    @Override
    public String createPostUrl() {
        String url = createPostUrl(Constants.WATCHLIST);
        Timber.d("Created POST URL", url);
        return url;
    }

}
