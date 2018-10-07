package info.houseofkim.movieproject;

import android.app.Fragment;
import android.app.LoaderManager;
import android.content.ContentValues;
import android.content.Context;
import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

import java.util.List;

import info.houseofkim.movieproject.model.MovieInfo;
import info.houseofkim.movieproject.model.MovieInfoAdapter;
import info.houseofkim.movieproject.model.MovieInfosContract;
import info.houseofkim.movieproject.model.MovieInfosDBHelper;
import info.houseofkim.movieproject.utils.JsonUtils;
import info.houseofkim.movieproject.utils.MovieFavoriteTask;
import info.houseofkim.movieproject.utils.MovieQueryTask;
import info.houseofkim.movieproject.utils.NetworkUtils;

public class MainActivityFragment extends Fragment implements MovieQueryTask.OnTaskCompleted, LoaderManager.LoaderCallbacks<Cursor>, MovieFavoriteTask.OnMFTaskCompleted {
    public static final String IMAGE_ID_LIST = "image_ids";
    public static final String LIST_INDEX = "list_index";

    private List<Integer> mImageIds;
    private int mListIndex;

    OnImageClickListener mCallback;


    private GridView gridView;
    public MovieInfoAdapter movieAdapter;

    private static MovieQueryTask.OnTaskCompleted extTask;
    private static MovieFavoriteTask.OnMFTaskCompleted extTaskMF;

    private static final int CURSOR_LOADER_ID = 0;


//    public MainActivityFragment() {
//    }

    public static MovieQueryTask.OnTaskCompleted getTaskContext() {
        return extTask;
    }

    public static MovieFavoriteTask.OnMFTaskCompleted getMFTaskContext() {
        return extTaskMF;
    }

    public void onTaskCompleted(MovieInfo[] response) {
        if (response != null) {
            // Log.e("Response", response.toString());
            clearMovieInfosTable();
            insertData(response);
            refreshMovies();
            // refreshMovies(response);
        } else {
            Log.e("Response", "null");
        }
    }

    private void clearMovieInfosTable() {
        MovieInfosDBHelper mdbhelper = new MovieInfosDBHelper(getContext());
        mdbhelper.getWritableDatabase().delete(MovieInfosContract.MovieInfoEntry.TABLE_MOVIEINFOS, null, null);
        mdbhelper.resetId(mdbhelper.getWritableDatabase());
    }


    @Override
    public void onMFTaskCompleted(MovieInfo[] response) {
        Log.e("LoadFavorite", "Completed");
        //   Log.e("LoadFavorite", String.valueOf(response.length));

        if (response != null) {
            clearMovieInfosTable();
            Log.e("LoadFavorite", String.valueOf(response.toString()));
            insertData(response);
            refreshMovies();
        } else {
            Log.e("LoadFavorite", "null");
        }
    }


    public interface OnImageClickListener {

        void onImageSelected(int position);
    }

