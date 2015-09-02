package com.example.admin.moviesapp.models;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Mikhail on 02.09.2015.
 */
public class ProductionCountry {

    @SerializedName("iso_3166_1")
    private String code_;

    @SerializedName("name")
    private String name_;

    public String getCode() {
        return code_;
    }

    public void setCode(String code) {
        this.code_ = code;
    }

    public String getName() {
        return name_;
    }

    public void setName(String name) {
        this.name_ = name;
    }
}
