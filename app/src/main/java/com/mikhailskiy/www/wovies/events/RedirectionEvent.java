package com.mikhailskiy.www.wovies.events;

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
