package com.example.admin.moviesapp.managers;

import android.os.Message;

import com.example.admin.moviesapp.helpers.States;
import com.example.admin.moviesapp.interfaces.UpdateListener;
import com.example.admin.moviesapp.models.Movie;
import com.example.admin.moviesapp.models.MovieDetails;
import com.example.admin.moviesapp.requests.GeneralRequest;
import com.example.admin.moviesapp.requests.MovieDetailsRequest;
import com.example.admin.moviesapp.requests.MovieRequest;
import com.example.admin.moviesapp.requests.PhotoRequest;

import android.os.Handler;

import java.util.ArrayList;
import java.util.List;

import timber.log.Timber;

/**
 * Created by Mikhail Valuyskiy on 02.09.2015.
 */
public class RequestManager extends Handler {

    private static RequestManager instance = null;
    private UpdateListener updateListener_ = null;

    int size=0;
    int counter=0;

    public void init (UpdateListener listener){
        this.updateListener_ = listener;
    }

    public static RequestManager getInstance(){
        if (instance == null){
            instance = new RequestManager();
        }
        return instance;
    }

    public void handleMessage(Message message){
        MovieRequest movieRequest = new MovieRequest(getInstance());
        PhotoRequest photoRequest = new PhotoRequest(getInstance());
        MovieDetailsRequest movieDetailsRequest = new MovieDetailsRequest(getInstance());

        switch (message.what){
            case States.MOVIES_REQUEST:
                Timber.v("MOVIES_REQUEST");
                // create appropriate request
                movieRequest.postRequest();
                break;

            case States.MOVIES_REQUEST_COMPLETED:
                Timber.v("MOVIES_REQUEST_COMPLETED");
                String response = (String) message.obj;
                movieRequest.getMoviesList(response);
                break;
            
            case States.MOVIES_REQUEST_WAS_PARSED:
                Timber.v("MOVIES_REQUEST_WAS_PARSED");
                List<Movie> movieList = (List<Movie>) message.obj;
                size = movieList.size();
                for (counter=0;counter<size;counter++){
                    photoRequest.postPhotoRequest(movieList.get(counter));
                    String path = movieList.get(counter).getPosterPath();
                    Timber.v(path);
                }
                break;

            case States.IMAGE_DOWNLOADED:
                Timber.v("IMAGE_DOWNLOAD");
                List<Movie> moviesWithImages = new ArrayList<>();
                Movie movie = (Movie)message.obj;
                moviesWithImages.add(movie);
                Timber.v(Integer.toString(counter));
                Timber.v(Integer.toString(size));
                updateListener_.onUpdate(moviesWithImages);

                break;

            case States.MOVIE_DETAILS_REQUEST_COMPLETED:
                Timber.v("MOVIE_DETAILS_REQUEST_COMPLETED");
                String movieDetailsResponse = (String) message.obj;
                movieDetailsRequest.getMovieDetailsObject(movieDetailsResponse);
                break;

            case States.MOVIE_DETAILS_REQUEST_WAS_PARSED:
                Timber.v("MOVIE_DETAILS_REQUEST_WAS_PARSED");
                MovieDetails movieDetails = (MovieDetails)message.obj;
                break;


            case States.VOLLEY_REQUEST_FAILED:
                String errorMsg = (String)message.obj;
                updateListener_.onErrorRaised(errorMsg);

            default:
                break;
        }
    }
}
