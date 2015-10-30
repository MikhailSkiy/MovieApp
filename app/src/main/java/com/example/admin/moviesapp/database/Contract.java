package com.example.admin.moviesapp.database;

import android.provider.BaseColumns;

/**
 * Created by Mikhail Valuyskiy on 14.09.2015.
 */
public class Contract {

    public static final class MoviesEntry implements BaseColumns {
        // Table name
        public static final String TABLE_NAME = "movies";
        public static final String COLUMN__ADULT = "adult";
        public static final String COLUMN_ORIGINAL_TITLE = "original_title";
        public static final String COLUMN_OVERVIEW = "overview";
        public static final String COLUMN_RELEASE_DATE = "release_date";
        public static final String COLUMN_POSTER_PATH = "poster_path";
        public static final String COLUMN_TITLE = "title";
        public static final String COLUMN_VIDEO = "video";
        public static final String COLUMN_VOTE_AVERAGE = "vote_average";
        public static final String COLUMN_VOTE_COUNT = "vote_count";
        public static final String COLUMN_COVER = "cover";
    }

    public static final class MoviesDetailsEntry implements BaseColumns {
        // Table name
        public static final String TABLE_NAME = "movies_details";
        public static final String COLUMN_ADULT = "adult";
        public static final String COLUMN_BACKDROP_PATH = "backdrop_path";
        public static final String COLUMN_BUDGET = "budget";
        //public static final String COLUMN_GENRES = "genres";
        public static final String COLUMN_HOMEPAGE = "homepage";
        public static final String COLUMN_IMDB_ID = "imdb_id";
        public static final String COLUMN_ORIGINAL_LANGUAGE = "original_language";
        public static final String COLUMN_ORIGINAL_TITLE = "original_title";
        public static final String COLUMN_OVERVIEW = "overview";
        public static final String COLUMN_POPULARITY = "popularity";
        public static final String COLUMN_POSTER_PATH = "poster_path";
        //public static final String COLUMN_PRODUCTION_COMPANIES = "production_companies";
        //public static final String COLUMN_PRODUCTION_COUNTRIES = "production_countries";
        public static final String COLUMN_RELEASE_DATE = "release_date";
        public static final String COLUMN_REVENUE = "revenue";
        public static final String COLUMN_RUNTIME = "runtime";
        //public static final String COLUMN_SPOKEN_LANGUAGES = "spoken_languages";
        public static final String COLUMN_STATUS = "status";
        public static final String COLUMN_TAGLINE = "tagline";
        public static final String COLUMN_TITLE = "title";
        public static final String COLUMN_VIDEO = "video";
        public static final String COLUMN_VOTE_AVERAGE = "vote_average";
        public static final String COLUMN_VOTE_COUNT = "vote_count";
        public static final String COLUMN_USER_RATING = "user_rating";
        public static final String COLUMN_COVER = "cover";
    }

    public static final class GenreEntry implements BaseColumns {
        // Table name
        public static final String TABLE_NAME = "genres";
        public static final String COLUMN_NAME = "name";
    }

    public static final class MovieGenreEntry implements BaseColumns {
        // Table name
        public static final String TABLE_NAME = "movie_genre";
        public static final String COLUMN_MOVIE_ID = "movie_id";
        public static final String COLUMN_GENRE_ID = "genre_id";
    }

    public static final class CastEntry implements BaseColumns {
        // Table name
        public static final String TABLE_NAME = "cast";
        public static final String COLUMN_CAST_ID = "cast_id";
        public static final String COLUMN_CHARACTER = "character";
        public static final String COLUMN_CREDIT_ID = "credit_id";
        public static final String COLUMN_ID = "_id";
        public static final String COLUMN_NAME = "name";
        public static final String COLUMN_ORDER = "order_key";
        public static final String COLUMN_PROFILE_PATH = "profile_path";
        public static final String COLUMN_MOVIE_ID = "movie_id";
        public static final String COLUMN_COVER = "cover";
    }

    public static final class CastDetailsEntry implements BaseColumns{
        // Table name
        public static final String TABLE_NAME = "cast_details";
        public static final String COLUMN_ADULT = "adult";
        public static final String COLUMN_ALSO_KNOWN_AS = "also_known_as";
        public static final String COLUMN_BIOGRAPHY = "biography";
        public static final String COLUMN_BIRTHDAY = "birthday";
        public static final String COLUMN_DEATHDAY = "deathday";
        public static final String COLUMN_HOMEPAGE = "homepage";
        public static final String COLUMN_IMDB_ID = "imdb_id";
        public static final String COLUMN_NAME = "name";
        public static final String COLUMN_PLACE_OF_BIRTH = "place_of_birth";
        public static final String COLUMN_POPULARITY = "popularity";
        public static final String COLUMN_PROFILE_PATH = "profile_path";
        public static final String COLUMN_COVER = "cover";
    }

    public static final class TrailersEntry implements BaseColumns{
        // Table name
        public static final String TABLE_NAME = "trailers";
        public static final String COLUMN_CODE = "code";
        public static final String COLUMN_KEY = "key";
        public static final String COLUMN_NAME = "name";
        public static final String COLUMN_SITE = "site";
        public static final String COLUMN_SIZE = "size";
        public static final String COLUMN_TYPE = "type";
        // this column for foreign key
        public static final String COLUMN_MOVIE_ID = "movie_id";
    }

    public static final class FavoriteMoviesEntry implements BaseColumns{
        public static final String TABLE_NAME = "favorits";
        public static final String COLUMN_MOVIE_ID = "movie_id";
    }

    public static final class WatchlistMoviesEntry implements BaseColumns{
        public static final String TABLE_NAME = "watchlist";
        public static final String COLUMN_MOVIE_ID = "movie_id";
    }
}

