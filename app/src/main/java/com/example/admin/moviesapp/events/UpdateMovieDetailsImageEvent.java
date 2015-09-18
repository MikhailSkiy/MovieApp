package com.example.admin.moviesapp.events;

import com.example.admin.moviesapp.models.MovieDetails;

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
