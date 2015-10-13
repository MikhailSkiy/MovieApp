package com.example.admin.moviesapp.helpers;

/**
 * Created by Mikhail Valuyskiy on 02.09.2015.
 */
public class States {
    // Request for getting list of movies
    // Movie discover requests start from 0-10
    public static final int MOVIES_REQUEST = 0;
    public static final int MOVIES_REQUEST_COMPLETED = 1;
    public static final int MOVIES_REQUEST_WAS_PARSED = 2;

    public static final int IMAGE_DOWNLOADED = 3;
    public static final int COVER_DOWNLOADED = 4;
    public static final int CAST_PROFILE_DOWNLOADED = 5;

    // Movie details errors starts from 20-29
    public static final int MOVIE_DETAILS_REQUEST_COMPLETED = 20;
    public static final int MOVIE_DETAILS_REQUEST_WAS_PARSED = 21;
    public static final int MOVIES_DETAILS_REQUEST = 22;

    // Trailers requests
    public static final int TRAILERS_REQUEST = 30;
    public static final int TRAILERS_REQUEST_COMPLETED = 31;
    public static final int TRAILERS_REQUEST_WAS_PARSED = 32;

    // Casts request
    public static final int CASTS_REQUEST = 40;
    public static final int CASTS_REQUEST_COMPLETED = 41;
    public static final int CASTS_REQUEST_WAS_PARSED = 42;

    // Cast Details request
    public static final int CAST_DETAILS_REQUEST = 50;
    public static final int CAST_DETAILS_REQUEST_COMPLETED=51;
    public static final int CAST_DETAILS_IMAGE_DOWNLOADED = 52;

    // Movie Credits request
    public static final int MOVIE_CREDITS_REQUEST = 60;
    public static final int MOVIE_CREDITS_REQUEST_COMPLETED = 61;
    public static final int MOVIE_CREDITS_IMAGE_DOWNLOADED = 62;

    // Auth requests
    public static final int LOGIN_REQUEST = 70;
    public static final int TOKEN_REQUEST_RECEIVED = 71;
    public static final int SESSION_ID_REQUEST = 72;
    public static final int SESSION_ID_RECEIVED = 73;

    // Account requests
    public static final int ACCOUNT_INFO_REQUEST = 80;
    public static final int ACCOUNT_REQUEST_COMPLETED = 81;

    // Error msgs starts from 90-99
    public static final int VOLLEY_REQUEST_FAILED = 90;
}
