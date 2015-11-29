package com.example.admin.moviesapp.models;

/**
 * Created by Mikhail on 29.11.15.
 */
public class AboutItem {
    private String title_;
    private int id_;

    public AboutItem(String title, int id){
        this.title_ = title;
        this.id_ = id;
    }

    public String getTitle() {
        return title_;
    }

    public void setTitle(String title) {
        this.title_ = title;
    }

    public int getId() {
        return id_;
    }

    public void setId_(int id) {
        this.id_ = id;
    }
}
