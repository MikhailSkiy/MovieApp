package com.mikhailskiy.www.wovies.events;

import com.mikhailskiy.www.wovies.models.Cast;

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
