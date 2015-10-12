package com.example.admin.moviesapp.managers;

import android.os.Handler;
import android.os.Message;

import com.example.admin.moviesapp.events.RedirectionEvent;
import com.example.admin.moviesapp.events.UpdateCastDetailsImageEvent;
import com.example.admin.moviesapp.events.UpdateCastDetailsUI;
import com.example.admin.moviesapp.events.UpdateCastListEvent;
import com.example.admin.moviesapp.events.UpdateMovieCreditsListEvent;
import com.example.admin.moviesapp.events.UpdateMovieDescriptionUI;
import com.example.admin.moviesapp.events.UpdateMovieDetailsImageEvent;
import com.example.admin.moviesapp.events.UpdateMovieTrailersUI;
import com.example.admin.moviesapp.helpers.States;
import com.example.admin.moviesapp.interfaces.UpdateListener;
import com.example.admin.moviesapp.models.Cast;
import com.example.admin.moviesapp.models.CastDetails;
import com.example.admin.moviesapp.models.CommonMovie;
import com.example.admin.moviesapp.models.Movie;
import com.example.admin.moviesapp.models.MovieCredits;
import com.example.admin.moviesapp.models.MovieDetails;
import com.example.admin.moviesapp.models.Trailer;
import com.example.admin.moviesapp.requests.AuthRequest;
import com.example.admin.moviesapp.requests.CastDetailsRequest;
import com.example.admin.moviesapp.requests.CastsRequest;
import com.example.admin.moviesapp.requests.ImageRequest;
import com.example.admin.moviesapp.requests.MovieCreditsRequest;
import com.example.admin.moviesapp.requests.MovieDetailsRequest;
import com.example.admin.moviesapp.requests.MovieRequest;
import com.example.admin.moviesapp.requests.TrailerRequest;

import java.util.ArrayList;
import java.util.List;

import de.greenrobot.event.EventBus;
import timber.log.Timber;

/**
 * Created by Mikhail Valuyskiy on 02.09.2015.
 */
public class RequestManager extends Handler {

    private static RequestManager instance = null;
    private UpdateListener updateListener_ = null;

    int size = 0;
    int counter = 0;

    int castSize = 0;
    int castCounter = 0;

    public void init(UpdateListener listener) {
        this.updateListener_ = listener;
    }

    public static RequestManager getInstance() {
        if (instance == null) {
            instance = new RequestManager();
        }
        return instance;
    }

