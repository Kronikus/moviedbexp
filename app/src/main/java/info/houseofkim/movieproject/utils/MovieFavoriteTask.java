package info.houseofkim.movieproject.utils;

import android.content.AsyncTaskLoader;
import android.content.Context;
import android.database.Cursor;
import android.os.AsyncTask;
import android.util.Log;

import java.util.List;
import java.util.Objects;

import info.houseofkim.movieproject.model.MovieInfo;
import info.houseofkim.movieproject.model.MovieInfosContract;


public class MovieFavoriteTask extends AsyncTask<Void,Void,MovieInfo[]> {
    private static final String LOG_MOVIE_FAVORITE_TASK = "LoadFavorite";
    private Context mContext;
    private OnMFTaskCompleted taskCompleted;

    public interface OnMFTaskCompleted {
        void onMFTaskCompleted(MovieInfo[] response);
    }
    public MovieFavoriteTask(OnMFTaskCompleted activityContext, Context context) {
        this.taskCompleted = activityContext;
        mContext = context;
    }


    private MovieInfo[] getMovieFavorites(){
        Log.e(LOG_MOVIE_FAVORITE_TASK,"Started");
        MovieInfo[] res = null;
        Cursor c =
                mContext.getContentResolver().query(MovieInfosContract.MovieFavorite.CONTENT_URI,
                        null,
                        null,
                        null,
                        null);
        if (c != null) {
            if (c.getCount() == 0) {
                c.close();
            } else {
                res = new MovieInfo[c.getCount()];
                c.moveToFirst();
                Log.e(LOG_MOVIE_FAVORITE_TASK,String.valueOf(c));
              for (int i=0;i<c.getCount(); i++){
                    // required fields cannot be null
                    int movieId = c.getInt(c.getColumnIndex(MovieInfosContract.MovieFavorite.COLUMN_MOVIEID));
                    String movieImage = c.getString(c.getColumnIndex(MovieInfosContract.MovieFavorite.COLUMN_IMAGE));
                    //optional fields can be empty
                    String movieName = c.getString(c.getColumnIndex(MovieInfosContract.MovieFavorite.COLUMN_NAME));
                    String movieReleaseDate = c.getString(c.getColumnIndex(MovieInfosContract.MovieFavorite.COLUMN_IMAGE));
                    String movieDescription = c.getString(c.getColumnIndex(MovieInfosContract.MovieFavorite.COLUMN_IMAGE));
                    String movieDuration = c.getString(c.getColumnIndex(MovieInfosContract.MovieFavorite.COLUMN_IMAGE));
                    double movieRating = c.getDouble(c.getColumnIndex(MovieInfosContract.MovieFavorite.COLUMN_IMAGE));
                    double moviePopularity = c.getDouble(c.getColumnIndex(MovieInfosContract.MovieFavorite.COLUMN_IMAGE));
                    res[i] = new MovieInfo(movieId, movieName, movieReleaseDate, movieImage, movieDescription, movieDuration, movieRating, moviePopularity);
                    c.moveToNext();
                }
                c.close();
            }
        }
        Log.e(LOG_MOVIE_FAVORITE_TASK,String.valueOf(res));
        return res;

    }

      @Override
    protected MovieInfo[] doInBackground(Void... voids) {
        return  getMovieFavorites();
    }

    @Override
    protected void onPostExecute(MovieInfo[] movieInfos) {
        Log.d("Task","finished");
        if (movieInfos != null && movieInfos.length != 0) {

            taskCompleted.onMFTaskCompleted(movieInfos);
//
        } else {
            Log.e("OnMFTask", "No Favorites");
            //  showErrorMessage();
        }
    }
}
