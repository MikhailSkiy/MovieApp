package com.example.admin.moviesapp.helpers;

import android.content.Context;

import com.example.admin.moviesapp.R;
import com.example.admin.moviesapp.activities.MainActivity;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Mikhail Valuyskiy on 22.09.2015.
 */
public class GenresMap {

    public class GenreContainer {
       public int itemId;
       public int genreId;

        public GenreContainer(int itemId, int genreId) {
            this.itemId = itemId;
            this.genreId = genreId;
        }
    }

    public HashMap<String, GenreContainer> genresMap = new HashMap<String, GenreContainer>();

    public GenresMap() {
        initMap();
    }


    private void initMap() {
        Context context = MainActivity.getContextOfApplication();
        genresMap.put(Util.getStringResource(R.string.action_key),new GenreContainer(R.id.action_btn,28));
        genresMap.put(Util.getStringResource(R.string.adventure_key),new GenreContainer(R.id.adventure_btn,12));
        genresMap.put(Util.getStringResource(R.string.animation_key),new GenreContainer(R.id.animation_btn,16));
        genresMap.put(Util.getStringResource(R.string.comedy_key),new GenreContainer(R.id.comedy_btn,35));
        genresMap.put(Util.getStringResource(R.string.crime_key),new GenreContainer(R.id.crime_btn,80));
        genresMap.put(Util.getStringResource(R.string.documentary_key),new GenreContainer(R.id.documentary_btn,99));
        genresMap.put(Util.getStringResource(R.string.drama_key),new GenreContainer(R.id.drama_btn,18));
        genresMap.put(Util.getStringResource(R.string.family_key),new GenreContainer(R.id.family_btn,10751));
        genresMap.put(Util.getStringResource(R.string.fantasy_key),new GenreContainer(R.id.fantasy_btn,14));
        genresMap.put(Util.getStringResource(R.string.foreign_key),new GenreContainer(R.id.foreign_btn,10769));
        genresMap.put(Util.getStringResource(R.string.history_key),new GenreContainer(R.id.history_btn,36));
        genresMap.put(Util.getStringResource(R.string.horror_key),new GenreContainer(R.id.horror_btn,27));
        genresMap.put(Util.getStringResource(R.string.music_key),new GenreContainer(R.id.music_btn,10402));
        genresMap.put(Util.getStringResource(R.string.mystery_key),new GenreContainer(R.id.mystery_btn,9648));
        genresMap.put(Util.getStringResource(R.string.romance_key),new GenreContainer(R.id.romance_btn,10749));
        genresMap.put(Util.getStringResource(R.string.science_fiction_key),new GenreContainer(R.id.science_fiction_btn,878));
        genresMap.put(Util.getStringResource(R.string.tv_movie_key),new GenreContainer(R.id.tv_movie_btn,10770));
        genresMap.put(Util.getStringResource(R.string.thriller_key),new GenreContainer(R.id.thriller_btn,53));
        genresMap.put(Util.getStringResource(R.string.war),new GenreContainer(R.id.war_btn,10752));
        genresMap.put(Util.getStringResource(R.string.western),new GenreContainer(R.id.western_btn,37));
    }
}
