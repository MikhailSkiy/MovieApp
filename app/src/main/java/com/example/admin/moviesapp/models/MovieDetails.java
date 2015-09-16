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


    public boolean isAdult() {
        return adult_;
    }

    public void setAdult(boolean adult) {
        this.adult_ = adult;
    }

    public String getBackdropPath() {
        return backdropPath_;
    }

    public void setBackdropPath(String backdropPath) {
        this.backdropPath_ = backdropPath;
    }

    public long getBudget() {
        return budget_;
    }

    public void setBudget(long budget) {
        this.budget_ = budget;
    }

    public List<Genre> getGenres() {
        return genres_;
    }

    public void setGenres(List<Genre> genres) {
        this.genres_ = genres;
    }

    public String getHomepage() {
        return homepage_;
    }

    public void setHomepage(String homepage) {
        this.homepage_ = homepage;
    }

    public long getId() {
        return id_;
    }

    public void setId(long id) {
        this.id_ = id;
    }

    public String getImdbId() {
        return imdbId_;
    }

    public void setImdbId(String imdbId) {
        this.imdbId_ = imdbId;
    }

    public String getOriginalLanguage() {
        return originalLanguage_;
    }

    public void setOriginalLanguage(String originalLanguage) {
        this.originalLanguage_ = originalLanguage;
    }

    public String getOriginalTitle() {
        return originalTitle_;
    }

    public void setOriginalTitle(String originalTitle) {
        this.originalTitle_ = originalTitle;
    }

    public String getOverview() {
        return overview_;
    }

    public void setOverview(String overview) {
        this.overview_ = overview;
    }

    public double getPopularity() {
        return popularity_;
    }

    public void setPopularity(double popularity) {
        this.popularity_ = popularity;
    }

    public String getPosterPath() {
        return posterPath_;
    }

    public void setPosterPath(String posterPath) {
        this.posterPath_ = posterPath;
    }

    public List<ProductionCompany> getCompanies() {
        return companies_;
    }

    public void setCompanies(List<ProductionCompany> companies) {
        this.companies_ = companies;
    }

    public List<ProductionCountry> getCountries() {
        return countries_;
    }

    public void setCountries(List<ProductionCountry> countries) {
        this.countries_ = countries;
    }

    public String getReleaseDate() {
        return releaseDate_;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate_ = releaseDate;
    }

    public long getRevenue() {
        return revenue_;
    }

    public void setRevenue(long revenue) {
        this.revenue_ = revenue;
    }

    public int getRuntime() {
        return runtime_;
    }

    public void setRuntime(int runtime) {
        this.runtime_ = runtime;
    }

    public String getStatus() {
        return status_;
    }

    public void setStatus(String status) {
        this.status_ = status;
    }

    public String getTagline() {
        return tagline_;
    }

    public void setTagline(String tagline) {
        this.tagline_ = tagline;
    }

    public String getTitle() {
        return title_;
    }

    public void setTitle(String title) {
        this.title_ = title;
    }

    public int getImageStatus(){
        return States.COVER_DOWNLOADED;
    }

    public String getImagePath(){
      return getBackdropPath();
    }

    public double getVoteAverage() {
        return voteAverage_;
    }

    public void setVoteAverage(double voteAverage) {
        this.voteAverage_ = voteAverage;
    }

    public long getVoteCount() {
        return voteCount_;
    }

    public void setVoteCount(long voteCount) {
        this.voteCount_ = voteCount;
    }


    public byte[] getCover() {
        return cover_;
    }

    public void setCover(byte[] cover) {
        this.cover_ = cover;
    }



}
