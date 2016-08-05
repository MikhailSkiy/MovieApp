package com.mikhailskiy.www.wovies.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.mikhailskiy.www.wovies.database.Contract.MoviesEntry;
import com.mikhailskiy.www.wovies.helpers.Util;
import com.mikhailskiy.www.wovies.models.Cast;
import com.mikhailskiy.www.wovies.models.Genre;
import com.mikhailskiy.www.wovies.models.Movie;
import com.mikhailskiy.www.wovies.models.MovieDetails;
import com.mikhailskiy.www.wovies.models.Trailer;

import java.util.ArrayList;
import java.util.List;

import static com.mikhailskiy.www.wovies.database.Contract.*;
import static com.mikhailskiy.www.wovies.database.Contract.CastDetailsEntry;
import static com.mikhailskiy.www.wovies.database.Contract.CastEntry;
import static com.mikhailskiy.www.wovies.database.Contract.GenreEntry;
import static com.mikhailskiy.www.wovies.database.Contract.MovieGenreEntry;
import static com.mikhailskiy.www.wovies.database.Contract.MoviesDetailsEntry;
import static com.mikhailskiy.www.wovies.database.Contract.TrailersEntry;

/**
 * Created by Mikhail Valuyskiy on 14.09.2015.
 */
public class DbHelper extends SQLiteOpenHelper {

    // When the database schema was changed, you must increment the database version
    private static final int DATABASE_VERSION = 7;
    public static final String DATABASE_NAME = "movies.db";

    public DbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // Creates table to hold movies data
    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        final String SQL_CREATE_MOVIES_TABLE = "CREATE TABLE " + MoviesEntry.TABLE_NAME + " (" +
                MoviesEntry._ID + " INTEGER PRIMARY KEY, " +
                MoviesEntry.COLUMN__ADULT + " INTEGER, " +
                MoviesEntry.COLUMN_ORIGINAL_TITLE + " TEXT, " +
                MoviesEntry.COLUMN_OVERVIEW + " TEXT, " +
                MoviesEntry.COLUMN_RELEASE_DATE + " INTEGER, " +
                MoviesEntry.COLUMN_POSTER_PATH + " TEXT, " +
                MoviesEntry.COLUMN_TITLE + " TEXT, " +
                MoviesEntry.COLUMN_VIDEO + " INTEGER, " +
                MoviesEntry.COLUMN_VOTE_AVERAGE + " REAL, " +
                MoviesEntry.COLUMN_VOTE_COUNT + " INTEGER, " +
                MoviesEntry.COLUMN_COVER + " BLOB);";

        final String SQL_CREATE_MOVIES_DETAILS_TABLE = "CREATE TABLE " + MoviesDetailsEntry.TABLE_NAME + " (" +
                MoviesDetailsEntry._ID + " INTEGER PRIMARY KEY, " +
                MoviesDetailsEntry.COLUMN_ADULT + " INTEGER, " +
                MoviesDetailsEntry.COLUMN_BACKDROP_PATH + " TEXT, " +
                MoviesDetailsEntry.COLUMN_BUDGET + " INTEGER, " +
                MoviesDetailsEntry.COLUMN_HOMEPAGE + " TEXT, " +
                MoviesDetailsEntry.COLUMN_IMDB_ID + " TEXT, " +
                MoviesDetailsEntry.COLUMN_ORIGINAL_LANGUAGE + " TEXT, " +
                MoviesDetailsEntry.COLUMN_ORIGINAL_TITLE + " TEXT, " +
                MoviesDetailsEntry.COLUMN_OVERVIEW + " TEXT, " +
                MoviesDetailsEntry.COLUMN_POPULARITY + " REAL, " +
                MoviesDetailsEntry.COLUMN_POSTER_PATH + " TEXT, " +
                MoviesDetailsEntry.COLUMN_RELEASE_DATE + " TEXT, " +
                MoviesDetailsEntry.COLUMN_REVENUE + " INTEGER, " +
                MoviesDetailsEntry.COLUMN_RUNTIME + " INTEGER, " +
                MoviesDetailsEntry.COLUMN_STATUS + " TEXT, " +
                MoviesDetailsEntry.COLUMN_TAGLINE + " TEXT, " +
                MoviesDetailsEntry.COLUMN_TITLE + " TEXT, " +
                MoviesDetailsEntry.COLUMN_VIDEO + " INTEGER, " +
                MoviesDetailsEntry.COLUMN_VOTE_AVERAGE + " REAL, " +
                MoviesDetailsEntry.COLUMN_VOTE_COUNT + " INTEGER, " +
                MoviesDetailsEntry.COLUMN_USER_RATING + " REAL, " +
                MoviesDetailsEntry.COLUMN_COVER + " BLOB);";

        final String SQL_CREATE_MOVIE_GENRE_TABLE = "CREATE TABLE " + MovieGenreEntry.TABLE_NAME + " (" +
                MovieGenreEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                MovieGenreEntry.COLUMN_MOVIE_ID + " INTEGER, " +
                MovieGenreEntry.COLUMN_GENRE_ID + " INTEGER, " +
                // Set up the movie_id column as a foreign key to moviedetails table
                " FOREIGN KEY (" + MovieGenreEntry.COLUMN_MOVIE_ID + ") REFERENCES " +
                MoviesDetailsEntry.TABLE_NAME + " (" + MoviesDetailsEntry._ID + "), " +

