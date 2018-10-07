package info.houseofkim.movieproject;

import android.app.Fragment;
import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.List;

import info.houseofkim.movieproject.model.DetailVideoAdapter;
import info.houseofkim.movieproject.model.MovieInfosContract;
import info.houseofkim.movieproject.model.VideoInfo;

public class DetailActivityFragment extends Fragment {


    private Cursor mDetailCursor;
    private View mRootView;
    private String mPosition;
    private ImageView mImageView;
    private TextView mTextView;
    private TextView mUriText;
    private Uri mUri;
    private static final int CURSOR_LOADER_ID = 0;
    DetailVideoAdapter videoAdapter;

    public static DetailActivityFragment newInstance(String position, Uri uri) {
        DetailActivityFragment fragment = new DetailActivityFragment();
        Bundle args = new Bundle();
        fragment.mPosition = position;
        fragment.mUri = uri;
        args.putString("id", position);
        fragment.setArguments(args);
        return fragment;
    }

    public DetailActivityFragment() {
        // Required empty public constructor
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_detail, container, false);
//         VideoInfo[] videoInfo = {new VideoInfo("asd","asd","asd"),
//                new VideoInfo("asd2","asd2","asd2"),
//                 new VideoInfo("asd3","asd3","asd3")};

        RecyclerView listView = rootView.findViewById(R.id.video_list);
        listView.setLayoutManager(new LinearLayoutManager(this.getContext()));
        VideoInfo[] videoInfo = new VideoInfo[]{};
        videoAdapter = new DetailVideoAdapter(getActivity(), Arrays.asList(videoInfo));
        listView.setAdapter(videoAdapter);

        return rootView;
    }


    @Override
    public void onDetach() {
        super.onDetach();
//        mListener = null;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

}