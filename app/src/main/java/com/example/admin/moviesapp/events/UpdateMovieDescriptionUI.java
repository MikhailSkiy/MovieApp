package com.example.admin.moviesapp.events;

import com.example.admin.moviesapp.models.MovieDetails;

/**
 * Created by Mikhail on 18.09.2015.
 */
public class UpdateMovieDescriptionUI {
    private MovieDetails movieDetails_;

    public UpdateMovieDescriptionUI(MovieDetails movieDetails) {
        this.movieDetails_ = movieDetails;
    }

    public MovieDetails getMovieDescription() {
        return this.movieDetails_;
    }
}
