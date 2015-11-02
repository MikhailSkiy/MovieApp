package com.example.admin.moviesapp.models.requests;

import android.net.Uri;

import com.example.admin.moviesapp.helpers.Constants;
import com.example.admin.moviesapp.helpers.SharedPrefUtil;
import com.example.admin.moviesapp.helpers.States;

import timber.log.Timber;

/**
 * Created by Mikhail on 26.10.15.
 */
public class WatchlistMovieRequest extends UpdateWatchlistItemRequest {

    private final String TAG = "WatchlistMovieRequest";
    protected final String MOVIES_KEY = "movies";

    private long itemId_;
    private boolean operationFlag_;

    @Override
    public String getTag() {
        return TAG;
    }

    @Override
    public int getSuccessfulGetRequestStatus() {
        return States.WATCHLIST_REQUEST_COMPLETED;
    }

    @Override
    public String getMediaType() {
        return Constants.MOVIES_MEDIA_TYPE;
    }

    @Override
    public String getType() {
        return Constants.WATCHLIST;
    }

    @Override
    public void setOperationFlag(boolean flag) {
        this.operationFlag_ = flag;
    }

    @Override
    public boolean getOperationFlag() {
        return this.operationFlag_;
    }

    @Override
    public long getItemId() {
        return this.itemId_;
    }

    @Override
    public void setItemId(long itemId) {
        this.itemId_ = itemId;
    }

    @Override
    public boolean isRequestSuccesfull(String statusCode) {
        if (statusCode.equals(Integer.toString(Constants.WATCHLIST_SUCCESS_CODE))){
            return true;
        } else {
            return false;
        }
    }

    @Override
    public int getSuccessfullPostRequestStatus() {
        return States.MOVIE_WAS_ADDED_TO_WATCHLIST_SUCCESSFULLY;
    }
}
