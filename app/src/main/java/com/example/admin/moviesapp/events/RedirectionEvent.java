package com.example.admin.moviesapp.events;

import com.example.admin.moviesapp.models.CastDetails;

/**
 * Created by Mikhail Valuyskiy on 12.10.2015.
 */
public class RedirectionEvent {

    private String url;

    public RedirectionEvent(String redirectUrl) {
        this.url = redirectUrl;
    }

    public String getUrl() {
        return this.url;
    }
}
