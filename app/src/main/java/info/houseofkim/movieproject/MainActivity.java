package info.houseofkim.movieproject;

import android.content.Context;
import android.content.res.Resources;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;
import android.content.Intent;

public class MainActivity extends AppCompatActivity implements MainActivityFragment.OnImageClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
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
}
