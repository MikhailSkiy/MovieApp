package com.example.admin.moviesapp.models.requests;

import android.net.Uri;

import com.example.admin.moviesapp.helpers.Constants;
import com.example.admin.moviesapp.helpers.SharedPrefUtil;
import com.example.admin.moviesapp.helpers.States;

import timber.log.Timber;

/**
 * Created by Mikhail Valuyskiy on 23.10.2015.
 */
public class FavoriteMovieRequest extends UpdateFavoriteItemRequest {

    private final String TAG = "FavoriteMovieRequest";

    private long itemId_;
    private boolean operationFlag_;

    @Override
    public String getTag() {
        return TAG;
    }

    @Override
    public int getSuccessfulGetRequestStatus() {
        return States.FAVORITES_REQUEST_COMPLETED;
    }

    @Override
    public String getMediaType() {
        return Constants.MOVIES_MEDIA_TYPE;
    }

    @Override
    public String getType() {
        return Constants.FAVORITE;
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
