package com.example.admin.moviesapp.helpers;

import android.support.annotation.IntDef;

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
}
