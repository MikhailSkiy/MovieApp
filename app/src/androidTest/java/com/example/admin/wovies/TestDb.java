package com.mikhailskiy.www.wovies;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.test.AndroidTestCase;

import com.mikhailskiy.www.wovies.database.Contract;
import com.mikhailskiy.www.wovies.database.DbHelper;

/**
 * Created by Mikhail Valuyskiy on 14.09.2015.
 */
public class TestDb extends AndroidTestCase {

    private static final int MOVIE_ID = 76341;
    private static final boolean MOVIE_ADULT = false;
    private static final String MOVIE_ORIGINAL_TITLE = "Mad Max: Fury Road";
    private static final String MOVIE_OVERVIEW = "An apocalyptic story set in the furthest reaches of our planet, in a stark desert landscape where humanity is broken, and most everyone is crazed fighting for the necessities of life. Within this world exist two rebels on the run who just might be able to restore order. There's Max, a man of action and a man of few words, who seeks peace of mind following the loss of his wife and child in the aftermath of the chaos. And Furiosa, a woman of action and a woman who believes her path to survival may be achieved if she can make it across the desert back to her childhood homeland.";
    private static final String MOVIE_RELEASE_DATE = "2015-05-15";
    private static final String MOVIE_POSTER_PATH = "/kqjL17yufvn9OVLyXYpvtyrFfak.jpg";
    private static final String MOVIE_TITLE = "Mad Max: Fury Road";
    private static final boolean MOVIE_VIDEO = false;
    private static final double MOVIE_VOTE_AVERAGE = 7.6;
    private static final int MOVIE_VOTE_COUNT = 2187;

    private DbHelper helper_;
    private SQLiteDatabase database_;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        helper_ = new DbHelper(mContext);
        database_ = helper_.getWritableDatabase();
    }

    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
        helper_.close();
        helper_.deleteDatabase(mContext);
    }

    public void testDatabaseCreation() throws Throwable {
        mContext.deleteDatabase(DbHelper.DATABASE_NAME);
        SQLiteDatabase database = new DbHelper(mContext).getWritableDatabase();
        assertEquals(true,database.isOpen());
        database.close();
    }

    public void testMovieInsert(){
        ContentValues movie = createMovieValues();
        long movieId = database_.insert(Contract.MoviesEntry.TABLE_NAME,null,movie);

        assertTrue(movieId != -1);

        Cursor cursor = database_.query(
                Contract.MoviesEntry.TABLE_NAME,
                null,
                null,
                null,
                null,
                null,
                null
        );

        if (!cursor.moveToFirst()){
            fail("No data returned");
        }
        try {
            assertEquals(cursor.getInt(cursor.getColumnIndex(Contract.MoviesEntry._ID)),MOVIE_ID);

            boolean isAdult = cursor.getInt(cursor.getColumnIndex(Contract.MoviesEntry.COLUMN__ADULT))==1?true:false;
            assertEquals(isAdult,MOVIE_ADULT);

            assertEquals(cursor.getString(cursor.getColumnIndex(Contract.MoviesEntry.COLUMN_ORIGINAL_TITLE)),MOVIE_ORIGINAL_TITLE);
            assertEquals(cursor.getString(cursor.getColumnIndex(Contract.MoviesEntry.COLUMN_OVERVIEW)),MOVIE_OVERVIEW);
            assertEquals(cursor.getString(cursor.getColumnIndex(Contract.MoviesEntry.COLUMN_RELEASE_DATE)),MOVIE_RELEASE_DATE);
            assertEquals(cursor.getString(cursor.getColumnIndex(Contract.MoviesEntry.COLUMN_POSTER_PATH)),MOVIE_POSTER_PATH);
            assertEquals(cursor.getString(cursor.getColumnIndex(Contract.MoviesEntry.COLUMN_TITLE)),MOVIE_TITLE);

            boolean isVideo = cursor.getInt(cursor.getColumnIndex(Contract.MoviesEntry.COLUMN_VIDEO))==1?true:false;
            assertEquals(isVideo,MOVIE_VIDEO);

            assertEquals(cursor.getDouble(cursor.getColumnIndex(Contract.MoviesEntry.COLUMN_VOTE_AVERAGE)),MOVIE_VOTE_AVERAGE);
            assertEquals(cursor.getInt(cursor.getColumnIndex(Contract.MoviesEntry.COLUMN_VOTE_COUNT)),MOVIE_VOTE_COUNT);
        } finally {
            cursor.close();
        }
    }

    private ContentValues createMovieValues(){
        ContentValues movieValues = new ContentValues();
        movieValues.put(Contract.MoviesEntry._ID,MOVIE_ID);
        if (MOVIE_ADULT) {
            movieValues.put(Contract.MoviesEntry.COLUMN__ADULT,1);
        } else {
            movieValues.put(Contract.MoviesEntry.COLUMN__ADULT,0);
        }

        movieValues.put(Contract.MoviesEntry.COLUMN_ORIGINAL_TITLE,MOVIE_ORIGINAL_TITLE);
        movieValues.put(Contract.MoviesEntry.COLUMN_OVERVIEW,MOVIE_OVERVIEW);
        movieValues.put(Contract.MoviesEntry.COLUMN_RELEASE_DATE,MOVIE_RELEASE_DATE);
        movieValues.put(Contract.MoviesEntry.COLUMN_POSTER_PATH,MOVIE_POSTER_PATH);
        movieValues.put(Contract.MoviesEntry.COLUMN_TITLE,MOVIE_TITLE);

        if (MOVIE_VIDEO){
            movieValues.put(Contract.MoviesEntry.COLUMN_VIDEO,1);
        } else {
            movieValues.put(Contract.MoviesEntry.COLUMN_VIDEO,0);
        }

        movieValues.put(Contract.MoviesEntry.COLUMN_VOTE_AVERAGE,MOVIE_VOTE_AVERAGE);
        movieValues.put(Contract.MoviesEntry.COLUMN_VOTE_COUNT,MOVIE_VOTE_COUNT);

        return movieValues;

    }

}