    @Override
    public void onPause() {

        super.onPause();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        // This makes sure that the host activity has implemented the callback interface
        // If not, it throws an exception
        try {
            mCallback = (OnImageClickListener) context;
            //  mMovieFavoriteCallback = (MovieFavoriteShow) context;
            //    Log.e("onimageclick",context.toString());
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()
                    + " must implement OnImageClickListener");
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {

        super.onSaveInstanceState(outState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            mImageIds = savedInstanceState.getIntegerArrayList(IMAGE_ID_LIST);
            mListIndex = savedInstanceState.getInt(LIST_INDEX);
        }
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);
        gridView = rootView.findViewById(R.id.movie_grid);


        // Log.e("Adapter1", Arrays.asList(movieInfos).toString());

        // Checking for network connectivity
        // MovieTaskExecute();


        //   Log.e("Adapter1", movieInfos.toString());
        if (movieAdapter == null) {
            movieAdapter = new MovieInfoAdapter(getActivity(), null, 0, CURSOR_LOADER_ID);
            //Log.e("Adapter", movieAdapter.toString());
        }
        gridView.setAdapter(movieAdapter);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                // Trigger the callback method and pass in the position that was clicked
                //movieAdapter.getMovieId(position)
                mCallback.onImageSelected(movieAdapter.getMovieId(position));
//                int uriId = position + 1;
//                // append Id to uri
//                Uri uri = ContentUris.withAppendedId(MovieInfosContract.MovieInfoEntry.CONTENT_URI,
//                        uriId);
//                // create fragment
//                DetailFragment detailFragment = DetailFragment.newInstance(uriId, uri);
//                getActivity().getSupportFragmentManager().beginTransaction()
//                        .replace(R.id.mo, detailFragment)
//                        .addToBackStack(null).commit();
            }
        });
        extTask = MainActivityFragment.this;
        extTaskMF = MainActivityFragment.this;

        return rootView;
    }

    private void StartMovieTaskExecute() {
        Context context = this.getContext();
        ConnectivityManager cm =
                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = null;
        if (cm != null) {
            activeNetwork = cm.getActiveNetworkInfo();
        }
        boolean isConnected = activeNetwork != null && activeNetwork.isConnectedOrConnecting();
        if (isConnected) {
            MovieQueryTask task = new MovieQueryTask(MainActivityFragment.this, context);
            task.execute(NetworkUtils.buildUrl("base", 0));

        } else {
            MovieInfo[] movieInfos = JsonUtils.parseCatalogMovieJSON(this.getContext(),
                    this.getString(R.string.startup));
            insertData(movieInfos);
        }
    }


    public void refreshMovies() {


        Cursor c =
                getActivity().getContentResolver().query(MovieInfosContract.MovieInfoEntry.CONTENT_URI,
                        null,
                        null,
                        null,
                        null);

        movieAdapter.swapCursor(c);
    }

    public MovieInfoAdapter getAdapter() {
        return movieAdapter;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        Cursor c =
                getActivity().getContentResolver().query(MovieInfosContract.MovieInfoEntry.CONTENT_URI,
                        new String[]{MovieInfosContract.MovieInfoEntry._ID},
                        null,
                        null,
                        null);
        if (c != null) {
            if (c.getCount() == 0) {
                StartMovieTaskExecute();
                c.close();
            }
        }
        // initialize loader
        getLoaderManager().initLoader(CURSOR_LOADER_ID, null, this);

        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new CursorLoader(getActivity(),
                MovieInfosContract.MovieInfoEntry.CONTENT_URI,
                null,
                null,
                null,
                null);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    // Set the cursor in our CursorAdapter once the Cursor is loaded
    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {

        movieAdapter.swapCursor(data);


    }

    // reset CursorAdapter on Loader Reset
    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        movieAdapter.swapCursor(null);
    }

    public void insertData(MovieInfo[] movieInfos) {

        ContentValues[] movieValuesArr = new ContentValues[movieInfos.length];
        // Loop through static array, add each to an instance of ContentValues
        // in the array of ContentValues
        for (int i = 0; i < movieValuesArr.length; i++) {

            movieValuesArr[i] = new ContentValues();
            movieValuesArr[i].put(MovieInfosContract.MovieInfoEntry.COLUMN_MOVIEID, movieInfos[i].getMovieId());
            movieValuesArr[i].put(MovieInfosContract.MovieInfoEntry.COLUMN_IMAGE, movieInfos[i].getImage());
            movieValuesArr[i].put(MovieInfosContract.MovieInfoEntry.COLUMN_DESCRIPTION,
                    movieInfos[i].getMovieDescription());
            //   Log.e("InsertData",String.valueOf(movieValuesArr[i]));

        }
        // bulkInsert our ContentValues array
        getActivity().getContentResolver().bulkInsert(MovieInfosContract.MovieInfoEntry.CONTENT_URI,
                movieValuesArr);
    }


}
