package com.mikhailskiy.www.wovies.models;

/**
 * Created by Mikhail Valuyskiy on 14.10.2015.
 */

import com.google.gson.annotations.SerializedName;

/**
 * Represents the user account information
 */
public class UserAccountInfo {
    @SerializedName("id")
    private long userId_;
    @SerializedName("name")
    private String name_;
    @SerializedName("username")
    private String userNickname_;

    public long getUserId() {
        return userId_;
    }

    public void setUserId(long userId) {
        this.userId_ = userId;
    }

    public String getName() {
        return name_;
    }

    public void setName(String name) {
        this.name_ = name;
    }

    public String getUserNickname() {
        return userNickname_;
    }

    public void setUserNickname(String userNickname) {
        this.userNickname_ = userNickname;
    }
}
