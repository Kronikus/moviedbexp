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

public class MainActivity extends AppCompatActivity implements MainActivityFragment.OnImageClickListener{

    public static final int LOADER_ID = 55;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Stetho.initialize(
                Stetho.newInitializerBuilder(this)
                        .enableDumpapp(
                                Stetho.defaultDumperPluginsProvider(this))
                        .enableWebKitInspector(
                                Stetho.defaultInspectorModulesProvider(this))
                        .build());
    }
//
    public void onImageSelected (int position) {
      //  Log.e("image", String.valueOf(position));
        Context context = this;
        // COMPLETED (3) Remove the Toast and launch the DetailActivity using an explicit Intent
        Class destinationClass = DetailActivity.class;
        Intent intentToStartDetailActivity = new Intent(context, destinationClass);
        intentToStartDetailActivity.putExtra(DetailActivity.EXTRA_POSITION,position);

        startActivity(intentToStartDetailActivity);


        //Toast.makeText(this, "Position clicked = " + position, Toast.LENGTH_SHORT).show();
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

//        if (id == R.id.action_settings){
//            Intent startSettingsActivity = new Intent(this, SettingsActivity.class);
//            startActivity(startSettingsActivity);
//            return true;
//        }
        if (id == R.id.action_popular){
            MovieTaskExecute(0);
            return true;
        }
        if (id == R.id.action_toprated){
            MovieTaskExecute(1);
            return true;
        }
        if (id == R.id.action_favorite){
            MovieFavoriteTask task = new MovieFavoriteTask(MainActivityFragment.getMFTaskContext(),this);
            task.execute();
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



}
