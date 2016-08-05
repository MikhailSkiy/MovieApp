package com.mikhailskiy.www.wovies.events;

import com.mikhailskiy.www.wovies.models.MovieDetails;

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
