package info.houseofkim.movieproject.model;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class MovieInfosDBHelper extends SQLiteOpenHelper {
    public static final String LOG_TAG = MovieInfosDBHelper.class.getSimpleName();

    //name & version
    private static final String DATABASE_NAME = "MovieInfos.db";
    private static final int DATABASE_VERSION = 1;

    public MovieInfosDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // Create the database
    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        final String SQL_CREATE_MOVIE_TABLE = "CREATE TABLE " +
                MovieInfosContract.MovieInfoEntry.TABLE_MOVIEINFOS + "(" + MovieInfosContract.MovieInfoEntry._ID +
                " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                MovieInfosContract.MovieInfoEntry.COLUMN_MOVIEID + " INTEGER NOT NULL, " +
                MovieInfosContract.MovieInfoEntry.COLUMN_NAME + " TEXT NOT NULL, " +
                MovieInfosContract.MovieInfoEntry.COLUMN_RELEASEDATE + " TEXT NOT NULL, " +
                MovieInfosContract.MovieInfoEntry.COLUMN_IMAGE + " TEXT NOT NULL, " +
                MovieInfosContract.MovieInfoEntry.COLUMN_DESCRIPTION + " TEXT NOT NULL, " +
                MovieInfosContract.MovieInfoEntry.COLUMN_DURATION + " TEXT NOT NULL, " +
                MovieInfosContract.MovieInfoEntry.COLUMN_VERSION_NAME + " TEXT NOT NULL, " +
                MovieInfosContract.MovieInfoEntry.COLUMN_RATING + " FLOAT NOT NULL, " +
                MovieInfosContract.MovieInfoEntry.COLUMN_FAVORITE + " BOOL NOT NULL, " +
                MovieInfosContract.MovieInfoEntry.COLUMN_MOVIEID + " INTEGER NOT NULL);";


        sqLiteDatabase.execSQL(SQL_CREATE_MOVIE_TABLE);
    }

    // Upgrade database when version is changed.
    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        Log.w(LOG_TAG, "Upgrading database from version " + oldVersion + " to " +
                newVersion + ". OLD DATA WILL BE DESTROYED");
        // Drop the table
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + MovieInfosContract.MovieInfoEntry.TABLE_MOVIEINFOS);
        sqLiteDatabase.execSQL("DELETE FROM SQLITE_SEQUENCE WHERE NAME = '" +
                MovieInfosContract.MovieInfoEntry.TABLE_MOVIEINFOS + "'");

        // re-create database
        onCreate(sqLiteDatabase);
    }
}