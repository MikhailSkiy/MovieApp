package com.example.admin.moviesapp.models;

/**
 * Created by Mikhail Valuyskiy on 04.09.2015.
 */
public abstract class CommonMovie {
    public abstract String getImagePath();
    public abstract void setCover(byte[] cover);
    public abstract int getImageStatus();
}
