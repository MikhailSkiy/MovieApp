package com.example.admin.moviesapp.models;

import android.database.Cursor;

import com.example.admin.moviesapp.helpers.States;
import com.google.gson.annotations.SerializedName;

import java.util.List;

import static com.example.admin.moviesapp.database.Contract.MoviesEntry;

/**
 * Created by Mikhail Valuyskiy on 01.09.2015.
 */
public class Movie extends CommonMovie {

    @SerializedName("adult")
    private boolean adult_;
    @SerializedName("id")
    private long id_;
    @SerializedName("genre_ids")
    private List<Integer> genres_;
    @SerializedName("original_title")
    private String originalTitle_;
    @SerializedName("overview")
    private String overview_;
    @SerializedName("release_date")
    private String releaseDate_;
    @SerializedName("poster_path")
    private String posterPath_;
    @SerializedName("popularity")
    private double popularity_;
    @SerializedName("title")
    private String title_;
    @SerializedName("video")
    private boolean video_;
    @SerializedName("vote_average")
    private double voteAverage_;
    @SerializedName("vote_count")
    private int voteCount_;

    private byte[] cover_;

    public static Movie getMovieFromCursor(Cursor cursor) {
        Movie movie = new Movie();
        long id = cursor.getLong(cursor.getColumnIndex(MoviesEntry._ID));
        boolean isAdult = cursor.getInt(cursor.getColumnIndex(MoviesEntry.COLUMN__ADULT)) == 1 ? true : false;
        String originalTitle = cursor.getString(cursor.getColumnIndex(MoviesEntry.COLUMN_ORIGINAL_TITLE));
        String overview = cursor.getString(cursor.getColumnIndex(MoviesEntry.COLUMN_OVERVIEW));
        String releaseDate = cursor.getString(cursor.getColumnIndex(MoviesEntry.COLUMN_RELEASE_DATE));
        String posterPath = cursor.getString(cursor.getColumnIndex(MoviesEntry.COLUMN_POSTER_PATH));
        String title = cursor.getString(cursor.getColumnIndex(MoviesEntry.COLUMN_TITLE));
        boolean video = cursor.getInt(cursor.getColumnIndex(MoviesEntry.COLUMN_VIDEO))==1 ? true : false;
        double voteAverage = cursor.getDouble(cursor.getColumnIndex(MoviesEntry.COLUMN_VOTE_AVERAGE));
        int voteCount = cursor.getInt(cursor.getColumnIndex(MoviesEntry.COLUMN_VOTE_COUNT));
        byte [] cover = cursor.getBlob(cursor.getColumnIndex(MoviesEntry.COLUMN_COVER));
        // Set values into movie object
        movie.setId(id);
        movie.setAdult(isAdult);
        movie.setOriginalTitle(originalTitle);
        movie.setOverview(overview);
        movie.setReleaseDate(releaseDate);
        movie.setPosterPath(posterPath);
        movie.setTitle(title);
        movie.setVideo(video);
        movie.setVoteAverage(voteAverage);
        movie.setVoteCount(voteCount);
        movie.setCover(cover);

        return movie;
    }

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
        return posterPath_;
    }

    public void setPosterPath(String posterPath) {
        this.posterPath_ = posterPath;
    }

    public double getPopularity() {
        return popularity_;
    }

    public void setPopularity(double popularity) {
        this.popularity_ = popularity;
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


    public List<Integer> getGenres() {
        return genres_;
    }

    public void setGenres(List<Integer> genres) {
        this.genres_ = genres;
    }
}
