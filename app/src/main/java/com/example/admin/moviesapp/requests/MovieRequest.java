package com.example.admin.moviesapp.requests;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;

import com.android.volley.Cache;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.admin.moviesapp.R;
import com.example.admin.moviesapp.activities.MainActivity;
import com.example.admin.moviesapp.helpers.Constants;
import com.example.admin.moviesapp.helpers.GenresMap;
import com.example.admin.moviesapp.helpers.States;
import com.example.admin.moviesapp.managers.AppController;
import com.example.admin.moviesapp.managers.RequestManager;
import com.example.admin.moviesapp.models.Movie;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;

import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import timber.log.Timber;

/**
 * Created by Mikhail Valuyskiy on 01.09.2015.
 */
public class MovieRequest implements RequestFactory {

    //region Keys for building query
    private final String BASE_URL = "http://api.themoviedb.org/3/discover/movie?";
    private final String SORT_BY = "sort_by";
    private final String WITH_GENRES = "with_genres";
    private final String PAGE = "page";
    private final String LANGUAGE = "language";
    private final String API_KEY = "api_key";
    //endregion

    //region Values for building query
    private final String POPULARITY = "popularity.";
    private final String RATING = "rating.";
    private final String Revenue = "revenue.";
    private final String LANGUAGE_VALUE = "ru";
    private final String API_KEY_VALUE = "0bd95c30f721d1e94381142dc1ce3d50";
    //endregion

    private static RequestManager manager_;

    private int page_;
    private int id_;

    public void setPage(int page) {
        this.page_ = page;
    }

    public void setId(int id) {
        this.id_ = id;
    }



    public MovieRequest(RequestManager manager){
        this.manager_ = manager;
    }

    public void postRequest(long id) {
        String url = createMoviesUrl();
        Timber.v("Created URL", url);
        postGetRequest(url);
    }



    public static void getMovieObjects(String response) {
        List<Movie> moviesList = getMoviesFromJson(response);
        manager_.sendMessage(manager_.obtainMessage(States.MOVIES_REQUEST_WAS_PARSED, moviesList));
    }

    private List<Integer> getGenres() {
        List<Integer> genres = new ArrayList();
        Context applicationContext = MainActivity.getContextOfApplication();
        SharedPreferences sharedPreferences = applicationContext.getSharedPreferences(applicationContext.getString(R.string.filter_preferences), Context.MODE_PRIVATE);
        GenresMap map = new GenresMap();
        for (String s : map.genresMap.keySet()) {
            int genreId = sharedPreferences.getInt(s,0);
            if (genreId != 0) {
                genres.add(genreId);
            }
        }
        return genres;
    }

    private String getSortTypeFromPreferences(){
        Context applicationContext = MainActivity.getContextOfApplication();
        SharedPreferences sharedPreferences = applicationContext.getSharedPreferences(applicationContext.getString(R.string.filter_preferences), Context.MODE_PRIVATE);
        String sortType = sharedPreferences.getString("Sort_type", "popularity");
        String sortValue = sharedPreferences.getString(sortType, "desc");
        String resultValue = sortType + "." + sortValue;
        return resultValue;
    }

    private String createGenresString(List<Integer> genres){
        String builtList = "";
        for (int i=0;i<genres.size();i++){
            builtList += Integer.toString(genres.get(i));
            if (i != genres.size() - 1) {
                builtList = builtList + ",";
            }
        }
        return builtList;
    }




    // Creates url for movies request
    private String createMoviesUrl() {
        String url = null;
        Uri builtUri = null;
        List<Integer> genres = getGenres();
        if (genres.size()>0) {
            builtUri = builtUriWithGenres(createGenresString(genres));
        } else {
            builtUri = builtUri();
        }

        url = builtUri.toString();
        Timber.v(url);
        return url;
    }

    private Uri builtUriWithGenres(String genres){
        Uri builtUri = Uri.parse(BASE_URL).buildUpon()
                .appendQueryParameter(LANGUAGE,LANGUAGE_VALUE)
                .appendQueryParameter(WITH_GENRES,genres)
                .appendQueryParameter(SORT_BY, getSortTypeFromPreferences())
                .appendQueryParameter(PAGE,Integer.toString(page_))
                .appendQueryParameter(API_KEY, getApiKey())
                .build();
        return builtUri;
    }

    private Uri builtUri(){
        Uri builtUri = Uri.parse(BASE_URL).buildUpon()
                .appendQueryParameter(LANGUAGE, LANGUAGE_VALUE)
                .appendQueryParameter(SORT_BY, getSortTypeFromPreferences())
                .appendQueryParameter(PAGE, Integer.toString(page_))
                .appendQueryParameter(API_KEY, getApiKey())
                .build();
        return builtUri;
    }

    // use Volley for sending request
    private void postGetRequest(final String url) {
        String tag = "movie_request";
        // Check if there is saved data in the cache
        // If so, there is no need to send same request, just take it from cache
        // Otherwise, send request
        String response = getDataFromCache(url);
        if (response != null) {
            Timber.v("Data was cashed");
            manager_.sendMessage(manager_.obtainMessage(States.MOVIES_REQUEST_COMPLETED, response));
        } else {
            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            Timber.v("Data was not cashed");
                            String responseString = response.toString();
                            manager_.sendMessage(manager_.obtainMessage(States.MOVIES_REQUEST_COMPLETED, responseString));
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            String errorMsg = error.getMessage();
                            Timber.e(error.getMessage());
                            manager_.sendMessage(manager_.obtainMessage(States.VOLLEY_REQUEST_FAILED, errorMsg));
                        }
                    });

            AppController.getInstance().addToRequestQueue(jsonObjectRequest, tag);
        }
    }

    private String getDataFromCache(String url) {
        Cache.Entry retrievedData = retrieveDataFromCache(url);
        String data = null;
        if (retrievedData != null) {
            try {
                data = new String(retrievedData.data, "UTF-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
        return data;
    }

    private Cache.Entry retrieveDataFromCache(String url) {
        Cache cache = AppController.getInstance().getRequestQueue().getCache();
        Cache.Entry entry = cache.get(url);
        return entry;
    }

    // use GSON for parsing request
    private static List<Movie> getMoviesFromJson(String response) {
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.setDateFormat("M/d/yy hh:mm a");
        Gson gson = gsonBuilder.create();

        Type listType = new TypeToken<List<Movie>>() {
        }.getType();
        List<Movie> movies = new ArrayList<Movie>();

        JsonParser parser = new JsonParser();
        JsonObject jsonObject = parser.parse(response).getAsJsonObject();
        JsonElement resultsElement = jsonObject.get("results");
        movies = (List<Movie>) gson.fromJson(resultsElement, listType);

        return movies;
    }

    // after that, when we got path of image, make request by means of Picasso for getting cover

    // Temporary method for getting api_key_value
    private String getApiKey() {
        return Constants.API_KEY_VALUE;
    }

}
