package com.example.admin.moviesapp.requests;

import android.net.Uri;

import com.example.admin.moviesapp.models.Movie;

import java.net.URL;
import java.util.List;

/**
 * Created by Mikhail Valuyskiy on 01.09.2015.
 */
public class MovieRequest {

    //region Keys for building query
    private String BASE_URL = "http://api.themoviedb.org/3/discover/movie?";
    private String SORT_BY = "sort_by";
    private String API_KEY = "api_key";
    //endregion

    //region Values for building query
    private String POPULARITY_DESC = "popularity.desc";
    //endregion

    // Creates url for movies request
    public String createMoviesUrl(){
        String url = null;
        Uri builtUri = Uri.parse(BASE_URL).buildUpon()
                .appendQueryParameter(SORT_BY,POPULARITY_DESC)
                .appendQueryParameter(API_KEY,getApiKey())
                .build();
        url = builtUri.toString();
        return url;
    }
    // use volley for sending request
    public String sendRequest(URL url){}

    // use GSON for parsing request
    public List<Movie> getMoviesFromJson(String response){}

    // after that, when we got path of image, make request by means of Picass for getting cover

}
