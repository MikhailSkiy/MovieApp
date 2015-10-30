package com.example.admin.moviesapp.events.successfullResponse;

import android.support.annotation.IntDef;

import com.example.admin.moviesapp.models.network.Response;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Created by Mikhail Valuyskiy on 22.10.2015.
 */
public class SuccessfullAlert {

    private Response response_;
    public int currentType;

    @Retention(RetentionPolicy.SOURCE)
    @IntDef({FavoriteMovieRequestType,WatchlistMovieRequestType,RateMovieRequestType})
    public @interface RequestType{}
    public static final int FavoriteMovieRequestType = 1;
    public static final int WatchlistMovieRequestType = 2;
    public static final int RateMovieRequestType = 3;

    public SuccessfullAlert(Response listOperationResponse, @RequestType int requestType){
        response_ = listOperationResponse;
        currentType = requestType;
    }

    public String getAlertText(){
        return response_.statusMessage;
    }

    public String getAlertStatus(){
        return response_.statusCode;
    }
}
