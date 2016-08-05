package com.mikhailskiy.www.wovies.events;

import com.mikhailskiy.www.wovies.models.MovieCredits;

/**
 * Created by Mikhail Valuyskiy on 09.09.2015.
 */
public class UpdateMovieCreditsListEvent {

    public void setMovieCredits_(MovieCredits movieCredits_) {
        this.movieCredits_ = movieCredits_;
    }

    private MovieCredits movieCredits_;

    public UpdateMovieCreditsListEvent(MovieCredits movieCredits) {
        this.movieCredits_ = movieCredits;
    }

    public MovieCredits getMovieCredits() {
        return movieCredits_;
    }

}
