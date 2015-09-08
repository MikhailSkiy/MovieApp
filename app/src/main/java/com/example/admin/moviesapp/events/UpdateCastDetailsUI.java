package com.example.admin.moviesapp.events;

import com.example.admin.moviesapp.models.CastDetails;

/**
 * Created by Mikhail Valuyskiy on 08.09.2015.
 */
public class UpdateCastDetailsUI {

    private CastDetails castDetails_;

    public UpdateCastDetailsUI(CastDetails castDetails){
        this.castDetails_ = castDetails;
    }

    public CastDetails getCastDetails(){
        return this.castDetails_;
    }
}
