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
import android.os.IBinder;
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
import info.houseofkim.movieproject.model.MovieInfosProvider;
import info.houseofkim.movieproject.utils.JsonUtils;
import info.houseofkim.movieproject.utils.MovieFavoriteTask;
import info.houseofkim.movieproject.utils.MovieQueryTask;
import info.houseofkim.movieproject.utils.NetworkUtils;

public class MainActivityFragment extends Fragment implements MovieQueryTask.OnTaskCompleted, LoaderManager.LoaderCallbacks<Cursor>, MovieFavoriteTask.OnMFTaskCompleted {
    public static final String IMAGE_ID_LIST = "image_ids";
    public static final String LIST_INDEX = "list_index";

    private List<Integer> mImageIds;
    private int mListIndex;
private Context mContext;
    OnImageClickListener mCallback;


    private GridView gridView;
    public MovieInfoAdapter movieAdapter;

    private static MovieQueryTask.OnTaskCompleted extTask;
    private static MovieFavoriteTask.OnMFTaskCompleted extTaskMF;

    private static final int CURSOR_LOADER_ID = 0;



    public static MovieQueryTask.OnTaskCompleted getTaskContext() {
        return extTask;
    }

    public static MovieFavoriteTask.OnMFTaskCompleted getMFTaskContext() {
        return extTaskMF;
    }

    public void onTaskCompleted(MovieInfo[] response) {
        if (response != null) {
            // Log.e("Response", response.toString());
getActivity().getContentResolver().delete(MovieInfosContract.MovieInfoEntry.CONTENT_URI,null,null);
            insertData(response);
            getLoaderManager().initLoader(CURSOR_LOADER_ID, null, this);
            refreshMovies();
        } else {
            Log.e("Response", "null");
        }
    }




    @Override
    public void onMFTaskCompleted(MovieInfo[] response) {
        Log.d("LoadFavorite", "Completed");
        //   Log.e("LoadFavorite", String.valueOf(response.length));

        if (response != null) {
            getActivity().getContentResolver().delete(MovieInfosContract.MovieInfoEntry.CONTENT_URI,null,null);
            insertData(response);
            refreshMovies();
        } else {
            refreshMovies();
            Log.e("LoadFavorite", "null");
        }
    }


    public interface OnImageClickListener {

        void onImageSelected(int position);
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        // This makes sure that the host activity has implemented the callback interface
        // If not, it throws an exception
        try {
            mCallback = (OnImageClickListener) context;
         } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()
                    + " must implement OnImageClickListener");
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_main, container, false);
        gridView = rootView.findViewById(R.id.movie_grid);

        if (movieAdapter == null) {
            movieAdapter = new MovieInfoAdapter(getActivity(), null, 0, CURSOR_LOADER_ID);
          //  Log.d("MainActivityFragment", movieAdapter.toString());

        }

        gridView.setAdapter(movieAdapter);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                // Trigger the callback method and pass in the position that was clicked
                //movieAdapter.getMovieId(position)
                mCallback.onImageSelected(movieAdapter.getMovieId(position));
            }
        });
        extTask = MainActivityFragment.this;
        extTaskMF = MainActivityFragment.this;
      //  mContext = (Context) this;


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
            MovieInfo[] movieInfos = JsonUtils.parseCatalogMovieJSON(context,
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
        getLoaderManager().restartLoader(CURSOR_LOADER_ID,null,this);


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
        if (c != null && MainActivity.getViewId() != 2) {
            if (c.getCount() == 0) {
                StartMovieTaskExecute();


            }
            c.close();
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
