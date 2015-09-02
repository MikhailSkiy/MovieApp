package com.example.admin.moviesapp.models;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Mikhail Valuyskiy on 01.09.2015.
 */
public class Movie extends Result {

    @SerializedName("adult")
    private boolean adult_;
    @SerializedName("id")
    private long id_;
    @SerializedName("original_title")
    private String originalTitle_;
    @SerializedName("overview")
    private String overview_;
    @SerializedName("release_date")
    private String releaseDate_;
    @SerializedName("poster_path")
    private String posterPath_;
    @SerializedName("video")
    private boolean video_;
    @SerializedName("vote_average")
    private double voteAverage_;
    @SerializedName("vote_count")
    private int voteCount_;

    private byte[] cover_;

    public String getTitle() {
        return originalTitle_;
    }

    public void setTitle(String title) {
        this.originalTitle_ = title;
    }

    public String getPosterPath() {
        return posterPath_;
    }

    public void setPosterPath(String posterPath) {
        this.posterPath_ = posterPath;
    }

    public byte[] getCover() {
        return cover_;
    }

    public void setCover(byte[] cover) {
        this.cover_ = cover;
    }

}
