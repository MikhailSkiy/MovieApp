package com.example.admin.moviesapp.events;

/**
 * Created by Mikhail Valuyskiy on 22.10.2015.
 */
public class SuccessfullAlert {

    private String alertText_;

    public SuccessfullAlert(String alertText){
        this.alertText_ = alertText;
    }

    public String getAlertText(){
        return this.alertText_;
    }
}
