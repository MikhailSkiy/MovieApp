package com.mikhailskiy.www.wovies.events;

import com.mikhailskiy.www.wovies.models.MovieDetails;

/**
 * Created by Mikhail on 18.09.2015.
 */
public class UpdateMovieDetailsImageEvent {
    private MovieDetails movieDetails_;

    public UpdateMovieDetailsImageEvent(MovieDetails movie) {
        movieDetails_ = movie;
    }

    public MovieDetails getMovieDetails() {
        return movieDetails_;
    }
}