                " FOREIGN KEY (" + MovieGenreEntry.COLUMN_GENRE_ID + ") REFERENCES " +
                GenreEntry.TABLE_NAME + " (" + GenreEntry._ID + ")" + " );";

        final String SQL_CREATE_GENRES_TABLE = "CREATE TABLE " + GenreEntry.TABLE_NAME + " (" +
                GenreEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                GenreEntry.COLUMN_NAME + " TEXT );";


        final String SQL_CREATE_CAST_DETAILS_TABLE = "CREATE TABLE " + CastDetailsEntry.TABLE_NAME + " (" +
                CastDetailsEntry._ID + " INTEGER PRIMARY KEY," +
                CastDetailsEntry.COLUMN_ADULT + " INTEGER, " +
                CastDetailsEntry.COLUMN_ALSO_KNOWN_AS + " TEXT, " +
                CastDetailsEntry.COLUMN_BIOGRAPHY + " TEXT, " +
                CastDetailsEntry.COLUMN_BIRTHDAY + " INTEGER, " +
                CastDetailsEntry.COLUMN_DEATHDAY + " INTEGER, " +
                CastDetailsEntry.COLUMN_HOMEPAGE + " TEXT, " +
                CastDetailsEntry.COLUMN_IMDB_ID + " TEXT, " +
                CastDetailsEntry.COLUMN_NAME + " TEXT, " +
                CastDetailsEntry.COLUMN_PLACE_OF_BIRTH + " TEXT, " +
                CastDetailsEntry.COLUMN_POPULARITY + " REAL, " +
                CastDetailsEntry.COLUMN_PROFILE_PATH + " TEXT, " +
                CastDetailsEntry.COLUMN_COVER + " BLOB);";

        final String SQL_CREATE_CAST_TABLE = "CREATE TABLE " + CastEntry.TABLE_NAME + " (" +
                CastEntry._ID + " INTEGER PRIMARY KEY," +
                CastEntry.COLUMN_CHARACTER + " TEXT, " +
                CastEntry.COLUMN_CREDIT_ID + " TEXT, " +
                CastEntry.COLUMN_CAST_ID + " INTEGER, " +
                CastEntry.COLUMN_NAME + " TEXT, " +
                CastEntry.COLUMN_ORDER + " INTEGER, " +
                CastEntry.COLUMN_PROFILE_PATH + " PROFILE_PATH, " +
                CastEntry.COLUMN_MOVIE_ID + " INTEGER, " +
                CastEntry.COLUMN_COVER + " BLOB, " +
                " FOREIGN KEY (" + CastEntry.COLUMN_MOVIE_ID + ") REFERENCES " +
                MoviesDetailsEntry.TABLE_NAME + " (" + MoviesDetailsEntry._ID + ")" + " );";

        final String SQL_CREATE_TRAILER_TABLE = "CREATE TABLE " + TrailersEntry.TABLE_NAME + " (" +
                TrailersEntry._ID + " TEXT PRIMARY KEY, " +
                TrailersEntry.COLUMN_CODE + " TEXT, " +
                TrailersEntry.COLUMN_KEY + " TEXT, " +
                TrailersEntry.COLUMN_NAME + " TEXT, " +
                TrailersEntry.COLUMN_SITE + " SITE, " +
                TrailersEntry.COLUMN_SIZE + " SIZE, " +
                TrailersEntry.COLUMN_TYPE + " TYPE, " +
                TrailersEntry.COLUMN_MOVIE_ID + " INTEGER, " +
                " FOREIGN KEY (" + TrailersEntry.COLUMN_MOVIE_ID + ") REFERENCES " +
                MoviesDetailsEntry.TABLE_NAME + " (" + MoviesDetailsEntry._ID + ")" + " );";

        final String SQL_CREATE_FAVORITE_MOVIES_TABLE = "CREATE TABLE " + FavoriteMoviesEntry.TABLE_NAME + " (" +
                FavoriteMoviesEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                FavoriteMoviesEntry.COLUMN_MOVIE_ID + " INTEGER, " +
                " FOREIGN KEY (" + FavoriteMoviesEntry.COLUMN_MOVIE_ID + ") REFERENCES " +
                MoviesDetailsEntry.TABLE_NAME + " (" + MoviesDetailsEntry._ID + ")" + " );";

        final String SQL_CREATE_WATCHLIST_TABLE = "CREATE TABLE " + WatchlistMoviesEntry.TABLE_NAME + " (" +
                WatchlistMoviesEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                WatchlistMoviesEntry.COLUMN_MOVIE_ID + " INTEGER, " +
                " FOREIGN KEY (" + WatchlistMoviesEntry.COLUMN_MOVIE_ID +  ") REFERENCES " +
                MoviesDetailsEntry.TABLE_NAME + " (" + MoviesDetailsEntry._ID + ")" + " );";

