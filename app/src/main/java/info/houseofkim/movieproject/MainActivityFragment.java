package info.houseofkim.movieproject;

import android.app.Activity;
import android.app.Fragment;
import android.app.LoaderManager;
import android.content.Intent;
import android.content.Loader;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.support.v4.content.AsyncTaskLoader;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import java.io.IOException;
import java.net.URL;
import java.util.Arrays;
import java.util.List;

import info.houseofkim.movieproject.model.MovieInfo;
import info.houseofkim.movieproject.model.MovieInfoAdapter;
import info.houseofkim.movieproject.utils.JsonUtils;
import info.houseofkim.movieproject.utils.MovieQueryTask;
import info.houseofkim.movieproject.utils.NetworkUtils;

public class MainActivityFragment extends Fragment implements MovieQueryTask.OnTaskCompleted {
    public static final String IMAGE_ID_LIST = "image_ids";
    public static final String LIST_INDEX = "list_index";

    private List<Integer> mImageIds;
    private int mListIndex;

    OnImageClickListener mCallback;
    View rootView ;
    private GridView gridView;
    public static MovieInfoAdapter movieAdapter;

    private static MovieQueryTask.OnTaskCompleted extTask;
    public static MovieQueryTask.OnTaskCompleted getTaskContext() {
        return extTask;
    }

    public void onTaskCompleted(MovieInfo[] response) {
        if (response != null) {
            refreshMovies(response);
        }
        else {
            Log.e("Response", "null");
        }
    }



    public interface OnImageClickListener {

        void onImageSelected (int position);
    }
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        // This makes sure that the host activity has implemented the callback interface
        // If not, it throws an exception
        try {
            mCallback = (OnImageClickListener) context;
        //    Log.e("onimageclick",context.toString());
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()
                    + " must implement OnImageClickListener");
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        if(savedInstanceState != null) {
            mImageIds = savedInstanceState.getIntegerArrayList(IMAGE_ID_LIST);
            mListIndex = savedInstanceState.getInt(LIST_INDEX);
        }
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);
        gridView = rootView.findViewById(R.id.movie_grid);

         final MovieInfo[]movieInfos = JsonUtils.parseCatalogMovieJSON(this.getContext(),
                this.getString(R.string.startup));
       // Log.e("Adapter1", Arrays.asList(movieInfos).toString());

        // Checking for network connectivity
        Context context=this.getContext();
        ConnectivityManager cm =
                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = null;
        if (cm != null) {
            activeNetwork = cm.getActiveNetworkInfo();
        }
        boolean isConnected = activeNetwork != null &&  activeNetwork.isConnectedOrConnecting();
        if (isConnected) {
            MovieQueryTask task = new MovieQueryTask(MainActivityFragment.this,context);
            task.execute(NetworkUtils.buildUrl("base",0));

        }

     //   Log.e("Adapter1", movieInfos.toString());
        movieAdapter = new MovieInfoAdapter(getActivity(), Arrays.asList(movieInfos));
    //Log.e("Adapter", movieAdapter.toString());

        gridView.setAdapter(movieAdapter);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                // Trigger the callback method and pass in the position that was clicked

                mCallback.onImageSelected(movieAdapter.getMovieId(position));
            }
        });
        extTask = (MovieQueryTask.OnTaskCompleted) MainActivityFragment.this;
        return rootView;
    }


    public void refreshMovies(MovieInfo[] movieInfos) {

        movieAdapter = new MovieInfoAdapter(getActivity(), Arrays.asList(movieInfos));
        gridView.setAdapter(movieAdapter);
        //gridView.getAdapter();
        movieAdapter.notifyDataSetChanged();
    }

}
    //Picasso.with(this)
//        .load(sandwich.getImage())
//        .into(ingredientsIv);