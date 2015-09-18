package com.example.admin.moviesapp.events;

import com.example.admin.moviesapp.models.Cast;

/**
 * Created by Mikhail Valuyskiy on 18.09.2015.
 */
public class UpdateCastListEvent {
    private Cast cast_;

    public UpdateCastListEvent(Cast cast) {
        this.cast_ = cast;
    }

    public Cast getCast() {
        return cast_;
    }
}
