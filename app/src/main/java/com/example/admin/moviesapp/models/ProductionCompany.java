package com.example.admin.moviesapp.models;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Mikhail on 02.09.2015.
 */
public class ProductionCompany {

    @SerializedName("name")
    private String name_;

    @SerializedName("id")
    private long id_;

    public String getName() {
        return name_;
    }

    public void setName(String name) {
        this.name_ = name;
    }

    public long getId() {
        return id_;
    }

    public void setId(long id) {
        this.id_ = id;
    }
}
