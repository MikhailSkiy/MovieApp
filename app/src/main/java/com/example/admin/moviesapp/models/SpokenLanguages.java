package com.example.admin.moviesapp.models;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Mikhail on 02.09.2015.
 */
public class SpokenLanguages {

    @SerializedName("iso_639_1")
    private String code_;

    @SerializedName("name")
    private String name_;

    public String getName() {
        return name_;
    }

    public void setName(String name) {
        this.name_ = name;
    }

    public String getCode() {
        return code_;
    }

    public void setCode(String code) {
        this.code_ = code;
    }

}
