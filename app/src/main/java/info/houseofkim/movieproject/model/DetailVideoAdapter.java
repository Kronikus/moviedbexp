package info.houseofkim.movieproject.model;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.Arrays;
import java.util.List;


import info.houseofkim.movieproject.R;

public class DetailVideoAdapter extends RecyclerView.Adapter<DetailVideoAdapter.ViewHolder> {

    private static final String DETAIL_VIDEO_ADAPTER = "DetailVideoAdapter";
    private List<VideoInfo> movieList;
    private ItemClickListener mClickListener;
    private LayoutInflater mInflater;
//    private Activity mActivity;

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView myTextView;


        ViewHolder(View itemView) {
            super(itemView);
            myTextView = itemView.findViewById(R.id.video_name);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            //  if (mClickListener != null) mClickListener.onItemClick(view, getAdapterPosition());
            watchYoutubeVideo(view.getContext(), movieList.get(getAdapterPosition()).getVideokey());
        }


    }

    public DetailVideoAdapter(Context context, List<VideoInfo> data) {
        super();
        movieList = data;
        mInflater = LayoutInflater.from(context);
      //  mActivity = (Activity) context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {


        View convertView = mInflater.inflate(
                R.layout.trailer_item, parent, false);

        //assert videoInfo != null;
        Log.d(DETAIL_VIDEO_ADAPTER, String.valueOf("VideoItemStart"));
        return new ViewHolder(convertView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        VideoInfo videoInfo = movieList.get(position);
        assert videoInfo != null;
        holder.myTextView.setText(videoInfo.getVideoname());

    }

    @Override
    public int getItemCount() {
        return movieList.size();
    }

    // allows clicks events to be caught
    void setClickListener(ItemClickListener itemClickListener) {
        this.mClickListener = itemClickListener;
    }

    // parent activity will implement this method to respond to click events
    public interface ItemClickListener {
        void onItemClick(View view, int position);
    }

    public void updateData(VideoInfo[] viewModels) {
       // movieList.clear();
        movieList = Arrays.asList(viewModels);
        //movieList.addAll(viewModels)
        //Collections.addAll(movieList, viewModels);
        notifyDataSetChanged();
    }

    private static void watchYoutubeVideo(Context context, String id) {
        //not working R.string.base_youtube_applink
        Intent appIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("vnd.youtube:"+ id));
        Intent webIntent = new Intent(Intent.ACTION_VIEW,
                Uri.parse(R.string.base_youtube_weblink + id));
        try {
            context.startActivity(appIntent);
        } catch (ActivityNotFoundException ex) {
            context.startActivity(webIntent);
        }
    }
}
