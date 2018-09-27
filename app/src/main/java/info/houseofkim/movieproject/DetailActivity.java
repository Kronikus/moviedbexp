package info.houseofkim.movieproject;

import android.app.LoaderManager;
import android.content.AsyncTaskLoader;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.Locale;

import info.houseofkim.movieproject.model.MovieInfo;
import info.houseofkim.movieproject.model.MovieInfoAdapter;
import info.houseofkim.movieproject.utils.JsonUtils;
import info.houseofkim.movieproject.utils.NetworkUtils;

public class DetailActivity extends  MainActivity implements LoaderManager.LoaderCallbacks <MovieInfo>{
    private static int current_movie;
    private TextView mViewMovieName;
    private TextView mViewMovieReleaseDate;
    private TextView mViewMovieDuration;
    private TextView mViewMovieRating;
    private TextView mViewMovieDescription;

    public static final String EXTRA_POSITION = "extra_position";
    private static final int DEFAULT_POSITION = -1;
    //public int current_movie=-1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detailactivity);

        mViewMovieName = findViewById(R.id.movie_name);
        mViewMovieDescription = findViewById(R.id.movie_description);
        mViewMovieDuration = findViewById(R.id.movie_duration);
        mViewMovieRating = findViewById(R.id.movie_rating);
        mViewMovieReleaseDate = findViewById(R.id.movie_releasedate);
        Intent intent = getIntent();
        if (intent == null) {
            closeOnError();
        }

        int position = intent.getIntExtra(EXTRA_POSITION, DEFAULT_POSITION);
        if (position == DEFAULT_POSITION ) {
            // EXTRA_POSITION not found in intent
            closeOnError();
            return;
        }
        current_movie = position;

       // MovieInfo movieInfo= getItem(position);
        Log.e("Detail movie url", String.valueOf(NetworkUtils.buildUrlSingleMovie(String.valueOf(position))));
        getLoaderManager().initLoader(0, null, this).forceLoad();
    }

    private void closeOnError() {
        finish();
        Toast.makeText(this, R.string.detail_error_message, Toast.LENGTH_SHORT).show();

    }


    @Override
    public Loader<MovieInfo> onCreateLoader(int i, Bundle bundle) {
        return  new GetMovieData(this);
    }

    @Override
    public void onLoadFinished(Loader<MovieInfo> loader, MovieInfo movieInfo) {
        mViewMovieName.setText(movieInfo.getMovieName());
        DateFormat df = new SimpleDateFormat("yyyy", Locale.US);
        mViewMovieReleaseDate.setText(String.format(movieInfo.getMovieReleaseDate(), df));
        mViewMovieDuration.setText(movieInfo.getMovieDuration());
        mViewMovieDescription.setText(movieInfo.getMovieDescription());
        mViewMovieRating.setText(String.valueOf(movieInfo.getMovieRating()));

        ImageView iconView =  findViewById(R.id.movie_pic);
        Picasso.with(this)
                .load(getString(R.string.moviedbimageurl)+movieInfo.getImage())
                .into(iconView);
       // Toast.makeText(this, "Position clicked = " + movieInfo.getMovieName(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onLoaderReset(Loader<MovieInfo> loader) {

    }
    private static class GetMovieData extends AsyncTaskLoader<MovieInfo> {


        GetMovieData(Context context) {
            super(context);
        }

        @Override
        public MovieInfo loadInBackground() {

            String movieJson = null;
            MovieInfo movieInfo = null;
            try {
                URL movie_url = NetworkUtils.buildUrlSingleMovie(String.valueOf(DetailActivity.current_movie));
                movieJson = NetworkUtils.getResponseFromHttpUrl(movie_url);

            }catch (IOException e) {
                e.printStackTrace();

            }

            if (movieJson != null && !movieJson.equals("")) {
                movieInfo = JsonUtils.parseMovieJSON(getContext(), movieJson , 0);
            }else
                {
                    Log.e("DetActonBackground", "No search results");
                }
            return movieInfo;
        }
    }
}
