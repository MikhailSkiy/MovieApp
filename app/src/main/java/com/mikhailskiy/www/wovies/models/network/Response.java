package com.mikhailskiy.www.wovies.models.network;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Mikhail Valuyskiy on 22.10.2015.
 */

// This class represents the response
// from mark movie as favorite post request
public class Response {
    @SerializedName("status_code")
    public String statusCode;
    @SerializedName("status_message")
    public String statusMessage;

}
