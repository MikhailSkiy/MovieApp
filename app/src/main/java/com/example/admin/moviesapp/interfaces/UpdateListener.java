package com.example.admin.moviesapp.interfaces;

import com.example.admin.moviesapp.models.Cast;
import com.example.admin.moviesapp.models.CommonMovie;
import com.example.admin.moviesapp.models.Trailer;

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