        sqLiteDatabase.execSQL(SQL_CREATE_MOVIES_TABLE);
        sqLiteDatabase.execSQL(SQL_CREATE_MOVIES_DETAILS_TABLE);
        sqLiteDatabase.execSQL(SQL_CREATE_GENRES_TABLE);
        sqLiteDatabase.execSQL(SQL_CREATE_MOVIE_GENRE_TABLE);
        sqLiteDatabase.execSQL(SQL_CREATE_CAST_TABLE);
        sqLiteDatabase.execSQL(SQL_CREATE_TRAILER_TABLE);
        sqLiteDatabase.execSQL(SQL_CREATE_FAVORITE_MOVIES_TABLE);
        sqLiteDatabase.execSQL(SQL_CREATE_WATCHLIST_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + MoviesEntry.TABLE_NAME);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + CastEntry.TABLE_NAME);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + CastDetailsEntry.TABLE_NAME);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TrailersEntry.TABLE_NAME);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + MoviesDetailsEntry.TABLE_NAME);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + GenreEntry.TABLE_NAME);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + MovieGenreEntry.TABLE_NAME);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + FavoriteMoviesEntry.TABLE_NAME);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + WatchlistMoviesEntry.TABLE_NAME);
        onCreate(sqLiteDatabase);
    }

    public void deleteDatabase(Context context) {
        context.deleteDatabase(DbHelper.DATABASE_NAME);
    }

    //region Watchlist operations

    /**
     * Adds id of selected movie to the watchlist
     * Returns rowId if data was successfully inserted
     * Otherwise, if the data exists return 0
     */
    public long addMovieToWatchlist(long movieId) {
        if (!isMovieInWatchlist(movieId)) {
            SQLiteDatabase database = this.getWritableDatabase();
            ContentValues values = insertMovieInWatchlist(movieId);
            long rowId = database.insert(WatchlistMoviesEntry.TABLE_NAME, null, values);
            database.close();
            return rowId;
        } else return 0;
    }

    public boolean isMovieInWatchlist(long movieId) {
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
        String query = "SELECT * FROM " + WatchlistMoviesEntry.TABLE_NAME + " WHERE " + WatchlistMoviesEntry.COLUMN_MOVIE_ID + " = " + movieId;
        Cursor cursor = sqLiteDatabase.rawQuery(query, null);
        if (cursor.moveToFirst()) {
            cursor.close();
            return true;
        } else {
            cursor.close();
            return false;
        }
    }

    private ContentValues insertMovieInWatchlist(long movieId) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(WatchlistMoviesEntry.COLUMN_MOVIE_ID, movieId);
        return contentValues;
    }

    /**
     * Delets movie from watchlist table
     * @return true - succeses, otherwise - false
     */

    public boolean deleteMovieFromWatchlist(long movieId) {
        SQLiteDatabase database = this.getWritableDatabase();
        int rowsDeleted = database.delete(WatchlistMoviesEntry.TABLE_NAME, WatchlistMoviesEntry.COLUMN_MOVIE_ID + "=?", new String[]{Long.toString(movieId)});
        if (rowsDeleted > 0) {
            return true;
        } else {
            return false;
        }
    }

    //endregions

    //region Favorite Movies operations

    /**
     * Adds id of selected movie to the favorite tables
     * Returns rowId if data was succesfully inserted
     * Otherwise, if the data exists returns 0
     */
    public long addMovieToFavorites(long movieId) {
        if (!isMovieIsFavorite(movieId)) {
            SQLiteDatabase database = this.getWritableDatabase();
            ContentValues values = insertFavoriteMovieIntoContentValues(movieId);
            long rowId = database.insert(FavoriteMoviesEntry.TABLE_NAME, null, values);
            database.close();
            return rowId;
        } else return 0;
    }

    /**
     * Delets movie from favorits table
     * @return true - succeses, otherwise - false
     */
    public boolean deleteFavoriteMovie(long movieId) {
        SQLiteDatabase database = this.getWritableDatabase();
        int rowsDeleted = database.delete(FavoriteMoviesEntry.TABLE_NAME, FavoriteMoviesEntry.COLUMN_MOVIE_ID + "=?", new String[]{Long.toString(movieId)});
        if (rowsDeleted > 0) {
            return true;
        } else {
            return false;
        }
    }

    private ContentValues insertFavoriteMovieIntoContentValues(long movieId) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(FavoriteMoviesEntry.COLUMN_MOVIE_ID, movieId);
        return contentValues;
    }

    // Checks is this movie in the favorite table
    public boolean isMovieIsFavorite(long movieId) {
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
        String query = "SELECT * FROM " + FavoriteMoviesEntry.TABLE_NAME + " WHERE " + FavoriteMoviesEntry.COLUMN_MOVIE_ID + " = " + movieId;
        Cursor cursor = sqLiteDatabase.rawQuery(query, null);

        if (cursor.moveToFirst()) {
            cursor.close();
            return true;
        } else {
            cursor.close();
            return false;
        }
    }

    // Returns the list of all movies which in the favorite movies table
    public List<MovieDetails> getAllFavoriteMovies() {
        SQLiteDatabase database = this.getWritableDatabase();
        List<MovieDetails> movieDetailses = new ArrayList<>();
        List<Integer> favoritesMovieId = new ArrayList<>();
        String selectQuery = "SELECT * FROM " + FavoriteMoviesEntry.TABLE_NAME;
        Cursor cursor = database.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                int id = getIdFromCursor(cursor);
                String selectFavoriteMovieQuery = "SELECT * FROM " + MoviesDetailsEntry.TABLE_NAME + "WHERE " + MoviesDetailsEntry._ID + "= " + id;
                Cursor movieCursor = database.rawQuery(selectFavoriteMovieQuery, null);
                if (movieCursor.moveToFirst()) {
                    do {
                        MovieDetails movieDetails = getMovieDetailsFromCursor(movieCursor);
                        movieDetailses.add(movieDetails);
                    } while (movieCursor.moveToNext());
                }
            } while (cursor.moveToNext());
        }
        database.close();

        return movieDetailses;
    }

    private int getIdFromCursor(Cursor cursor) {
        int id = cursor.getInt(cursor.getColumnIndex(FavoriteMoviesEntry.COLUMN_MOVIE_ID));
        return id;
    }

    //endregion

    //region Movie operations
    // TODO add limit for 20 results per one operation
    public List<Movie> getAllMovies() {
        SQLiteDatabase database = this.getWritableDatabase();
        List<Movie> movieList = new ArrayList<>();
        String selectQuery = " SELECT * FROM " + MoviesEntry.TABLE_NAME;
        Cursor cursor = database.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                Movie movie = getMovieFromCursor(cursor);
                movieList.add(movie);
            } while (cursor.moveToNext());
        }
        database.close();

        return movieList;
    }

    private Movie getMovieFromCursor(Cursor cursor) {
        int id = cursor.getInt(cursor.getColumnIndex(MoviesEntry._ID));
        boolean adult = (cursor.getInt(cursor.getColumnIndex(MoviesEntry.COLUMN__ADULT)) == 1) ? true : false;
        String originalTitle = cursor.getString(cursor.getColumnIndex(MoviesEntry.COLUMN_ORIGINAL_TITLE));
        String overview = cursor.getString(cursor.getColumnIndex(MoviesEntry.COLUMN_OVERVIEW));
        String releasedDate = Util.getDateFromUnix(cursor.getLong(cursor.getColumnIndex(MoviesEntry.COLUMN_RELEASE_DATE)));
        String posterPath = cursor.getString(cursor.getColumnIndex(MoviesEntry.COLUMN_POSTER_PATH));
        String title = cursor.getString(cursor.getColumnIndex(MoviesEntry.COLUMN_TITLE));
        boolean video = (cursor.getInt(cursor.getColumnIndex(MoviesEntry.COLUMN_VIDEO)) == 1) ? true : false;
        double voteAverage = cursor.getDouble(cursor.getColumnIndex(MoviesEntry.COLUMN_VOTE_AVERAGE));
        int voteCount = cursor.getInt(cursor.getColumnIndex(MoviesEntry.COLUMN_VOTE_COUNT));
        byte[] cover = cursor.getBlob(cursor.getColumnIndex(MoviesEntry.COLUMN_COVER));
        // Set values into object
        Movie movie = new Movie();
        movie.setId(id);
        movie.setAdult(adult);
        movie.setOriginalTitle(originalTitle);
        movie.setOverview(overview);
        movie.setReleaseDate(releasedDate);
        movie.setPosterPath(posterPath);
        movie.setTitle(title);
        movie.setVideo(video);
        movie.setVoteAverage(voteAverage);
        movie.setVoteCount(voteCount);
        movie.setCover(cover);

        return movie;
    }

    public void addMovie(Movie movie) {
        if (movie != null) {
            if (!isMovieExists(movie.getId())) {
                SQLiteDatabase database = this.getWritableDatabase();
                ContentValues values = insertMovieInContentValues(movie);
                database.insert(MoviesEntry.TABLE_NAME, null, values);
                database.close();
            }
        } else {
            throw new IllegalArgumentException("Passed movie object is null or already exist");
        }
    }

    private ContentValues insertMovieInContentValues(Movie movie) {
        ContentValues values = new ContentValues();
        values.put(MoviesEntry._ID, movie.getId());
        values.put(MoviesEntry.COLUMN__ADULT, movie.isAdult() ? 1 : 0);
        values.put(MoviesEntry.COLUMN_ORIGINAL_TITLE, movie.getOriginalTitle());
        values.put(MoviesEntry.COLUMN_OVERVIEW, movie.getOverview());
        values.put(MoviesEntry.COLUMN_RELEASE_DATE, Util.getUnixDate(movie.getReleaseDate()));
        values.put(MoviesEntry.COLUMN_POSTER_PATH, movie.getPosterPath());
        values.put(MoviesEntry.COLUMN_TITLE, movie.getTitle());
        values.put(MoviesEntry.COLUMN_VIDEO, movie.isVideo() ? 1 : 0);
        values.put(MoviesEntry.COLUMN_VOTE_AVERAGE, movie.getVoteAverage());
        values.put(MoviesEntry.COLUMN_VOTE_COUNT, movie.getVoteCount());
        values.put(MoviesEntry.COLUMN_COVER, movie.getCover());

        return values;
    }

    public boolean isMovieExists(long movieId) {
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
        String query = "SELECT * FROM " + MoviesEntry.TABLE_NAME + " WHERE " + MoviesEntry._ID + " = " + movieId;
        Cursor cursor = sqLiteDatabase.rawQuery(query, null);
        if (cursor.getCount() <= 0) {
            cursor.close();
            return false;
        }
        cursor.close();
        return true;

    }

    //endregion

    //region Cast operations
    public long addCast(Cast cast, long castId) {
        long id = -1;
        if (cast != null) {
            if (!isCastExists(cast.getId())) {
                SQLiteDatabase database = this.getWritableDatabase();
                ContentValues castValues = insertCastInContentValues(cast, castId);
                id = database.insert(CastEntry.TABLE_NAME, null, castValues);
                database.close();
                return id;
            }
        } else {
            throw new IllegalArgumentException("Passed cast object is null or already exist");
        }
        return id;
    }

