package com.powernusa.andy.powermovies.details;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.powernusa.andy.powermovies.R;
import com.powernusa.andy.powermovies.network.Review;

import java.util.ArrayList;

/**
 * Created by andy on 26/04/2017.
 */

public class ReviewListAdpater extends RecyclerView.Adapter<ReviewListAdpater.ViewHolder> {
    private ArrayList<Review> mReviews;
    private Callbacks mCallbacks;

    public ReviewListAdpater(ArrayList<Review> reviews, Callbacks callbacks) {
        mReviews = reviews;
        mCallbacks = callbacks;
    }


    public interface Callbacks {
        void read(Review review, int position);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View rootView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.review_list_content, parent, false);

        return new ViewHolder(rootView);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        final Review review = mReviews.get(position);
        holder.mReviewAuthor.setText(review.getAuthor());
        holder.mReviewContent.setText(review.getContent());

        holder.mRootView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCallbacks.read(review, holder.getAdapterPosition());
            }
        });

    }


    @Override
    public int getItemCount() {
        return mReviews.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private TextView mReviewAuthor;
        private TextView mReviewContent;
        private View mRootView;

        public ViewHolder(View itemView) {
            super(itemView);
            mRootView = itemView;
            mReviewAuthor = (TextView) mRootView.findViewById(R.id.review_author);
            mReviewContent = (TextView) mRootView.findViewById(R.id.review_content);
        }
    }

    public void add(ArrayList<Review> reviews) {

        mReviews.clear();
        mReviews.addAll(reviews);

        notifyDataSetChanged();
    }
}
