package info.houseofkim.movieproject.model;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import info.houseofkim.movieproject.R;

public class MovieInfoAdapter extends CursorAdapter {
    private static final String LOG_TAG = MovieInfoAdapter.class.getSimpleName();
    private Context mContext;
    private static int sLoaderID;

    public static class ViewHolder {
        final ImageView imageView;

        ViewHolder(View view) {
            imageView = view.findViewById(R.id.movie_image);
        }
    }

    public MovieInfoAdapter(Context context, Cursor c, int flags, int loaderID) {
        super(context, c, flags);
        Log.d(LOG_TAG, "MovieInfoAdapter");
        mContext = context;
        sLoaderID = loaderID;
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        int layoutId = R.layout.fragment_movie_pic;

        //  Log.d(LOG_TAG, "In new View");

        View view = LayoutInflater.from(context).inflate(layoutId, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        view.setTag(viewHolder);

        return view;
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {

        ViewHolder viewHolder = (ViewHolder) view.getTag();

        // Log.d(LOG_TAG, "In bind View");
        int imageIndex = cursor.getColumnIndex(MovieInfosContract.MovieInfoEntry.COLUMN_IMAGE);
        String image = cursor.getString(imageIndex);
        //Log.i(LOG_TAG, "Image reference extracted: " + image);
        Picasso.with(mContext)
                .load(mContext.getString(R.string.moviedbimageurl) + image)
                .error(R.mipmap.picasso_error)
                .into(viewHolder.imageView);


    }


    public int getMovieId(int position) {
        int dbposition = position + 1;
        // Log.d("getMovieId", String.valueOf(position));
        Uri uri = MovieInfosContract.MovieInfoEntry.CONTENT_URI;
        String[] resultView = new String[]{MovieInfosContract.MovieInfoEntry.COLUMN_MOVIEID};
        Cursor mInfo = mContext.getContentResolver().query(uri, resultView, null, null, null);
        int movid = 0;
        Log.d("getMovieId", String.valueOf(mInfo));
        if (mInfo != null) {
            if (mInfo.getCount() != 0) {

                mInfo.move(dbposition);
                movid = mInfo.getInt(mInfo.getColumnIndex(MovieInfosContract.MovieInfoEntry.COLUMN_MOVIEID));
                Log.d("getMovieId", String.valueOf(movid));

            }
            mInfo.close();
        }
        return movid;
    }

}
