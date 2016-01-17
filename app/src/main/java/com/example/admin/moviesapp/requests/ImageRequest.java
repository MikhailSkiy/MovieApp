package com.example.admin.moviesapp.requests;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;

import com.android.volley.Cache;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.example.admin.moviesapp.helpers.States;
import com.example.admin.moviesapp.helpers.Util;
import com.example.admin.moviesapp.managers.AppController;
import com.example.admin.moviesapp.managers.RequestManager;
import com.example.admin.moviesapp.models.CommonMovie;
import com.example.admin.moviesapp.models.Movie;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import timber.log.Timber;

/**
 * Created by Mikhail Valuyskiy on 02.09.2015.
 */
public class ImageRequest {

    //region Keys for building query
    public final String BASE_PHOTO_URL = "http://image.tmdb.org/t/p/";
    public final String SIZE = "w185";
    public final String SIZE_W342 = "w342";
    //endregion

    private RequestManager manager_;

    public ImageRequest(RequestManager manager) {
        this.manager_ = manager;
    }

    public void postImageRequest(CommonMovie movie){
        postMovieImageRequest(movie);
    }


    // Creates url for movies request
    private String createMoviesUrl(String photoId) {
        String url = null;
        Uri builtUri = Uri.parse(BASE_PHOTO_URL).buildUpon()
                .appendPath(SIZE_W342)
                .appendEncodedPath(photoId)
                .build();
        url = builtUri.toString();
        Timber.v(url);
        return url;
    }

    private void postMovieImageRequest(final CommonMovie movie) {
        ImageLoader imageLoader = AppController.getInstance().getImageLoader();
        List<Movie> movieList = new ArrayList<>();
        // Create appropriate URL for getting image
        String url = createMoviesUrl(movie.getImagePath());
        if (url!=null) {
            // Take image from cache
            Bitmap photoFromCache = getPhotoFromCache(url);

            if (photoFromCache != null) {
                movie.setCover(Util.getBytesFromBitmap(photoFromCache));
                manager_.sendMessage(manager_.obtainMessage(movie.getImageStatus(), movie));

            } else {
                imageLoader.get(url, new ImageLoader.ImageListener() {
                    @Override
                    public void onResponse(ImageLoader.ImageContainer response, boolean isImmediate) {
                        if (response.getBitmap() != null) {
                            movie.setCover(Util.getBytesFromBitmap(response.getBitmap()));
                            manager_.sendMessage(manager_.obtainMessage(movie.getImageStatus(), movie));
                        }
                    }

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        manager_.sendMessage(manager_.obtainMessage(States.VOLLEY_REQUEST_FAILED, error.getMessage()));
                    }
                });

            }
        }

    }


    private Bitmap getPhotoFromCache(String url) {
        Cache.Entry retrievedData = retrieveDataFromCache(url);
        Bitmap photoFromCache = null;
        if (retrievedData != null) {
            photoFromCache = BitmapFactory.decodeByteArray(retrievedData.data, 0, retrievedData.data.length);
        }
        return photoFromCache;
    }

    private Cache.Entry retrieveDataFromCache(String url) {
        Cache cache = AppController.getInstance().getRequestQueue().getCache();
        Cache.Entry entry = cache.get(url);
        return entry;
    }

}
