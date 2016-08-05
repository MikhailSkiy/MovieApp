package com.mikhailskiy.www.wovies.helpers;

import android.support.annotation.IntDef;
import android.support.annotation.StringDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Created by Mikhail on 06.09.2015.
 */
public class Constants {
    public final static String API_KEY_VALUE = "0bd95c30f721d1e94381142dc1ce3d50";
    public static final int DEFAULT_CROPPING_RADIUS = 100;

    @Retention(RetentionPolicy.SOURCE)
    @IntDef({CONNECTION_STATUS_OK,NO_CONNECTION})
    public @interface ConnectionStatus{}

    public static final int CONNECTION_STATUS_OK = 0;
    public static final int NO_CONNECTION = 1;

    // Constants for movie operations such as add to favorite list
    // or add to watchlist
    @Retention(RetentionPolicy.SOURCE)
    @IntDef({FAVORITE_SUCCESS_CODE,WATCHLIST_SUCCESS_CODE})
    public @interface MoviesStatus{}
    public static final int FAVORITE_SUCCESS_CODE = 12;
    public static final int WATCHLIST_SUCCESS_CODE = 1;

    // Constants for media types which necessary for POST request
    // link http://docs.themoviedb.apiary.io/#reference/account/accountidwatchlist/post
    @Retention(RetentionPolicy.SOURCE)
    @StringDef({MOVIES_MEDIA_TYPE,TV_MEDIA_TYPE})
    public @interface MediaType{}
    public static final String MOVIES_MEDIA_TYPE = "movie";
    public static final String TV_MEDIA_TYPE = "tv";

    // Constants for specific POST request like as favorite or watchlist
    @Retention(RetentionPolicy.SOURCE)
    @StringDef({FAVORITE,WATCHLIST})
    public @interface ListType{}
    public static final String FAVORITE = "favorite";
    public static final String WATCHLIST = "watchlist";

    // Constants for type of requests in main menu list
    @Retention(RetentionPolicy.SOURCE)
    @IntDef({MOVIES_MODE,FAVORITE_MOVIES_MODE,WATCHLIST_MODE})
    public @interface MoviesActivityListMode{}
    public static final int MOVIES_MODE = 0;
    public static final int FAVORITE_MOVIES_MODE = 1;
    public static final int WATCHLIST_MODE = 2;
}
