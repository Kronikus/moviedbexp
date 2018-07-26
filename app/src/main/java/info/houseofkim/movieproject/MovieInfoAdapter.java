package info.houseofkim.movieproject;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

public class MovieInfoAdapter extends ArrayAdapter<MovieInfo>{

    public MovieInfoAdapter(@NonNull Context context, @NonNull List<MovieInfo> objects) {
        super(context, 0, objects);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        MovieInfo movieInfo= getItem(position);
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(
                    R.layout.fragment_movie_pic, parent, false);
        }
        ImageView iconView = (ImageView) convertView.findViewById(R.id.movie_image);
        iconView.setImageResource(movieInfo.image);

//        TextView versionNameView = (TextView) convertView.findViewById(R.id.);
//        versionNameView.setText(androidFlavor.versionName
//                + " - " + androidFlavor.versionNumber );

        return convertView;
    }
}
