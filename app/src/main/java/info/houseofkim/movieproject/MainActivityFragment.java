package info.houseofkim.movieproject;

import android.app.Fragment;
import android.app.LoaderManager;
import android.content.Loader;
import android.os.AsyncTask;
import android.support.v4.content.AsyncTaskLoader;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

import java.io.IOException;
import java.net.URL;
import java.util.Arrays;
import java.util.List;

import info.houseofkim.movieproject.model.MovieInfo;
import info.houseofkim.movieproject.model.MovieInfoAdapter;
import info.houseofkim.movieproject.utils.JsonUtils;
import info.houseofkim.movieproject.utils.NetworkUtils;

public class MainActivityFragment extends Fragment  {
    public static final String IMAGE_ID_LIST = "image_ids";
    public static final String LIST_INDEX = "list_index";

    private List<Integer> mImageIds;
    private int mListIndex;

    OnImageClickListener mCallback;
    View rootView ;
    GridView gridView;
    MovieInfoAdapter movieAdapter;
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

    public MainActivityFragment() {
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

         final MovieInfo[]movieInfos = JsonUtils.parseStartingMovieJSON(this.getContext(),
                this.getString(R.string.startup));
       // Log.e("Adapter1", Arrays.asList(movieInfos).toString());
       new MovieQueryTask().execute(NetworkUtils.buildUrl("base"));

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

        return rootView;
    }

    public class MovieQueryTask extends AsyncTask<URL, Void, String> {

        // COMPLETED (26) Override onPreExecute to set the loading indicator to visible
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
           //mLoadingIndicator.setVisibility(View.VISIBLE);
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
            if (movieSearchResults != null && !movieSearchResults.equals("")) {
                MovieInfo[] movieInfos = JsonUtils.parseStartingMovieJSON(getContext(), movieSearchResults );


                movieAdapter = new MovieInfoAdapter(getActivity(), Arrays.asList(movieInfos));
                gridView.setAdapter(movieAdapter);
                movieAdapter.notifyDataSetChanged();

            } else {
                Log.e("onpostexecute", "No search results");
              //  showErrorMessage();
            }
        }
    }

}
    //Picasso.with(this)
//        .load(sandwich.getImage())
//        .into(ingredientsIv);