package com.example.admin.moviesapp.helpers;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Mikhail Valuyskiy on 22.09.2015.
 */
public class GenresMap {
    public HashMap<String, Integer> genresMap = new HashMap<String, Integer>();

    public GenresMap() {
        initMap();
    }

    private void initMap() {
        genresMap.put("Action",28);
        genresMap.put("Adventure",12);
        genresMap.put("Animation",16);
        genresMap.put("Comedy",35);
        genresMap.put("Crime",80);
        genresMap.put("Documentary",99);
        genresMap.put("Drama",18);
        genresMap.put("Family",10751);
        genresMap.put("Fantasy",14);
        genresMap.put("Foreign",10769);
        genresMap.put("History",36);
        genresMap.put("Horror",27);
        genresMap.put("Music",10402);
        genresMap.put("Mystery",9648);
        genresMap.put("Romance",10749);
        genresMap.put("Science Fiction",878);
        genresMap.put("TV Movie",10770);
        genresMap.put("Thriller",53);
        genresMap.put("War",10752);
        genresMap.put("Western",37);
    }
}
