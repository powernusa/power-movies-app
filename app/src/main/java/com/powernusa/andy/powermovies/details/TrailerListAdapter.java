package com.powernusa.andy.powermovies.details;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.powernusa.andy.powermovies.R;
import com.powernusa.andy.powermovies.network.Trailer;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by andy on 18/04/2017.
 */

public class TrailerListAdapter extends RecyclerView.Adapter<TrailerListAdapter.ViewHolder> {
    private final ArrayList<Trailer> mTrailers;
    private final Callbacks mCallbacks;

    public interface Callbacks{
        void watch(Trailer  trailer,int position);
    }

    public TrailerListAdapter(ArrayList<Trailer> trailers, Callbacks callbacks) {
        this.mTrailers = trailers;
        this.mCallbacks = callbacks;
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View rootView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.trailer_list_content,parent,false);
        return new ViewHolder(rootView);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        final Trailer trailer = mTrailers.get(position);
        Context context = holder.mView.getContext();

        float paddingLeft = 0;
        if(position == 0){
            paddingLeft = context.getResources().getDimension(R.dimen.padding_16dp);
        }
        float paddingRight = 0;
        if(position + 1 != getItemCount()){
            paddingRight = context.getResources().getDimension(R.dimen.padding_16dp) / 2;
        }

        holder.mView.setPadding((int)paddingLeft,0,(int)paddingRight,0);

        String thumbnailUrl = "http://img.youtube.com/vi/" + trailer.getKey() + "/0.jpg";
        Picasso.with(context)
                .load(thumbnailUrl)
                .config(Bitmap.Config.RGB_565)
                .into(holder.mThumbnailView);

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCallbacks.watch(trailer,holder.getAdapterPosition());
            }
        });
    }

    @Override
    public int getItemCount() {
        return mTrailers.size();
    }

    public void add(List<Trailer> trailers){
        mTrailers.clear();
        mTrailers.addAll(trailers);
        notifyDataSetChanged();
    }
    public ArrayList<Trailer>getTrailers(){
        return mTrailers;
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        public final View mView;
        private ImageView mThumbnailView;
        public ViewHolder(View itemView) {
            super(itemView);
            mView = itemView;
            mThumbnailView = (ImageView) mView.findViewById(R.id.trailer_thumbnail);
        }
    }
}
