package com.example.admin.moviesapp.models;

import android.graphics.drawable.Drawable;

/**
 * Created by Mikhail on 29.11.15.
 */
public class AboutItem {
    private String title_;
    public int id;
    private Drawable image_id;

    public AboutItem(int id, String title, Drawable image_id){
        this.id = id;
        this.title_ = title;
        this.image_id= image_id;
    }

    public String getTitle() {
        return title_;
    }

    public void setTitle(String title) {
        this.title_ = title;
    }

    public Drawable getId() {
        return image_id;
    }

    public void setId_(Drawable image_id) {
        this.image_id = image_id;
    }
}
