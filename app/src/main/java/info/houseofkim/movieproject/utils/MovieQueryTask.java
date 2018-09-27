package info.houseofkim.movieproject.utils;

import android.os.AsyncTask;
import android.util.Log;

import java.io.IOException;
import java.net.URL;
import java.util.Arrays;

import info.houseofkim.movieproject.MainActivityFragment;
import info.houseofkim.movieproject.model.MovieInfo;
import info.houseofkim.movieproject.model.MovieInfoAdapter;

import android.content.Context;
import android.widget.GridView;
import android.widget.Toast;

import info.houseofkim.movieproject.MainActivityFragment;



public  class MovieQueryTask extends AsyncTask<URL, Void, String> {
   // private MovieInfoAdapter movieAdapter;
    private Context context;
    public interface OnTaskCompleted {
        void onTaskCompleted(MovieInfo[] response);
    }
    private OnTaskCompleted taskCompleted;
    public MovieQueryTask (OnTaskCompleted activityContext,Context mainContext){
            this.taskCompleted = activityContext;
            this.context = mainContext;
        }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        //mLoadingIndicator.setVisibility(View.VISIBLE);
        Log.e("Task","started");
    }

    @Override
    protected String doInBackground(URL... params) {
        URL searchUrl = params[0];
        String movieSearchResults = null;


        try {
            movieSearchResults = NetworkUtils.getResponseFromHttpUrl(searchUrl);
            Log.e("Catalog",searchUrl.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return movieSearchResults;
    }

    @Override
    protected void onPostExecute(String movieSearchResults) {
        Log.e("Task","finished");
        if (movieSearchResults != null && !movieSearchResults.equals("")) {
            MovieInfo[] movieInfos = JsonUtils.parseCatalogMovieJSON(context, movieSearchResults );

            taskCompleted.onTaskCompleted(movieInfos);
//
        } else {
            Log.e("onpostexecute", "No search results");
            //  showErrorMessage();
        }
    }
}
