package com.example.admin.moviesapp.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.admin.moviesapp.database.Contract.MoviesEntry;
import com.example.admin.moviesapp.helpers.Util;
import com.example.admin.moviesapp.models.Cast;
import com.example.admin.moviesapp.models.CastDetails;
import com.example.admin.moviesapp.models.Movie;

import java.util.ArrayList;
import java.util.List;

import static com.example.admin.moviesapp.database.Contract.*;

/**
 * Created by Mikhail Valuyskiy on 14.09.2015.
 */
public class DbHelper extends SQLiteOpenHelper {

    // When the database schema was changed, you must increment the database version
    private static final int DATABASE_VERSION = 1;
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
                CastEntry.COLUMN_COVER + " BLOB);";

        final String SQL_CREATE_TRAILER_TABLE = "CREATE TABLE " + TrailersEntry.TABLE_NAME + " (" +
                TrailersEntry._ID + " INTEGER PRIMARY KEY, " +
                TrailersEntry.COLUMN_CODE + " TEXT, " +
                TrailersEntry.COLUMN_KEY + " TEXT, " +
                TrailersEntry.COLUMN_NAME + " TEXT, " +
                TrailersEntry.COLUMN_SITE + " SITE, " +
                TrailersEntry.COLUMN_SIZE + " SIZE, " +
                TrailersEntry.COLUMN_TYPE + " TYPE);";

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + MoviesEntry.TABLE_NAME);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + CastEntry.TABLE_NAME);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + CastDetailsEntry.TABLE_NAME);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TrailersEntry.TABLE_NAME);
        onCreate(sqLiteDatabase);
    }

    public void deleteDatabase(Context context) {
        context.deleteDatabase(DbHelper.DATABASE_NAME);
    }

    public List<Movie> getAllMovies(){
        SQLiteDatabase database = this.getWritableDatabase();
        List<Movie> movieList= new ArrayList<>();
        String selectQuery = " SELECT * FROM " + MoviesEntry.TABLE_NAME;
        Cursor cursor = database.rawQuery(selectQuery,null);

        if (cursor.moveToFirst()){
            do {
                Movie movie = getMovieFromCursor(cursor);
                movieList.add(movie);
            } while (cursor.moveToNext());
        }
        database.close();

        return movieList;
    }

    private Movie getMovieFromCursor(Cursor cursor){
        int id = cursor.getInt(cursor.getColumnIndex(MoviesEntry._ID));
        boolean adult = (cursor.getInt(cursor.getColumnIndex(MoviesEntry.COLUMN__ADULT)) == 1) ? true :false;
        String originalTitle = cursor.getString(cursor.getColumnIndex(MoviesEntry.COLUMN_ORIGINAL_TITLE));
        String overview = cursor.getString(cursor.getColumnIndex(MoviesEntry.COLUMN_OVERVIEW));
        String releasedDate = Util.getDateFromUnix(cursor.getLong(cursor.getColumnIndex(MoviesEntry.COLUMN_RELEASE_DATE)));
        String posterPath = cursor.getString(cursor.getColumnIndex(MoviesEntry.COLUMN_POSTER_PATH));
        String title = cursor.getString(cursor.getColumnIndex(MoviesEntry.COLUMN_TITLE));
        boolean video = (cursor.getInt(cursor.getColumnIndex(MoviesEntry.COLUMN_VIDEO)) == 1) ? true :false;
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

    public void insertMovie(Movie movie) {
        if (movie != null) {
            SQLiteDatabase database = this.getWritableDatabase();
            ContentValues values = insertMovieInContentValues(movie);
            database.insert(MoviesEntry.TABLE_NAME, null, values);
            database.close();
        } else {
            throw new IllegalArgumentException("Passed movie object is null");
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
        values.put(MoviesEntry.COLUMN_VOTE_AVERAGE,movie.getVoteAverage());
        values.put(MoviesEntry.COLUMN_VOTE_COUNT,movie.getVoteCount());

        return values;
    }

}
