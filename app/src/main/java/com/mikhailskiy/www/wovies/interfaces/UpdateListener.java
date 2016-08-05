package com.mikhailskiy.www.wovies.interfaces;

import com.mikhailskiy.www.wovies.models.CommonMovie;
import com.mikhailskiy.www.wovies.models.Trailer;

import java.util.List;

/**
 * Created by Mikhail Valuyskiy on 02.09.2015.
 */
public interface UpdateListener {
    public void onUpdate(List<? extends CommonMovie> resultList);
    public void UpdateTrailers(List<Trailer> trailersList);
    public void UpdateCasts(List<? extends CommonMovie> casts);
    public void onErrorRaised(String errorMsg);
}
