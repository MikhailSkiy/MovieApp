package com.mikhailskiy.www.wovies.helpers;

import android.content.Context;

import com.mikhailskiy.www.wovies.activities.MainActivity;

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
