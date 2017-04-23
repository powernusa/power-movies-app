package com.powernusa.andy.powermovies.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by andy on 22/04/2017.
 */

public class MoviesDBHelper extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 2;
    private static final String DATABASE_NAME = "movies.db";

    public MoviesDBHelper(Context context) {
        super(context,DATABASE_NAME,null,DATABASE_VERSION);

    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        final String SQL_CREATE_MOVIES_TABLE = "CREATE TABLE " + MovieContract.MovieEntry.TABLE_NAME
                + "(" +
                MovieContract.MovieEntry._ID + " INTEGER PRIMARY KEY," +
                MovieContract.MovieEntry.COLUMN_MOVIE_ID + " INTEGER NOT NULL," +
                MovieContract.MovieEntry.COLUMN_MOVIE_TITLE + " TEXT NOT NULL," +
                MovieContract.MovieEntry.COLUMN_MOVIE_POSTER_PATH + " TEXT NOT NULL," +
                MovieContract.MovieEntry.COLUMN_MOVIE_OVERVIEW + " TEXT NOT NULL," +
                MovieContract.MovieEntry.COLUMN_MOVIE_VOTE_AVERAGE + " TEXT NOT NULL," +
                MovieContract.MovieEntry.COLUMN_MOVIE_RELEASE_DATE + " TEXT NOT NULL," +
                MovieContract.MovieEntry.COLUMN_MOVIE_BACKDROP_PATH + " TEXT NOT NULL" +
                ");";
        db.execSQL(SQL_CREATE_MOVIES_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + MovieContract.MovieEntry.TABLE_NAME);
        onCreate(db);

    }
}
