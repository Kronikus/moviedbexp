package info.houseofkim.movieproject;

import android.app.LoaderManager;
import android.content.AsyncTaskLoader;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.Locale;

import info.houseofkim.movieproject.model.DetailReviewAdapter;
import info.houseofkim.movieproject.model.DetailVideoAdapter;
import info.houseofkim.movieproject.model.MovieInfo;
import info.houseofkim.movieproject.model.MovieInfosContract;
import info.houseofkim.movieproject.model.ReviewInfo;
import info.houseofkim.movieproject.model.VideoInfo;
import info.houseofkim.movieproject.utils.JsonUtils;
import info.houseofkim.movieproject.utils.NetworkUtils;

public class DetailActivity extends MainActivity implements LoaderManager.LoaderCallbacks<MovieInfo> {
    private static int current_movie;
    private TextView mViewMovieName;
    private TextView mViewMovieReleaseDate;
    private TextView mViewMovieDuration;
    private TextView mViewMovieRating;
    private TextView mViewMovieDescription;

    private ToggleButton mViewToggleButton;

    private RecyclerView videoView;
    private RecyclerView reviewView;
    private FrameLayout mLoadingIndicator;

    public static final String EXTRA_POSITION = "extra_position";
    private static final int DEFAULT_POSITION = -1;
    //public int current_movie=-1;

    private MovieInfo mInfo;

