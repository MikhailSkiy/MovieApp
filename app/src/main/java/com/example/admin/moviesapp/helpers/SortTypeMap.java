package com.example.admin.moviesapp.helpers;

import android.content.Context;

import com.example.admin.moviesapp.activities.MainActivity;

import java.util.HashMap;

/**
 * Created by Mikhail Valuyskiy on 25.09.2015.
 */
public class SortTypeMap {

    public HashMap<String,Boolean> sortMap = new HashMap<String,Boolean>();
    public SortTypeMap(){
        initMap();
    }

    private void initMap(){
        Context context = MainActivity.getContextOfApplication();
        sortMap.put("Popularity",true);
        sortMap.put("Rating",true);
        sortMap.put("Revenue",true);
    }
}
