package com.example.admin.moviesapp.managers;


import android.app.Application;
import android.text.TextUtils;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;
import com.example.admin.moviesapp.BuildConfig;
import com.example.admin.moviesapp.helpers.LruBitmapCache;

import timber.log.Timber;


/**
 * Created by Mikhail Valuyskiy on 21.07.2015.
 */
public class AppController extends Application {

    public static final String TAG = AppController.class.getSimpleName();

    private RequestQueue requestQueue_;
    private ImageLoader imageLoader_;
    private static AppController instance_;

    @Override
    public void onCreate() {
        super.onCreate();
        if (BuildConfig.DEBUG) {
            Timber.plant(new Timber.DebugTree());
        }
        instance_ = this;
    }

    public static synchronized AppController getInstance(){
        return instance_;
    }

    public RequestQueue getRequestQueue(){
        if (requestQueue_ == null){
            requestQueue_ = Volley.newRequestQueue(getApplicationContext());
        }
        return requestQueue_;
    }

    public ImageLoader getImageLoader(){
        getRequestQueue();
        if (imageLoader_ == null){
            imageLoader_ = new ImageLoader(this.requestQueue_,new LruBitmapCache());
        }
        return this.imageLoader_;
    }

    public <T> void addToRequestQueue(Request<T> reguest,String tag){
        reguest.setTag(TextUtils.isEmpty(tag) ? TAG : tag);
        getRequestQueue().add(reguest);
    }

    public <T> void addToRequestQueue(Request<T> request){
        request.setTag(TAG);
        getRequestQueue().add(request);
    }

    public void cancelPendingRequests(Object tag){
        if (requestQueue_ != null){
            requestQueue_.cancelAll(tag);
        }
    }

}