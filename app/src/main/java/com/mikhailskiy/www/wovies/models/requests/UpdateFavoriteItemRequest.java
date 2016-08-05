package com.mikhailskiy.www.wovies.models.requests;

import com.mikhailskiy.www.wovies.helpers.Constants;

import timber.log.Timber;

/**
 * Created by Mikhail on 26.10.15.
 */
public abstract class UpdateFavoriteItemRequest extends UpdateItemRequest {

    protected final String MOVIES_KEY = "movies";

    /**
     * Creates url to get list of favorite movies
     */
    @Override
    public String createGetUrl() {
        String url = createGetUrl(Constants.FAVORITE, MOVIES_KEY);
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
        String url = createPostUrl(Constants.FAVORITE);
        Timber.d("Created URL", url);
        return url;
    }

}
