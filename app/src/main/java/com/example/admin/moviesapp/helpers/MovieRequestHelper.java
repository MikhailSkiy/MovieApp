package com.example.admin.moviesapp.helpers;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.admin.moviesapp.R;
import com.example.admin.moviesapp.activities.MainActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Mikhail Valuyskiy on 28.09.2015.
 */
public class MovieRequestHelper {

    public static List<Integer> getGenres() {
        List<Integer> genres = new ArrayList();
        Context applicationContext = MainActivity.getContextOfApplication();
        SharedPreferences sharedPreferences = applicationContext.getSharedPreferences(applicationContext.getString(R.string.filter_preferences), Context.MODE_PRIVATE);
        GenresMap map = new GenresMap();
        for (String s : map.genresMap.keySet()) {
            int genreId = sharedPreferences.getInt(s,0);
            if (genreId != 0) {
                genres.add(genreId);
            }
        }
        return genres;
    }


    public static String getSortTypeFromPreferences(){
        Context applicationContext = MainActivity.getContextOfApplication();
        SharedPreferences sharedPreferences = applicationContext.getSharedPreferences(applicationContext.getString(R.string.filter_preferences), Context.MODE_PRIVATE);
        String sortType = sharedPreferences.getString("Sort_type", "popularity");
        String sortValue = sharedPreferences.getString(sortType, "desc");
        String resultValue = sortType + "." + sortValue;
        return resultValue;
    }


}
