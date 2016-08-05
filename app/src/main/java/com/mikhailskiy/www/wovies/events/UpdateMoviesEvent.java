package com.mikhailskiy.www.wovies.events;

import com.mikhailskiy.www.wovies.models.CommonMovie;

import java.util.List;

/**
 * Created by Mikhail on 29.11.15.
 */
public class UpdateMoviesEvent {
    private List<? extends CommonMovie> movies_;

    public UpdateMoviesEvent(List<? extends CommonMovie> movies){
        this.movies_ = movies;
    }

    public List<? extends CommonMovie> getMovies(){
        return movies_;
    }
}

