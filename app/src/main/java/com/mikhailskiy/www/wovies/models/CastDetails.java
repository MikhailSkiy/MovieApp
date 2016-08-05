package com.mikhailskiy.www.wovies.models;

import com.mikhailskiy.www.wovies.helpers.States;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Mikhail Valuyskiy on 08.09.2015.
 */
public class CastDetails extends CommonMovie {
    @SerializedName("adult")
    private boolean adult_;
    @SerializedName("also_known_as")
    private List<String> alsoKnownAs_;
    @SerializedName("biography")
    private String biography_;
    @SerializedName("birthday")
    private String birthday_;
    @SerializedName("deathday")
    private String deathday_;
    @SerializedName("homepage")
    private String homepage_;
    @SerializedName("id")
    private long id_;
    @SerializedName("imdb_id")
    private String imdbId_;
    @SerializedName("name")
    private String name_;
    @SerializedName("place_of_birth")
    private String placeOfBirth_;
    @SerializedName("popularity")
    private double popularity_;
    @SerializedName("profile_path")
    private String profilePath_;

    private byte[] castProfileImage_;

    public boolean isAdult() {
        return adult_;
    }

    public void setAdult(boolean adult) {
        this.adult_ = adult;
    }

    public List<String> getAlsoKnownAs() {
        return alsoKnownAs_;
    }

    public void setAlsoKnownAs(List<String> alsoKnownAs) {
        this.alsoKnownAs_ = alsoKnownAs;
    }

    public String getBiography() {
        return biography_;
    }

    public void setBiography(String biography) {
        this.biography_ = biography;
    }

    public String getBirthday() {
        return birthday_;
    }

    public void setBirthday(String birthday) {
        this.birthday_ = birthday;
    }

    public String getDeathday() {
        return deathday_;
    }

    public void setDeathday(String deathday) {
        this.deathday_ = deathday;
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

    public String getName() {
        return name_;
    }

    public void setName(String name) {
        this.name_ = name;
    }

    public String getPlaceOfBirth() {
        return placeOfBirth_;
    }

    public void setPlaceOfBirth(String placeOfBirth) {
        this.placeOfBirth_ = placeOfBirth;
    }

    public double getPopularity() {
        return popularity_;
    }

    public void setPopularity(double popularity) {
        this.popularity_ = popularity;
    }

    public String getProfilePath() {
        return profilePath_;
    }

    public void setProfilePath(String profilePath) {
        this.profilePath_ = profilePath;
    }

    public String getImagePath(){
        return getProfilePath();
    }

    public void setCover(byte[] cover){
        this.castProfileImage_ = cover;
    }

    public byte[] getCover(){
        return this.castProfileImage_;
    }

    public int getImageStatus(){
        return States.CAST_DETAILS_IMAGE_DOWNLOADED;
    }
}
