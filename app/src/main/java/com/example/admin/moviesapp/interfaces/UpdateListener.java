package com.example.admin.moviesapp.interfaces;

import com.example.admin.moviesapp.models.CommonMovie;

import java.util.List;

/**
 * Created by Mikhail Valuyskiy on 02.09.2015.
 */
public interface UpdateListener {
    public void onUpdate(List<? extends CommonMovie> resultList);
    public void onErrorRaised(String errorMsg);
}
