package com.example.admin.moviesapp.events;

/**
 * Created by Mikhail Valuyskiy on 08.09.2015.
 */
public class UpdateCastDetailsImageEvent {

    private byte[] image_;

    public UpdateCastDetailsImageEvent(byte[] image){
        this.image_ = image;
    }

    public byte[] getImage(){
        return this.image_;
    }
}
