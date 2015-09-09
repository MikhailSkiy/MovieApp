package com.example.admin.moviesapp.events;

/**
 * Created by Mikhail Valuyskiy on 08.09.2015.
 */
public class UpdateCastDetailsImageEvent {

    private byte[] image_;
    private String path_;

    public UpdateCastDetailsImageEvent(byte[] image){
        this.image_ = image;
    }

    public UpdateCastDetailsImageEvent(byte[] image,String path){
        this.image_ = image;
        this.path_ = path;
    }

    public byte[] getImage(){
        return this.image_;
    }

    public String getPath(){
        return this.path_;
    }
}
