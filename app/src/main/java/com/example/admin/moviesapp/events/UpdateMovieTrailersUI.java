package com.example.admin.moviesapp.events;

import com.example.admin.moviesapp.models.Trailer;

/**
 * Created by Mikhail on 17.09.2015.
 */
public class UpdateMovieTrailersUI {

    private Trailer trailer_;

    public void setMovieTrailer(Trailer trailer){
        this.trailer_ = trailer;
    }

    public Trailer getMovieTrailer(){
        return trailer_;
    }

    public UpdateMovieTrailersUI(Trailer trailer){
        this.trailer_ = trailer;
    }
}
