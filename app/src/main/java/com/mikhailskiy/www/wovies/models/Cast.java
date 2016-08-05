package com.mikhailskiy.www.wovies.models;

import com.mikhailskiy.www.wovies.helpers.States;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Mikhail Valuyskiy on 07.09.2015.
 */
public class Cast extends CommonMovie{
    @SerializedName("cast_id")
    private long castId_;
    @SerializedName("character")
    private String character_;
    @SerializedName("credit_id")
    private String creditId_;
    @SerializedName("id")
    private long id_;
    @SerializedName("name")
    private String name_;
    @SerializedName("order")
    private int order_;
    @SerializedName("profile_path")
    private String profilePath_;

    private byte[] cover_;

    public long getCastId() {
        return castId_;
    }

    public void setCastId(long castId) {
        this.castId_ = castId;
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

    public String getName() {
        return name_;
    }

    public void setName(String name) {
        this.name_ = name;
    }

    public int getOrder() {
        return order_;
    }

    public void setOrder(int order) {
        this.order_ = order;
    }

    public String getProfilePath() {
        return profilePath_;
    }

    public void setProfilePath(String profilePath) {
        this.profilePath_ = profilePath;
    }

    public byte[] getCover() {
        return cover_;
    }

    public void setCover(byte[] cover) {
        this.cover_ = cover;
    }

    public String getImagePath(){
       return getProfilePath();
    }

    public int getImageStatus(){
        return States.CAST_PROFILE_DOWNLOADED;
    }
}
