package com.example.admin.moviesapp.events;

/**
 * Created by Mikhail on 18.09.2015.
 */
public class UpdateMovieDetailsImageEvent {
    private byte[] image_;
    private String path_;

    public UpdateMovieDetailsImageEvent(byte[] image) {
        this.image_ = image;
    }

    public UpdateMovieDetailsImageEvent(byte[] image, String path) {
        this.image_ = image;
        this.path_ = path;
    }

    public byte[] getImage() {
        return this.image_;
    }

    public String getPath() {
        return this.path_;
    }
}
