package info.houseofkim.movieproject.model;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.net.Uri;
import android.provider.BaseColumns;

public class MovieInfosContract {
    static final String CONTENT_AUTHORITY = "info.houseofkim.movieproject.app";

    private static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);


    public static final class MovieInfoEntry implements BaseColumns {
        // table name
        public static final String TABLE_MOVIEINFOS = "movieinfos";
        // columns
        public static final String _ID = "_id";
        public static final String COLUMN_MOVIEID = "movieID";
        static final String COLUMN_NAME = "name";
        static final String COLUMN_RELEASEDATE = "releasedate";
        public static final String COLUMN_IMAGE = "image";
        public static final String COLUMN_DESCRIPTION = "description";
        static final String COLUMN_DURATION = "duration";
        static final String COLUMN_RATING = "rating";
        static final String COLUMN_POPULARITY = "popularity";
        static final String COLUMN_FAVORITE = "favorite";
        static final String COLUMN_VERSION_NAME = "version";

        // create content uri
        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon()
                .appendPath(TABLE_MOVIEINFOS).build();
        // create cursor of base type directory for multiple entries
        static final String CONTENT_DIR_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + TABLE_MOVIEINFOS;
        // create cursor of base type item for single entry
        static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + TABLE_MOVIEINFOS;

        // for building URIs on insertion
        static Uri buildMovieInfosUri(long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }
    }


    public static final class MovieFavorite implements BaseColumns {
        // table name
        static final String TABLE_MOVIEFAVORITE = "moviefavorite";
        // columns
        static final String _ID = "_id";
        public static final String COLUMN_MOVIEID = "movieID";
        public static final String COLUMN_NAME = "name";
        public static final String COLUMN_RELEASEDATE = "releasedate";
        public static final String COLUMN_IMAGE = "image";
        public static final String COLUMN_DESCRIPTION = "description";
        public static final String COLUMN_DURATION = "duration";
        public static final String COLUMN_RATING = "rating";
        public static final String COLUMN_POPULARITY = "popularity";
        public static final String COLUMN_FAVORITE = "favorite";
        // create content uri
        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon()
                .appendPath(TABLE_MOVIEFAVORITE).build();
        // create cursor of base type directory for multiple entries
        static final String CONTENT_DIR_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + TABLE_MOVIEFAVORITE;
        // create cursor of base type item for single entry
        static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + TABLE_MOVIEFAVORITE;

        // for building URIs on insertion
        public static Uri buildMovieInfosUri(long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }
    }
}
