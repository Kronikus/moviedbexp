package info.houseofkim.movieproject.model;

import android.content.ContentUris;
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

//    public MovieInfoAdapter(@NonNull Context context, @NonNull List<MovieInfo> objects) {
//        super(context, 0, objects);
//        mImageIds=objects;
//    }
//        @NonNull
//    @Override
//    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
//
//        MovieInfo movieInfo= getItem(position);
//        if (convertView == null) {
//            convertView = LayoutInflater.from(getContext()).inflate(
//                    R.layout.fragment_movie_pic, parent, false);
//        }
//        ImageView iconView =  convertView.findViewById(R.id.movie_image);
//        assert movieInfo != null;
//        //iconView.setImageResource(movieInfo.image);
//        Picasso.with(this.getContext())
//            .load(getContext().getString(R.string.moviedbimageurl)+movieInfo.getImage())
//            .into(iconView);
//
////        TextView versionNameView = (TextView) convertView.findViewById(R.id.);
////        versionNameView.setText(androidFlavor.versionName
////                + " - " + androidFlavor.versionNumber );
//
//        return convertView;
//    }

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

     //   Log.d(LOG_TAG, "In new View");

        View view = LayoutInflater.from(context).inflate(layoutId, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        view.setTag(viewHolder);

        return view;
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {

        ViewHolder viewHolder = (ViewHolder) view.getTag();

      //  Log.d(LOG_TAG, "In bind View");

//        int versionIndex = cursor.getColumnIndex(MovieInfosContract.MovieInfoEntry.COLUMN_VERSION_NAME);
//        final String versionName = cursor.getString(versionIndex);
//        viewHolder.textView.setText(versionName);

        int imageIndex = cursor.getColumnIndex(MovieInfosContract.MovieInfoEntry.COLUMN_IMAGE);
        String image = cursor.getString(imageIndex);
      //  Log.i(LOG_TAG, "Image reference extracted: " + image);
        Picasso.with(mContext)
                .load(mContext.getString(R.string.moviedbimageurl) + image)
                .into(viewHolder.imageView);


    }


    public int getMovieId(int position) {
        int  dbposition = position +1;
       // Log.d("getMovieId", String.valueOf(position));
//        Uri uri = ContentUris.withAppendedId(MovieInfosContract.MovieInfoEntry.CONTENT_URI,
//                dbposition);
        Uri uri = MovieInfosContract.MovieInfoEntry.CONTENT_URI;
       // String selection = MovieInfosContract.MovieInfoEntry._ID;
      //  String[] selectionId = new String[]{String.valueOf(position + 1)};
        String[] resultView = new String[]{MovieInfosContract.MovieInfoEntry.COLUMN_MOVIEID};
        Cursor mInfo = mContext.getContentResolver().query(uri, resultView,null , null, null);
        int movid = 0;
        Log.d("getMovieId",String.valueOf(mInfo));
        if (mInfo != null) {
            if (mInfo.getCount() != 0) {

                mInfo.move(dbposition);
                movid = mInfo.getInt(mInfo.getColumnIndex(MovieInfosContract.MovieInfoEntry.COLUMN_MOVIEID));
                Log.d("getMovieId",String.valueOf(movid));
                mInfo.close();
            }
        }
        return movid;
    }

}
