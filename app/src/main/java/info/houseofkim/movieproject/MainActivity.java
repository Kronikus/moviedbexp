package info.houseofkim.movieproject;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.facebook.stetho.Stetho;

import info.houseofkim.movieproject.utils.MovieFavoriteTask;
import info.houseofkim.movieproject.utils.MovieQueryTask;
import info.houseofkim.movieproject.utils.NetworkUtils;

public class MainActivity extends AppCompatActivity implements MainActivityFragment.OnImageClickListener {

    public static final int LOADER_ID = 55;
    public static int VIEW__ID = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (savedInstanceState != null) {
            VIEW__ID = savedInstanceState.getInt(getString(R.string.viewid), 0);
        }
    }
    //
    public void onImageSelected(int position) {
        //  Log.e("image", String.valueOf(position));
        Context context = this;
        Class destinationClass = DetailActivity.class;
        Intent intentToStartDetailActivity = new Intent(context, destinationClass);
        intentToStartDetailActivity.putExtra(DetailActivity.EXTRA_POSITION, position);

        startActivity(intentToStartDetailActivity);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        /* Use AppCompatActivity's method getMenuInflater to get a handle on the menu inflater */
        MenuInflater inflater = getMenuInflater();
        /* Use the inflater's inflate method to inflate our menu layout to this menu */
        inflater.inflate(R.menu.main_menu, menu);
        /* Return true so that the menu is displayed in the Toolbar */
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_popular) {
            MovieTaskExecute(0);
            VIEW__ID = 0;
            return true;
        }
        if (id == R.id.action_toprated) {
            MovieTaskExecute(1);
            VIEW__ID = 1;
            return true;
        }
        if (id == R.id.action_favorite) {
            MovieFavoriteTask task = new MovieFavoriteTask(MainActivityFragment.getMFTaskContext(), this);
            task.execute();
            VIEW__ID = 2;
            return true;
        }
        //  if (id == R.id.app_bar_search){}

        return super.onOptionsItemSelected(item);
    }

    private void MovieTaskExecute(int i) {
        Context context = this;
        ConnectivityManager cm =
                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = null;
        if (cm != null) {
            activeNetwork = cm.getActiveNetworkInfo();
        }
        boolean isConnected = activeNetwork != null && activeNetwork.isConnectedOrConnecting();
        if (isConnected) {
            MovieQueryTask task = new MovieQueryTask(MainActivityFragment.getTaskContext(), context);
            task.execute(NetworkUtils.buildUrl("base", i));

        }
    }

    public static int getViewId() {
        return VIEW__ID;
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putInt(getString(R.string.viewid), VIEW__ID);
        super.onSaveInstanceState(outState);
    }
}
