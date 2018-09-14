package info.houseofkim.movieproject.model;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.List;

import info.houseofkim.movieproject.R;

public class MovieInfoAdapter extends ArrayAdapter<MovieInfo>{
    private Context mContext;
    private List<MovieInfo> mImageIds;
    public MovieInfoAdapter(@NonNull Context context, @NonNull List<MovieInfo> objects) {
        super(context, 0, objects);
        mImageIds=objects;
    }
        @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        MovieInfo movieInfo= getItem(position);
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(
                    R.layout.fragment_movie_pic, parent, false);
        }
        ImageView iconView =  convertView.findViewById(R.id.movie_image);
        assert movieInfo != null;
        //iconView.setImageResource(movieInfo.image);
        Picasso.with(this.getContext())
            .load(getContext().getString(R.string.moviedbimageurl)+movieInfo.getImage())
            .into(iconView);

//        TextView versionNameView = (TextView) convertView.findViewById(R.id.);
//        versionNameView.setText(androidFlavor.versionName
//                + " - " + androidFlavor.versionNumber );

        return convertView;
    }
    public int getMovieId(int position){
        MovieInfo movieInfo= mImageIds.get(position);
        return movieInfo.getMovieId();
    };
}
