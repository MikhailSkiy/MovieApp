package com.example.admin.moviesapp.models;

/**
 * Created by Mikhail Valuyskiy on 01.09.2015.
 */
public class Movie {
    private boolean adult_;
    private long id_;
    private String originalTitle_;
    private String overview_;
    private String releaseDate_;
    private String posterPath_;
    private boolean video_;
    private double voteAverage_;
    private int voteCount_;
    private byte[] cover_;

    public String getTitle() {
        return originalTitle_;
    }

    public void setTitle(String title) {
        this.originalTitle_ = title;
    }

    public byte[] getCover() {
        return cover_;
    }

    public void setCover(byte[] cover) {
        this.cover_ = cover;
    }
}