    private DetailVideoAdapter videoAdapter;
    private DetailReviewAdapter reviewAdapter;
    private VideoInfo[] mVideosData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detailactivity);

        mViewMovieName = findViewById(R.id.movie_name);
        mViewMovieDescription = findViewById(R.id.movie_description);
        mViewMovieDuration = findViewById(R.id.movie_duration);
        mViewMovieRating = findViewById(R.id.movie_rating);
        mViewMovieReleaseDate = findViewById(R.id.movie_releasedate);

        mLoadingIndicator = findViewById(R.id.progressbar_layout);

        mViewToggleButton = findViewById(R.id.togglebutton);

        Intent intent = getIntent();
        if (intent == null) {
            closeOnError();
        }
        assert intent != null;
        int position = intent.getIntExtra(EXTRA_POSITION, DEFAULT_POSITION);
        if (position == DEFAULT_POSITION) {
            // EXTRA_POSITION not found in intent
            closeOnError();
            return;
        }
        current_movie = position;

        mViewToggleButton.setChecked(checkMovieFavorite(current_movie));

        mLoadingIndicator.setVisibility(View.VISIBLE);
        //MovieInfo movieInfo = MainActivityFragment.getAdapter().getItem(position);
        //populateUI(movieInfo);
        //MovieInfo movieInfo= getItem(position);
        Log.d("Detail movie url", String.valueOf(NetworkUtils.buildUrlSingleMovie(String.valueOf(position))));
        getLoaderManager().initLoader(0, null, this).forceLoad();

        VideoInfo[] videoInfo = new VideoInfo[]{new VideoInfo("", "", "")};
        videoView = findViewById(R.id.video_list_main);
        videoView.setLayoutManager(new LinearLayoutManager(this));
        videoAdapter = new DetailVideoAdapter(this, Arrays.asList(videoInfo));
        videoView.setAdapter(videoAdapter);

        ReviewInfo[] reviewInfos = new ReviewInfo[]{new ReviewInfo("", "", "", "")};
        reviewView = findViewById(R.id.review_list_main);
        reviewView.setLayoutManager(new LinearLayoutManager(this));
        reviewAdapter = new DetailReviewAdapter(this, Arrays.asList(reviewInfos));
        reviewView.setAdapter(reviewAdapter);
    }

    private void closeOnError() {
        finish();
        Toast.makeText(this, R.string.detail_error_message, Toast.LENGTH_SHORT).show();

    }


    @Override
    public Loader<MovieInfo> onCreateLoader(int i, Bundle bundle) {
        mLoadingIndicator.setVisibility(View.VISIBLE);
        return new GetMovieData(this);
    }


    @Override
    public void onLoadFinished(Loader<MovieInfo> loader, MovieInfo movieInfo) {
        //Log.e("DetailActivityInfo",String.valueOf(movieInfo));
        populateUI(movieInfo);

        mInfo = movieInfo;

        videoAdapter.updateData(movieInfo.getMovieTrailer2());

        reviewAdapter.updateData(movieInfo.getMovieReview2());
        // Toast.makeText(this, "Position clicked = " + movieInfo.getMovieName(), Toast.LENGTH_SHORT).show();
    }

    private void populateUI(MovieInfo movieInfo) {
        mViewMovieName.setText(movieInfo.getMovieName());
        // DateFormat df = new SimpleDateFormat("yyyy", Locale.US);
        mViewMovieReleaseDate.setText(getReleaseDateYear(movieInfo.getMovieReleaseDate()));
        mViewMovieDuration.setText(movieInfo.getMovieDuration());
        mViewMovieDescription.setText(movieInfo.getMovieDescription());
        mViewMovieRating.setText(String.valueOf(movieInfo.getMovieRating()));

        ImageView iconView = findViewById(R.id.movie_pic);
        Picasso.with(this)
                .load(getString(R.string.moviedbimageurl) + movieInfo.getImage())
                .error(R.mipmap.picasso_error)
                .into(iconView);

        ScrollView scrollView = findViewById(R.id.detail_activity_scrollview);
        scrollView.fullScroll(ScrollView.FOCUS_UP);
        mLoadingIndicator.setVisibility(View.INVISIBLE);
    }


    @Override
    public void onLoaderReset(Loader<MovieInfo> loader) {

    }

    public void AddFavoriteClick(View view) {
        // Loop through static array of Flavors, add each to an instance of ContentValues
        // in the array of ContentValues

        if (checkMovieFavorite(mInfo.getMovieId())) {
            // remove from favorite
            Uri uri = MovieInfosContract.MovieFavorite.CONTENT_URI;
            Uri uri2 = MovieInfosContract.MovieInfoEntry.CONTENT_URI;
            String selection = MovieInfosContract.MovieInfoEntry.COLUMN_MOVIEID;
            String[] selectionId = new String[]{String.valueOf(mInfo.getMovieId())};
            if (getContentResolver() != null) {
                getContentResolver().delete(uri, selection + "= ?", selectionId);
                //   Log.e("Unfavorite",String.valueOf(VIEW__ID));
                //  Log.e("Unfavorite",String.valueOf(getViewId()));
                if (getViewId() == 2) {
                    getContentResolver().delete(uri2, selection + "= ?", selectionId);

                }
                Toast.makeText(this, "Removed from favorites", Toast.LENGTH_SHORT).show();

            } else {
                Toast.makeText(this, "Error can't get database", Toast.LENGTH_SHORT).show();

            }
        } else {
            ContentValues movieFavoriteValue = new ContentValues();
            movieFavoriteValue.put(MovieInfosContract.MovieFavorite.COLUMN_MOVIEID, mInfo.getMovieId());

            movieFavoriteValue.put(MovieInfosContract.MovieFavorite.COLUMN_NAME, mInfo.getMovieName());
            movieFavoriteValue.put(MovieInfosContract.MovieFavorite.COLUMN_RELEASEDATE, mInfo.getMovieReleaseDate());
            movieFavoriteValue.put(MovieInfosContract.MovieFavorite.COLUMN_IMAGE, mInfo.getImage());
            movieFavoriteValue.put(MovieInfosContract.MovieFavorite.COLUMN_DESCRIPTION, mInfo.getMovieDescription());
            movieFavoriteValue.put(MovieInfosContract.MovieFavorite.COLUMN_DURATION, mInfo.getMovieDuration());
            movieFavoriteValue.put(MovieInfosContract.MovieFavorite.COLUMN_RATING, mInfo.getMovieRating());
            movieFavoriteValue.put(MovieInfosContract.MovieFavorite.COLUMN_POPULARITY, mInfo.getMoviePopularity());
            movieFavoriteValue.put(MovieInfosContract.MovieFavorite.COLUMN_FAVORITE, true);
            //   Log.e("InsertData",String.valueOf(movieValuesArr[i]));
            if (getContentResolver() != null) {
                getContentResolver().insert(MovieInfosContract.MovieFavorite.CONTENT_URI, movieFavoriteValue);
                Toast.makeText(this, "Added to favorites", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private boolean checkMovieFavorite(int movieid) {
        Log.d("checkFavoritesMovieId", String.valueOf(movieid));
        Uri uri = MovieInfosContract.MovieFavorite.CONTENT_URI;
        String selection = MovieInfosContract.MovieFavorite.COLUMN_MOVIEID;
        String[] selectionId = new String[]{String.valueOf(movieid)};
        String[] resultView = new String[]{MovieInfosContract.MovieInfoEntry.COLUMN_MOVIEID};
        Cursor c = getContentResolver().query(uri, resultView, selection + "= ?", selectionId, null);
        int movid = 0;
        Log.d("checkFavoritesMovieId", String.valueOf(c));
        if (c != null) {
            if (c.getCount() != 0) {

                Log.d("checkFavoritesMovieId", String.valueOf(c.getCount()));
                c.close();
                return true;
            }
        }
        return false;
    }

    private static class GetMovieData extends AsyncTaskLoader<MovieInfo> {


        GetMovieData(Context context) {
            super(context);
        }

        @Override
        public MovieInfo loadInBackground() {

            String movieJson = null;
            String videoJSON = null;
            String reviewJson = null;
            MovieInfo movieInfo = null;
            VideoInfo[] videos ;
            ReviewInfo[] reviews ;
            try {
                URL movie_url = NetworkUtils.buildUrlSingleMovie(String.valueOf(DetailActivity.current_movie));
                movieJson = NetworkUtils.getResponseFromHttpUrl(movie_url);
                //          Log.e("DetailLoadFinished",String.valueOf(movieJson));
//                movieJson = MainActivityFragment.mo
                URL video_url = NetworkUtils.buildUrlFetchMovieVideos(String.valueOf(DetailActivity.current_movie));
                videoJSON = NetworkUtils.getResponseFromHttpUrl(video_url);

                URL review_url = NetworkUtils.buildUrlFetchMovieReviews(String.valueOf(DetailActivity.current_movie));

                reviewJson = NetworkUtils.getResponseFromHttpUrl(review_url);

            } catch (IOException e) {
                e.printStackTrace();

            }

            if (movieJson != null && !movieJson.equals("")) {

                movieInfo = JsonUtils.parseMovieJSON(getContext(), movieJson, 0);

                videos = JsonUtils.parseArrayVideosJSON(getContext(), videoJSON);
                movieInfo.setMovieTrailer2(videos);
                movieInfo.setMovieTrailer1(videoJSON);

                reviews = JsonUtils.parseArrayReviewsJSON(getContext(), reviewJson);
                movieInfo.setMovieReview1(reviewJson);
                movieInfo.setMoviewReview2(reviews);

            } else {
                Log.e("DetActonBackground", "No search results");
            }
            return movieInfo;
        }

    }

    public String getReleaseDateYear(String inputdate) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
        Date date = null;
        try {
            date = format.parse(inputdate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        SimpleDateFormat df = new SimpleDateFormat("yyyy", Locale.US);
        return df.format(date);
    }

}
