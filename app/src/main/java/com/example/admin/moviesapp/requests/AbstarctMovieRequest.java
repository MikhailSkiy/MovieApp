package com.example.admin.moviesapp.requests;

/**
 * Created by Mikhail Valuyskiy on 14.10.2015.
 */

import com.example.admin.moviesapp.helpers.Constants;
import com.example.admin.moviesapp.models.Movie;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * The basic request for requests connected with movies
 */
public abstract class AbstarctMovieRequest {
    protected final String BASE_URL = "http://api.themoviedb.org/3/account/";
    protected final String API_KEY = "api_key";
    protected final String SESSION_KEY = "session_id";

    abstract void sendHttpRequest();

    /**
     * Returns list of movies from json
     * @param jsonResponse - response from server
     * @return list of Movie objects
     */
    protected List<Movie> getMoviesFromJson(String jsonResponse){
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.setDateFormat("M/d/yy hh:mm a");
        Gson gson = gsonBuilder.create();

        Type listType = new TypeToken<List<Movie>>() {
        }.getType();
        List<Movie> movies = new ArrayList<Movie>();

        JsonParser parser = new JsonParser();
        JsonObject jsonObject = parser.parse(jsonResponse).getAsJsonObject();
        JsonElement resultsElement = jsonObject.get("results");
        movies = (List<Movie>) gson.fromJson(resultsElement, listType);
        return movies;
    }

    // Method for getting api_key value
    protected static String getApiKey() {
        return Constants.API_KEY_VALUE;
    }

}