//
//    String query = "SELECT " + TrailersEntry.TABLE_NAME + "." + TrailersEntry.COLUMN_NAME +
//            " FROM " + MoviesDetailsEntry.TABLE_NAME + " JOIN " + TrailersEntry.TABLE_NAME +
//            " ON " + " ( " + MoviesDetailsEntry.TABLE_NAME  + "." + MoviesDetailsEntry._ID +
//            " = " + TrailersEntry.TABLE_NAME + "." + TrailersEntry.COLUMN_MOVIE_ID + " )" +
//            " WHERE " + TrailersEntry.TABLE_NAME + "." + TrailersEntry._ID + " = " + movieId;


//    SELECT cast.name from movies_details JOIN cast ON (movies_details._id = cast.movie_id) where cast.movie_id = 76341
//
//    SELECT * from cast where cast.movie_id = 76341

    public List<Cast> getAllCast(long movieId) {
        SQLiteDatabase database = this.getWritableDatabase();
        List<Cast> castList = new ArrayList<>();
        String query = "SELECT * FROM " +
                CastEntry.TABLE_NAME + " WHERE " + CastEntry.COLUMN_MOVIE_ID + " = " + movieId;
        Cursor cursor = database.rawQuery(query, null);

        if (cursor.moveToFirst()) {
            do {
                Cast cast = getCastFromCursor(cursor);
                castList.add(cast);
            } while (cursor.moveToNext());
        }
        database.close();

        return castList;
    }

    private Cast getCastFromCursor(Cursor cursor) {
        long id = cursor.getLong(cursor.getColumnIndex(CastEntry._ID));
        long castId = cursor.getLong(cursor.getColumnIndex(CastEntry.COLUMN_CAST_ID));
        String character = cursor.getString(cursor.getColumnIndex(CastEntry.COLUMN_CHARACTER));
        String creditId = cursor.getString(cursor.getColumnIndex(CastEntry.COLUMN_CREDIT_ID));
        String name = cursor.getString(cursor.getColumnIndex(CastEntry.COLUMN_NAME));
        int order = cursor.getInt(cursor.getColumnIndex(CastEntry.COLUMN_ORDER));
        String profilePath = cursor.getString(cursor.getColumnIndex(CastEntry.COLUMN_PROFILE_PATH));
        byte[] cover = cursor.getBlob(cursor.getColumnIndex(CastEntry.COLUMN_COVER));
        // Set values to Casr object
        Cast cast = new Cast();
        cast.setId(id);
        cast.setCastId(castId);
        cast.setCharacter(character);
        cast.setCreditId(creditId);
        cast.setName(name);
        cast.setOrder(order);
        cast.setProfilePath(profilePath);
        cast.setCover(cover);

        return cast;
    }

    private ContentValues insertCastInContentValues(Cast cast, long movieId) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(CastEntry._ID, cast.getId());
        contentValues.put(CastEntry.COLUMN_CHARACTER, cast.getCharacter());
        contentValues.put(CastEntry.COLUMN_CREDIT_ID, cast.getCreditId());
        contentValues.put(CastEntry.COLUMN_CAST_ID, cast.getCastId());
        contentValues.put(CastEntry.COLUMN_NAME, cast.getName());
        contentValues.put(CastEntry.COLUMN_ORDER, cast.getOrder());
        contentValues.put(CastEntry.COLUMN_PROFILE_PATH, cast.getProfilePath());
        contentValues.put(CastEntry.COLUMN_COVER, cast.getCover());
        contentValues.put(CastEntry.COLUMN_MOVIE_ID, movieId);

        return contentValues;
    }

    private boolean isCastExists(long castId) {
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
        String query = "SELECT * FROM " + CastEntry.TABLE_NAME + " WHERE " + CastEntry._ID + " = " + castId;
        Cursor castCursor = sqLiteDatabase.rawQuery(query, null);
        if (castCursor.getCount() <= 0) {
            castCursor.close();
            return false;
        }
        castCursor.close();
        return true;
    }

    //endregion

    //region Trailers operations
    public void addTrailer(Trailer trailer, long movieId) {
        if (trailer != null) {
            if (!isTrailerExists(trailer.getId())) {
                SQLiteDatabase database = this.getWritableDatabase();
                ContentValues trailerValues = insertTrailerInContentValues(trailer, movieId);
                database.insert(TrailersEntry.TABLE_NAME, null, trailerValues);
                database.close();
            }
        } else {
            throw new IllegalArgumentException("Passed trailer object is null or already exist");
        }
    }

    public List<Trailer> getAllTrailers(long movieId) {
        SQLiteDatabase database = this.getWritableDatabase();
        List<Trailer> trailerList = new ArrayList<>();
        String query = "SELECT * FROM " + MoviesDetailsEntry.TABLE_NAME + " JOIN " + TrailersEntry.TABLE_NAME +
                " ON " + " ( " + MoviesDetailsEntry.TABLE_NAME + "." + MoviesDetailsEntry._ID +
                " = " + TrailersEntry.TABLE_NAME + "." + TrailersEntry.COLUMN_MOVIE_ID + " )" +
                " WHERE " + TrailersEntry.TABLE_NAME + "." + TrailersEntry.COLUMN_MOVIE_ID + " = " + movieId;

        Cursor cursor = database.rawQuery(query, null);

        if (cursor.moveToFirst()) {
            do {
                Trailer trailer = getTrailerFromCursor(cursor);
                trailerList.add(trailer);
            } while (cursor.moveToNext());
        }
        database.close();

        return trailerList;
    }

    private Trailer getTrailerFromCursor(Cursor cursor) {
        String trailerId = cursor.getString(cursor.getColumnIndex(TrailersEntry._ID));
        String code = cursor.getString(cursor.getColumnIndex(TrailersEntry.COLUMN_CODE));
        String key = cursor.getString(cursor.getColumnIndex(TrailersEntry.COLUMN_KEY));
        String name = cursor.getString(cursor.getColumnIndex(TrailersEntry.COLUMN_NAME));
        String site = cursor.getString(cursor.getColumnIndex(TrailersEntry.COLUMN_SITE));
        int size = cursor.getInt(cursor.getColumnIndex(TrailersEntry.COLUMN_SIZE));
        String type = cursor.getString(cursor.getColumnIndex(TrailersEntry.COLUMN_TYPE));
        // Set values into Trailer Object
        Trailer trailer = new Trailer();
        trailer.setId(trailerId);
        trailer.setIso_639_1(code);
        trailer.setKey(key);
        trailer.setName(name);
        trailer.setSite(site);
        trailer.setSize(size);
        trailer.setType(type);

        return trailer;
    }

    private ContentValues insertTrailerInContentValues(Trailer trailer, long movieId) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(TrailersEntry._ID, trailer.getId());
        contentValues.put(TrailersEntry.COLUMN_CODE, trailer.getIso_639_1());
        contentValues.put(TrailersEntry.COLUMN_KEY, trailer.getKey());
        contentValues.put(TrailersEntry.COLUMN_NAME, trailer.getName());
        contentValues.put(TrailersEntry.COLUMN_SITE, trailer.getSite());
        contentValues.put(TrailersEntry.COLUMN_SIZE, trailer.getSize());
        contentValues.put(TrailersEntry.COLUMN_TYPE, trailer.getType());
        contentValues.put(TrailersEntry.COLUMN_MOVIE_ID, movieId);

        return contentValues;
    }

    private boolean isTrailerExists(String trailerId) {
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
        String query = "SELECT * FROM " + TrailersEntry.TABLE_NAME + " WHERE " + TrailersEntry._ID + " = " + "\"" + trailerId + "\"";
        Cursor trailerCursor = sqLiteDatabase.rawQuery(query, null);
        if (trailerCursor.getCount() <= 0) {
            trailerCursor.close();
            return false;
        }
        trailerCursor.close();
        return true;
    }

    //endregion

    //region Movie Details operations

    public void addMovieDetails(MovieDetails movieDetails) {
        if (movieDetails != null) {
            if (!isMovieDetailsExists(movieDetails.getId())) {
                SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
                // Insert movie details data
                ContentValues values = insertMovieDetailsInCntnValues(movieDetails);
                sqLiteDatabase.insert(MoviesDetailsEntry.TABLE_NAME, null, values);
                // Insert genres data into genres table
                for (int i = 0; i < movieDetails.getGenres().size(); i++) {
                    // If such genre doesn't exist, put into database
                    if (!isGenreExist(movieDetails.getGenres().get(i).getGenreId())) {
                        ContentValues genreValues = insertMovieGenresInCntnValues(movieDetails.getGenres().get(i));
                        sqLiteDatabase.insert(GenreEntry.TABLE_NAME, null, genreValues);
                    }
                    // Insert genre_id and movie_id into movie_genre table
                    ContentValues movieGenreValues = insertMovieGenreValues(movieDetails.getId(), movieDetails.getGenres().get(i).getGenreId());
                    sqLiteDatabase.insert(MovieGenreEntry.TABLE_NAME, null, movieGenreValues);
                }
                sqLiteDatabase.close();
            }
        } else {
            throw new IllegalArgumentException("Passed movie object is null or already exist");
        }
    }

    private ContentValues insertMovieDetailsInCntnValues(MovieDetails movieDetails) {
        ContentValues values = new ContentValues();
        values.put(MoviesDetailsEntry._ID, movieDetails.getId());
        values.put(MoviesDetailsEntry.COLUMN_ADULT, movieDetails.isAdult() ? 1 : 0);
        values.put(MoviesDetailsEntry.COLUMN_BACKDROP_PATH, movieDetails.getBackdropPath());
        values.put(MoviesDetailsEntry.COLUMN_BUDGET, movieDetails.getBudget());
        values.put(MoviesDetailsEntry.COLUMN_HOMEPAGE, movieDetails.getHomepage());
        values.put(MoviesDetailsEntry.COLUMN_IMDB_ID, movieDetails.getImdbId());
        values.put(MoviesDetailsEntry.COLUMN_ORIGINAL_LANGUAGE, movieDetails.getOriginalLanguage());
        values.put(MoviesDetailsEntry.COLUMN_ORIGINAL_TITLE, movieDetails.getOriginalTitle());
        values.put(MoviesDetailsEntry.COLUMN_OVERVIEW, movieDetails.getOverview());
        values.put(MoviesDetailsEntry.COLUMN_POPULARITY, movieDetails.getPopularity());
        values.put(MoviesDetailsEntry.COLUMN_POSTER_PATH, movieDetails.getPosterPath());
        values.put(MoviesDetailsEntry.COLUMN_RELEASE_DATE, movieDetails.getReleaseDate());
        values.put(MoviesDetailsEntry.COLUMN_REVENUE, movieDetails.getRevenue());
        values.put(MoviesDetailsEntry.COLUMN_RUNTIME, movieDetails.getRuntime());
        values.put(MoviesDetailsEntry.COLUMN_STATUS, movieDetails.getStatus());
        values.put(MoviesDetailsEntry.COLUMN_TAGLINE, movieDetails.getTagline());
        values.put(MoviesDetailsEntry.COLUMN_TITLE, movieDetails.getTitle());
        values.put(MoviesDetailsEntry.COLUMN_VOTE_AVERAGE, movieDetails.getVoteAverage());
        values.put(MoviesDetailsEntry.COLUMN_VOTE_COUNT, movieDetails.getVoteCount());
        values.put(MoviesDetailsEntry.COLUMN_COVER, movieDetails.getCover());
        values.put(MoviesDetailsEntry.COLUMN_USER_RATING, movieDetails.getUserRating());

        return values;
    }

    public long updateMovieRatingGivenByUser(long movieId, double newRating) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(MoviesDetailsEntry.COLUMN_USER_RATING, newRating);

        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        int updatedRows = sqLiteDatabase.update(MoviesDetailsEntry.TABLE_NAME, contentValues, "_id" + "=" + movieId, null);
        return updatedRows;
    }

    /**
     *   Checks is there is rating of movie, given by user
     *   False - if there is no such rating
     *   True - if user has rated this movie
     */
    public boolean isRatingGivenByUserExist(long movieId){
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
        String query = "SELECT " + MoviesDetailsEntry.COLUMN_USER_RATING + " FROM " + MoviesDetailsEntry.TABLE_NAME + " WHERE " +
                MoviesDetailsEntry._ID + " = " + movieId;
        Cursor cursor = sqLiteDatabase.rawQuery(query,null);
        if (cursor.getCount()<=0){
            cursor.close();
            return false;
        } else {
            cursor.close();
            return true;
        }
    }


    private ContentValues insertMovieGenresInCntnValues(Genre genre) {
        ContentValues values = new ContentValues();
        values.put(GenreEntry._ID, genre.getGenreId());
        values.put(GenreEntry.COLUMN_NAME, genre.getGenreName());

        return values;
    }

    private ContentValues insertMovieGenreValues(long movieId, long genreId) {
        ContentValues values = new ContentValues();
        values.put(MovieGenreEntry.COLUMN_GENRE_ID, genreId);
        values.put(MovieGenreEntry.COLUMN_MOVIE_ID, movieId);

        return values;
    }

    private boolean isGenreExist(long genreId) {
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
        String query = "SELECT * FROM " + GenreEntry.TABLE_NAME + " WHERE " + GenreEntry._ID + " = " + genreId;
        Cursor cursor = sqLiteDatabase.rawQuery(query, null);
        if (cursor.getCount() <= 0) {
            cursor.close();
            return false;
        } else {
            cursor.close();
            return true;
        }
    }

    public boolean isMovieDetailsExists(long movieDetailsId) {
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
        String query = "SELECT * FROM " + MoviesDetailsEntry.TABLE_NAME + " WHERE " + MoviesDetailsEntry._ID + " = " + movieDetailsId;
        Cursor cursor = sqLiteDatabase.rawQuery(query, null);
        if (cursor.getCount() <= 0) {
            cursor.close();
            return false;
        }
        cursor.close();

        return true;
    }

    public MovieDetails getMovieDetails(long movieDetailsId) {
        if (isMovieDetailsExists(movieDetailsId)) {
            SQLiteDatabase database = this.getWritableDatabase();
            MovieDetails movieDetails = new MovieDetails();
            String selectQuery = " SELECT * FROM " + MoviesDetailsEntry.TABLE_NAME + " WHERE " + MoviesDetailsEntry._ID + " = " + movieDetailsId;
            Cursor cursor = database.rawQuery(selectQuery, null);
            movieDetails = getMovieDetailsFromCursor(cursor);
            // Get genres for this movie
            List<Genre> genreList = new ArrayList<>();
            String selectGenresQuery = " SELECT " + GenreEntry.TABLE_NAME + "." + GenreEntry.COLUMN_NAME + "," + GenreEntry.TABLE_NAME + "." + GenreEntry._ID + " FROM " + MoviesDetailsEntry.TABLE_NAME +
                    " JOIN " + MovieGenreEntry.TABLE_NAME + " on " + " (" + MoviesDetailsEntry.TABLE_NAME + "." + MoviesDetailsEntry._ID + " = " + MovieGenreEntry.COLUMN_MOVIE_ID + " ) " +
                    " JOIN " + GenreEntry.TABLE_NAME + " on " + " (" + MovieGenreEntry.COLUMN_GENRE_ID + " = " + GenreEntry.TABLE_NAME + "." + GenreEntry._ID + " ) " +
                    " WHERE " + MoviesDetailsEntry.TABLE_NAME + "." + MoviesDetailsEntry._ID + " = " + movieDetailsId;
            Cursor genresCursor = database.rawQuery(selectGenresQuery, null);
            if (genresCursor.moveToFirst()) {
                do {
                    Genre genre = getGenreFromCursor(genresCursor);
                    genreList.add(genre);
                } while (genresCursor.moveToNext());
            }
            movieDetails.setGenres(genreList);

            return movieDetails;
        } else {
            return null;
        }
    }

    private Genre getGenreFromCursor(Cursor cursor) {
        int id = cursor.getInt(cursor.getColumnIndex(GenreEntry._ID));
        String name = cursor.getString(cursor.getColumnIndex(GenreEntry.COLUMN_NAME));
        Genre genre = new Genre();
        genre.setGenreId(id);
        genre.setGenreName(name);

        return genre;
    }

    private MovieDetails getMovieDetailsFromCursor(Cursor cursor) {
        cursor.moveToFirst();
        int id = cursor.getInt(cursor.getColumnIndex(MoviesDetailsEntry._ID));
        boolean adult = (cursor.getInt(cursor.getColumnIndex(MoviesDetailsEntry.COLUMN_ADULT)) == 1) ? true : false;
        String backdropPath = cursor.getString(cursor.getColumnIndex(MoviesDetailsEntry.COLUMN_BACKDROP_PATH));
        long budget = cursor.getLong(cursor.getColumnIndex(MoviesDetailsEntry.COLUMN_BUDGET));
        String homepage = cursor.getString(cursor.getColumnIndex(MoviesDetailsEntry.COLUMN_HOMEPAGE));
        String imdbId = cursor.getString(cursor.getColumnIndex(MoviesDetailsEntry.COLUMN_IMDB_ID));
        String originalLanguage = cursor.getString(cursor.getColumnIndex(MoviesDetailsEntry.COLUMN_ORIGINAL_LANGUAGE));
        String originalTitle = cursor.getString(cursor.getColumnIndex(MoviesDetailsEntry.COLUMN_ORIGINAL_TITLE));
        String overview = cursor.getString(cursor.getColumnIndex(MoviesDetailsEntry.COLUMN_OVERVIEW));
        double popularity = cursor.getDouble(cursor.getColumnIndex(MoviesDetailsEntry.COLUMN_POPULARITY));
        String posterPath = cursor.getString(cursor.getColumnIndex(MoviesDetailsEntry.COLUMN_POSTER_PATH));
        String releaseDate = cursor.getString(cursor.getColumnIndex(MoviesDetailsEntry.COLUMN_RELEASE_DATE));
        long revenue = cursor.getLong(cursor.getColumnIndex(MoviesDetailsEntry.COLUMN_REVENUE));
        int runtime = cursor.getInt(cursor.getColumnIndex(MoviesDetailsEntry.COLUMN_RUNTIME));
        String status = cursor.getString(cursor.getColumnIndex(MoviesDetailsEntry.COLUMN_STATUS));
        String tagline = cursor.getString(cursor.getColumnIndex(MoviesDetailsEntry.COLUMN_TAGLINE));
        String title = cursor.getString(cursor.getColumnIndex(MoviesDetailsEntry.COLUMN_TITLE));
        double voteAverage = cursor.getDouble(cursor.getColumnIndex(MoviesDetailsEntry.COLUMN_VOTE_AVERAGE));
        long voteCount = cursor.getLong(cursor.getColumnIndex(MoviesDetailsEntry.COLUMN_VOTE_COUNT));
        byte[] cover = cursor.getBlob(cursor.getColumnIndex(MoviesDetailsEntry.COLUMN_COVER));
        double userRating = cursor.getDouble(cursor.getColumnIndex(MoviesDetailsEntry.COLUMN_USER_RATING));
        // Set values into MovieDetails object
        MovieDetails movieDetails = new MovieDetails();
        movieDetails.setId(id);
        movieDetails.setAdult(adult);
        movieDetails.setBackdropPath(backdropPath);
        movieDetails.setBudget(budget);
        movieDetails.setHomepage(homepage);
        movieDetails.setImdbId(imdbId);
        movieDetails.setOriginalLanguage(originalLanguage);
        movieDetails.setOriginalTitle(originalTitle);
        movieDetails.setOverview(overview);
        movieDetails.setPopularity(popularity);
        movieDetails.setPosterPath(posterPath);
        movieDetails.setReleaseDate(releaseDate);
        movieDetails.setRevenue(revenue);
        movieDetails.setRuntime(runtime);
        movieDetails.setStatus(status);
        movieDetails.setTagline(tagline);
        movieDetails.setTitle(title);
        movieDetails.setVoteAverage(voteAverage);
        movieDetails.setVoteCount(voteCount);
        movieDetails.setCover(cover);
        movieDetails.setUserRating(userRating);

        return movieDetails;
    }


    //endregion

}
