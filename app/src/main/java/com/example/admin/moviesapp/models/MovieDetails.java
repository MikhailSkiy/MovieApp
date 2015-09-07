package com.example.admin.moviesapp.models;

import com.example.admin.moviesapp.helpers.States;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Mikhail on 02.09.2015.
 */
public class MovieDetails extends CommonMovie {

    @SerializedName("adult")
    private boolean adult_;

    @SerializedName("backdrop_path")
    private String backdropPath_;

    @SerializedName("budget")
    private long budget_;

    @SerializedName("genres")
    private List<Genre> genres_;

    @SerializedName("homepage")
    private String homepage_;

    @SerializedName("id")
    private long id_;

    @SerializedName("imdb_id")
    private String imdbId_;

    @SerializedName("original_language")
    private String originalLanguage_;

    @SerializedName("original_title")
    private String originalTitle_;

    @SerializedName("overview")
    private String overview_;

    @SerializedName("popularity")
    private double popularity_;

    @SerializedName("poster_path")
    private String posterPath_;

    @SerializedName("production_companies")
    private List<ProductionCompany> companies_;

    @SerializedName("production_countries")
    private List<ProductionCountry> countries_;

    @SerializedName("release_date")
    private String releaseDate_;

    @SerializedName("revenue")
    private long revenue_;

    @SerializedName("runtime")
    private int runtime_;

    @SerializedName("spoken_languages")
    private List<SpokenLanguages> languages_;

    @SerializedName("status")
    private String status_;

    @SerializedName("tagline")
    private String tagline_;

    @SerializedName("title")
    private String title_;

    @SerializedName("video")
    private boolean video_;

    @SerializedName("vote_average")
    private double voteAverage_;

    @SerializedName("vote_count")
    private long voteCount_;

    private byte[] cover_;

    public String getOriginalTitle() {
        return originalTitle_;
    }

    public void setOriginalTitle(String originalTitle) {
        this.originalTitle_ = originalTitle;
    }

    public List<ProductionCompany> getCompanies() {
        return companies_;
    }

    public void setCompanies(List<ProductionCompany> companies) {
        this.companies_ = companies;
    }

    public String getBackdropPath() {
        return backdropPath_;
    }

    public void setBackdropPath(String backdropPath) {
        this.backdropPath_ = backdropPath;
    }

    public byte[] getCover() {
        return cover_;
    }

    public void setCover(byte[] cover) {
        this.cover_ = cover;
    }

    public String getOverview() {
        return overview_;
    }

    public void setOverview(String overview) {
        this.overview_ = overview;
    }

    public int getImageStatus(){
        return States.COVER_DOWNLOADED;
    }

    public String getImagePath(){
      return getBackdropPath();
    }

    public int getRuntime() {
        return runtime_;
    }

    public void setRuntime(int runtime) {
        this.runtime_ = runtime;
    }

    public String getOriginalLanguage() {
        return originalLanguage_;
    }

    public void setOriginalLanguage(String originalLanguage) {
        this.originalLanguage_ = originalLanguage;
    }

    public List<Genre> getGenres() {
        return genres_;
    }

    public void setGenres(List<Genre> genres) {
        this.genres_ = genres;
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
