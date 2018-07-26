package info.houseofkim.movieproject;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

import java.util.Arrays;

public class MainActivityFragment extends Fragment {
    private MovieInfoAdapter movieAdapter;
MovieInfo[] movieInfos = {
        new MovieInfo("Cupcake", "1.5", R.drawable.cupcake),
        new MovieInfo("Donut", "1.6", R.drawable.donut),
        new MovieInfo("Eclair", "2.0-2.1", R.drawable.eclair),

}
    public  MainActivityFragment(){}
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);
         movieAdapter = new MovieInfoAdapter (getActivity(), Arrays.asList(movieInfos));

        GridView gridView = (GridView) rootView.findViewById(R.id.frag_main);
        gridView.setAdapter(MovieInfo);

        return rootView;
    }
}