    public void handleMessage(Message message) {


        ImageRequest imageRequest = new ImageRequest(getInstance());

        switch (message.what) {
            case States.MOVIES_REQUEST:
                Timber.v("MOVIES_REQUEST");
                // create appropriate request
                int page = (int) message.obj;
                MovieRequest movieRequest = new MovieRequest(getInstance());
                movieRequest.setPage(page);
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
                for (counter = 0; counter < size; counter++) {
                    imageRequest.postImageRequest(movieList.get(counter));
                    String path = movieList.get(counter).getPosterPath();
                    Timber.v(path);
                }
                break;

            case States.IMAGE_DOWNLOADED:
                Timber.v("IMAGE_DOWNLOAD");
                List<CommonMovie> moviesWithImages = new ArrayList<>();
                Movie movie = (Movie) message.obj;
                moviesWithImages.add(movie);
                Timber.v(Integer.toString(counter));
                Timber.v(Integer.toString(size));
                updateListener_.onUpdate(moviesWithImages);
                break;

            case States.MOVIES_DETAILS_REQUEST:
                Timber.v("MOVIES_DETAILS_REQUEST");
                long id = (long) message.obj;
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
                MovieDetails movieDetails = (MovieDetails) message.obj;
                imageRequest.postImageRequest(movieDetails);
                break;

            case States.COVER_DOWNLOADED:
                Timber.v("COVER_DOWNLOADED");
                List<CommonMovie> moviesWithCover = new ArrayList<>();
                MovieDetails movieDetailsWithCover = (MovieDetails) message.obj;
                moviesWithCover.add(movieDetailsWithCover);
                EventBus.getDefault().post(new UpdateMovieDescriptionUI(movieDetailsWithCover));
                EventBus.getDefault().post(new UpdateMovieDetailsImageEvent(movieDetailsWithCover));
                //  updateListener_.onUpdate(moviesWithCover);

                break;

            //region Trailers requests
            case States.TRAILERS_REQUEST:
                Timber.v("TRAILERS_REQUEST");
                long movieId = (long) message.obj;
                //create trailer request
                TrailerRequest trailerRequest = new TrailerRequest(getInstance());
                trailerRequest.postRequest(movieId);
                break;

            case States.TRAILERS_REQUEST_COMPLETED:
                Timber.v("TRAILERS_REQUEST_COMPLETED");
                String trailerServerResponse = (String) message.obj;
                TrailerRequest.getTrailersList(trailerServerResponse);
                break;

            case States.TRAILERS_REQUEST_WAS_PARSED:
                Timber.v("TRAILERS_REQUEST_WAS_PARSED");
                List<Trailer> trailersList = (List<Trailer>) message.obj;
                // update fiil list of trailers with links and names
                for (Trailer t : trailersList) {
                    EventBus.getDefault().post(new UpdateMovieTrailersUI(t));
                }
                break;

            //endregion

            //region Casts request
            case States.CASTS_REQUEST:
                Timber.v("CASTS_REQUEST");
                long movieCastId = (long) message.obj;
                // create casts request
                CastsRequest castsRequest = new CastsRequest(getInstance());
                castsRequest.postRequest(movieCastId);
                break;

            case States.CASTS_REQUEST_COMPLETED:
                Timber.v("CASTS_REQUEST_COMPLETED");
                String castServerResponse = (String) message.obj;
                CastsRequest.getCastsList(castServerResponse);
                break;

            case States.CASTS_REQUEST_WAS_PARSED:
                List<Cast> castList = (List<Cast>) message.obj;
                castSize = castList.size();
                for (castCounter = 0; castCounter < castSize; castCounter++) {
                    imageRequest.postImageRequest(castList.get(castCounter));
                    String path = castList.get(castCounter).getProfilePath();
                    Timber.v(path);
                }
                break;

            case States.CAST_PROFILE_DOWNLOADED:
                Timber.v("CAST_PROFILE_DOWNLOADED");
                List<CommonMovie> castsWithImages = new ArrayList<>();
                Cast cast = (Cast) message.obj;
                castsWithImages.add(cast);
                Timber.v(Integer.toString(castCounter));
                Timber.v(Integer.toString(castSize));
                EventBus.getDefault().post(new UpdateCastListEvent(cast));
                // updateListener_.UpdateCasts(castsWithImages);
                break;
            //endregion

            //region CastDetails

            case States.CAST_DETAILS_REQUEST:
                Timber.v("CAST_DETAILS_REQUEST");
                String casrId = (String) message.obj;
                CastDetailsRequest newCastDetReq = new CastDetailsRequest(getInstance());
                newCastDetReq.postRequest(casrId);
                break;

            case States.CAST_DETAILS_REQUEST_COMPLETED:
                Timber.v("CAST_DETAILS_REQUEST_COMPLETED");
                CastDetails castDetails = (CastDetails) message.obj;

                // Send through EventBus updateUI event
                EventBus.getDefault().post(new UpdateCastDetailsUI(castDetails));
                imageRequest.postImageRequest(castDetails);
                break;

            case States.CAST_DETAILS_IMAGE_DOWNLOADED:
                Timber.v("CAST_DETAILS_IMAGE_DOWNLOADED");
                CastDetails castDetailsWithImage = (CastDetails) message.obj;
                EventBus.getDefault().post(new UpdateCastDetailsImageEvent(castDetailsWithImage.getCover()));
                break;

            //endregion

            //region Movie Credits Request
            case States.MOVIE_CREDITS_REQUEST:
                Timber.v("MOVIE_CREDITS_REQUEST");
                String movieCreditId = (String) message.obj;
                MovieCreditsRequest creditsRequest = new MovieCreditsRequest(getInstance());
                creditsRequest.postRequest(movieCreditId);
                break;

            case States.MOVIE_CREDITS_REQUEST_COMPLETED:
                Timber.v("MOVIE_CREDITS_REQUEST_COMPLETED");
                List<MovieCredits> movieCreditsList = (List<MovieCredits>) message.obj;
                for (int i = 0; i < movieCreditsList.size(); i++) {
                    imageRequest.postImageRequest(movieCreditsList.get(i));
                }
                break;

            case States.MOVIE_CREDITS_IMAGE_DOWNLOADED:
                Timber.v("MOVIE_CREDITS_IMAGE_DOWNLOADED");
                MovieCredits returnedMovieCredits = (MovieCredits) message.obj;
                EventBus.getDefault().post(new UpdateMovieCreditsListEvent(returnedMovieCredits));
                break;
            //endregion

            //region Authentication requests
            case States.LOGIN_REQUEST:
                Timber.v("LOGIN_REQUEST");
                AuthRequest authRequest = new AuthRequest(getInstance());
                authRequest.getRequestToken();
                break;

            case States.TOKEN_REQUEST_RECEIVED:
                Timber.v("TOKEN_REQUEST_RECEIVED");
                String requestToken = (String)message.obj;
                String redirectionUrl = AuthRequest.getRedirectionUrl(requestToken);
                EventBus.getDefault().post(new RedirectionEvent(redirectionUrl));
                break;

            case States.SESSION_ID_REQUEST:
                Timber.v("SESSION_ID_REQUEST");
                AuthRequest.sendSessionIdRequest();
                break;

            case States.SESSION_ID_RECEIVED:
                Timber.v("SESSION_ID_RECEIVED");
                // Now user can send request for watchlist
                // And for list of favorites movies

                break;
                //endregion


            case States.VOLLEY_REQUEST_FAILED:
                String errorMsg = (String) message.obj;
                updateListener_.onErrorRaised(errorMsg);
                break;

            default:
                break;
        }
    }
}
