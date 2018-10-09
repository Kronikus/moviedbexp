package info.houseofkim.movieproject.model;

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

public class DetailReviewAdapter extends RecyclerView.Adapter<DetailReviewAdapter.ViewHolder> {

    private static final String DETAIL_REVIEW_ADAPTER = "DetailReviewAdapter";
    private List<ReviewInfo> movieList;
    private ItemClickListener mClickListener;
    private LayoutInflater mInflater;

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView reviewAuthor;
        TextView reviewContent;


        ViewHolder(View itemView) {
            super(itemView);
            reviewAuthor = itemView.findViewById(R.id.review_author);
            reviewContent = itemView.findViewById(R.id.review_content);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            //  if (mClickListener != null) mClickListener.onItemClick(view, getAdapterPosition());
            watchReview(itemView.getContext(), movieList.get(getAdapterPosition()).getReviewId());
        }
    }

    public DetailReviewAdapter(Context context, List<ReviewInfo> data) {
        super();
        movieList = data;
        mInflater = LayoutInflater.from(context);

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {


        View convertView = mInflater.inflate(
                R.layout.review_item, parent, false);

        //assert videoInfo != null;
        Log.d(DETAIL_REVIEW_ADAPTER, String.valueOf("ReviewItemStart"));
        return new ViewHolder(convertView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        ReviewInfo reviewInfo = movieList.get(position);
        assert reviewInfo != null;
        holder.reviewAuthor.setText(reviewInfo.getReviewAuthor());
        holder.reviewContent.setText(reviewInfo.getReviewContent());

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

    public void updateData(ReviewInfo[] viewModels) {
        movieList = Arrays.asList(viewModels);
        notifyDataSetChanged();
    }

    private static void watchReview(Context context, String id) {
        Intent webIntent = new Intent(Intent.ACTION_VIEW,
                Uri.parse(context.getString(R.string.base_review_weblink) + id));
        try {
            context.startActivity(webIntent);
        } catch (ActivityNotFoundException ex) {
            Log.e("DetailReviewAdapter", "Cannot start browser");
        }
    }
}
