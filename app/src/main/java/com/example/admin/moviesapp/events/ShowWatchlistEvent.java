package com.example.admin.moviesapp.events;

import com.example.admin.moviesapp.models.Movie;

import java.util.List;

/**
 * Created by Mikhail Valuyskiy on 14.10.2015.
 */

/**
 * Triggers updating of movie list in Main Activity.
 */
public class ShowWatchlistEvent {
    private List<Movie> movies_;

    public ShowWatchlistEvent(List<Movie> movies) {
        this.movies_ = movies;
    }

    public List<Movie> getMovies() {
        return movies_;
    }
}
