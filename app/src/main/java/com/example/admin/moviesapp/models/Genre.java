package com.example.admin.moviesapp.models;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Mikhail Valuyskiy on 02.09.2015.
 */
public class Genre {
    @SerializedName("id")
    private int genreId_;
    @SerializedName("name")
    private String genreName_;

    public String getGenreName() {
        return genreName_;
    }

    public void setGenreName(String genreName) {
        this.genreName_ = genreName;
    }

    public int getGenreId() {
        return genreId_;
    }

    public void setGenreId(int genreId) {
        this.genreId_ = genreId;
    }
}
