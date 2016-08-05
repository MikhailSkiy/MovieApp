package com.mikhailskiy.www.wovies.models;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Mikhail Valuyskiy on 04.09.2015.
 */
public class Trailer {
    @SerializedName("id")
    private String id_;
    @SerializedName("iso_639_1")
    private String iso_639_1_;
    @SerializedName("key")
    private String key_;
    @SerializedName("name")
    private String name_;
    @SerializedName("site")
    private String site_;
    @SerializedName("size")
    private int size_;
    @SerializedName("type")
    private String type_;

    public String getId() {
        return id_;
    }

    public void setId(String id) {
        this.id_ = id;
    }

    public String getIso_639_1() {
        return iso_639_1_;
    }

    public void setIso_639_1(String iso_639_1) {
        this.iso_639_1_ = iso_639_1;
    }

    public String getKey() {
        return key_;
    }

    public void setKey(String key) {
        this.key_ = key;
    }

    public String getName() {
        return name_;
    }

    public void setName(String name) {
        this.name_ = name;
    }

    public String getSite() {
        return site_;
    }

    public void setSite(String site) {
        this.site_ = site;
    }

    public int getSize() {
        return size_;
    }

    public void setSize(int size) {
        this.size_ = size;
    }

    public String getType() {
        return type_;
    }

    public void setType(String type) {
        this.type_ = type;
    }
}
