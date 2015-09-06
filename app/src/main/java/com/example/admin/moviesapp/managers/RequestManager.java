package com.example.admin.moviesapp.managers;

import android.os.Handler;
import android.os.Message;

import com.example.admin.moviesapp.helpers.States;
import com.example.admin.moviesapp.interfaces.UpdateListener;
import com.example.admin.moviesapp.models.CommonMovie;
import com.example.admin.moviesapp.models.Movie;
import com.example.admin.moviesapp.models.MovieDetails;
import com.example.admin.moviesapp.models.Trailer;
import com.example.admin.moviesapp.requests.ImageRequest;
import com.example.admin.moviesapp.requests.MovieDetailsRequest;
import com.example.admin.moviesapp.requests.MovieRequest;
import com.example.admin.moviesapp.requests.TrailerRequest;

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


        ImageRequest imageRequest = new ImageRequest(getInstance());

        switch (message.what){
            case States.MOVIES_REQUEST:
                Timber.v("MOVIES_REQUEST");
                // create appropriate request
                MovieRequest movieRequest = new MovieRequest(getInstance());
                movieRequest.postRequest(0);
                break;

            case States.MOVIES_REQUEST_COMPLETED:
                Timber.v("MOVIES_REQUEST_COMPLETED");
                String response = (String) message.obj;
                MovieRequest.getMovieObjects(response);
                break;
            
            case States.MOVIES_REQUEST_WAS_PARSED:
                Timber.v("MOVIES_REQUEST_WAS_PARSED");
                List<Movie> movieList = (List<Movie>) message.obj;
                size = movieList.size();
                for (counter=0;counter<size;counter++){
                    imageRequest.postImageRequest(movieList.get(counter));
                    String path = movieList.get(counter).getPosterPath();
                    Timber.v(path);
                }
                break;

            case States.IMAGE_DOWNLOADED:
                Timber.v("IMAGE_DOWNLOAD");
                List<CommonMovie> moviesWithImages = new ArrayList<>();
                Movie movie = (Movie)message.obj;
                moviesWithImages.add(movie);
                Timber.v(Integer.toString(counter));
                Timber.v(Integer.toString(size));
                updateListener_.onUpdate(moviesWithImages);
                break;

            case States.MOVIES_DETAILS_REQUEST:
                Timber.v("MOVIES_DETAILS_REQUEST");
                long id = (long)message.obj;
                // create appropriate request
                MovieDetailsRequest movieDetailsRequest = new MovieDetailsRequest(getInstance());
                movieDetailsRequest.postRequest(id);
                break;

            case States.MOVIE_DETAILS_REQUEST_COMPLETED:
                Timber.v("MOVIE_DETAILS_REQUEST_COMPLETED");
                String movieDetailsResponse = (String) message.obj;
                MovieDetailsRequest.getMovieObjects(movieDetailsResponse);
                break;

            case States.MOVIE_DETAILS_REQUEST_WAS_PARSED:
                Timber.v("MOVIE_DETAILS_REQUEST_WAS_PARSED");
                MovieDetails movieDetails = (MovieDetails)message.obj;
                imageRequest.postImageRequest(movieDetails);
                break;

            case States.COVER_DOWNLOADED:
                Timber.v("COVER_DOWNLOADED");
                List<CommonMovie> moviesWithCover = new ArrayList<>();
                MovieDetails movieDetailsWithCover = (MovieDetails)message.obj;
                moviesWithCover.add(movieDetailsWithCover);
                updateListener_.onUpdate(moviesWithCover);

                break;

            //region Trailers requests
            case States.TRAILERS_REQUEST:
                Timber.v("TRAILERS_REQUEST");
                long movieId = (long)message.obj;
                //create trailer request
                TrailerRequest trailerRequest = new TrailerRequest(getInstance());
                trailerRequest.postRequest(movieId);
                break;

            case States.TRAILERS_REQUEST_COMPLETED:
                Timber.v("TRAILERS_REQUEST_COMPLETED");
                String trailerServerResponse = (String)message.obj;
                TrailerRequest.getTrailersList(trailerServerResponse);
                break;

            case States.TRAILERS_REQUEST_WAS_PARSED:
                Timber.v("TRAILERS_REQUEST_WAS_PARSED");
                List<Trailer> trailersList = (List<Trailer>)message.obj;
                // update fiil list of trailers with links and names
                updateListener_.UpdateTrailers(trailersList);
                break;

            //endregion



            case States.VOLLEY_REQUEST_FAILED:
                String errorMsg = (String)message.obj;
                updateListener_.onErrorRaised(errorMsg);

            default:
                break;
        }
    }
}
