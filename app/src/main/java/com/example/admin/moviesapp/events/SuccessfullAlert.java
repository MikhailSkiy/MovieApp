package com.example.admin.moviesapp.events;

import com.example.admin.moviesapp.models.network.Response;

/**
 * Created by Mikhail Valuyskiy on 22.10.2015.
 */
public class SuccessfullAlert {

    private Response response_;

    public SuccessfullAlert(Response listOperationResponse ){
        response_ = listOperationResponse;
    }

    public String getAlertText(){
        return response_.statusMessage;
    }

    public String getAlertStatus(){
        return response_.statusCode;
    }
}
