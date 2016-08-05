package com.mikhailskiy.www.wovies.models;

import android.net.Uri;

import com.mikhailskiy.www.wovies.helpers.States;
import com.google.gson.annotations.SerializedName;

import timber.log.Timber;

/**
 * Created by Mikhail Valuyskiy on 01.09.2015.
 */
public class Movie extends CommonMovie {

    //region Keys for building query
    public final String BASE_PHOTO_URL = "http://image.tmdb.org/t/p/";
    public final String SIZE = "w185";
    public final String SIZE_W342 = "w342";
    //endregion

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
    private String posterUrl_;
    @SerializedName("title")
    private String title_;
    @SerializedName("video")
    private boolean video_;
    @SerializedName("vote_average")
    private double voteAverage_;
    @SerializedName("vote_count")
    private int voteCount_;

    private byte[] cover_;

    public boolean isAdult() {
        return adult_;
    }

    public void setAdult(boolean adult) {
        this.adult_ = adult;
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

    public String getTitle() {
        return title_;
    }

    public void setTitle(String title) {
        this.title_ = title;
    }

    public boolean isVideo() {
        return video_;
    }

    public void setVideo(boolean video) {
        this.video_ = video;
    }

    public double getVoteAverage() {
        return voteAverage_;
    }

    public void setVoteAverage(double voteAverage) {
        this.voteAverage_ = voteAverage;
    }

    public int getVoteCount() {
        return voteCount_;
    }

    public void setVoteCount(int voteCount) {
        this.voteCount_ = voteCount;
    }

    public String getOverview() {
        return overview_;
    }

    public void setOverview(String overview) {
        this.overview_ = overview;
    }

    public String getReleaseDate() {
        return releaseDate_;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate_ = releaseDate;
    }

    public String getPosterPath() {
        return posterUrl_;
    }

    public void setPosterPath(String posterPath) {
        this.posterUrl_ = posterPath;
    }

    public byte[] getCover() {
        return cover_;
    }

    public void setCover(byte[] cover) {
        this.cover_ = cover;
    }

    public int getImageStatus(){
        return States.IMAGE_DOWNLOADED;
    }

    public String getImagePath(){
        return getPosterPath();
    }

    public String getImageUrl(){
       return createMoviesUrl(posterUrl_);
    }

    private String createMoviesUrl(String photoId) {
        String url = null;
        Uri builtUri = Uri.parse(BASE_PHOTO_URL).buildUpon()
                .appendPath(SIZE_W342)
                .appendEncodedPath(photoId)
                .build();
        url = builtUri.toString();
        Timber.v(url);
        return url;
    }

}
