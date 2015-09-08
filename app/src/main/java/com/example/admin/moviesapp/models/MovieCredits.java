package com.example.admin.moviesapp.models;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Mikhail Valuyskiy on 08.09.2015.
 */
public class MovieCredits {
    @SerializedName("adult")
    private boolean adult_;
    @SerializedName("character")
    private String character_;
    @SerializedName("credit_id")
    private String creditId_;
    @SerializedName("id")
    private long id_;
    @SerializedName("original_title")
    private String originalTitle_;
    @SerializedName("poster_path")
    private String posterPath_;
    @SerializedName("release_date")
    private String releaseDate_;
    @SerializedName("title")
    private String title_;

    public boolean isAdult() {
        return adult_;
    }

    public void setAdult(boolean adult) {
        this.adult_ = adult;
    }

    public String getCharacter() {
        return character_;
    }

    public void setCharacter(String character) {
        this.character_ = character;
    }

    public String getCreditId() {
        return creditId_;
    }

    public void setCreditId(String creditId) {
        this.creditId_ = creditId;
    }

    public long getId() {
        return id_;
    }

    public void setId(long id) {
        this.id_ = id;
    }

    public String getOriginalTitle() {
        return originalTitle_;
    }

    public void setOriginalTitle(String originalTitle) {
        this.originalTitle_ = originalTitle;
    }

    public String getPosterPath() {
        return posterPath_;
    }

    public void setPosterPath(String posterPath) {
        this.posterPath_ = posterPath;
    }

    public String getReleaseDate() {
        return releaseDate_;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate_ = releaseDate;
    }

    public String getTitle() {
        return title_;
    }

    public void setTitle(String title) {
        this.title_ = title;
    }
}
