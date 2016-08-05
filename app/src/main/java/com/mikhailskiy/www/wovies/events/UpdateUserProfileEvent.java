package com.mikhailskiy.www.wovies.events;

import com.mikhailskiy.www.wovies.models.UserAccountInfo;

/**
 * Created by Mikhail Valuyskiy on 14.10.2015.
 */

/**
 * Event for updating user information in main navigation menu
 */
public class UpdateUserProfileEvent {
    private UserAccountInfo userAccountInfo_;

    public UpdateUserProfileEvent(UserAccountInfo info) {
        this.userAccountInfo_ = info;
    }

    public UserAccountInfo getUserAccountInfo() {
        return this.userAccountInfo_;
    }

}
