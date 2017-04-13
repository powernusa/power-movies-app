package com.powernusa.andy.powermovies;

import android.content.Context;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.powernusa.andy.powermovies.network.Movie;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by Andy on 4/7/2017.
 */

public class MovieListAdapter extends RecyclerView.Adapter<MovieListAdapter.ViewHolder>{
    public static final String LOG_TAG = MovieListAdapter.class.getSimpleName();
    private ArrayList<Movie> mMovies;
    private Callbacks mCallbacks;



    public interface Callbacks{
        void open(Movie movie,int position);
    }

    public MovieListAdapter(ArrayList<Movie> movies,Callbacks callbacks){
        mMovies = movies;
        mCallbacks = callbacks;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.movie_list_content,parent,false);
        Context context = view.getContext();
        int gridColsNumber = context.getResources().getInteger(R.integer.grid_num_cols);
        view.getLayoutParams().height = (int) (parent.getWidth() / gridColsNumber * Movie.POSTER_ASPECT_RATIO);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        holder.mTitleTextview.setVisibility(View.VISIBLE);
        holder.mTitleTextview.setText(mMovies.get(position).getTitle());
        holder.mThumbnail.setVisibility(View.VISIBLE);
        final Context context = holder.mView.getContext();

        Picasso.with(context)
                .load(mMovies.get(position).getPosterUrl(context))
                .into(holder.mThumbnail);

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(v.getContext(),"Movie Clicked: " + mMovies.get(position).getTitle(),Toast.LENGTH_LONG).show();
                mCallbacks.open(mMovies.get(position),position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mMovies.size();
    }

    public void add(ArrayList<Movie> movies){
        mMovies = movies;
        if(movies != null && !movies.isEmpty()){
            Log.d(LOG_TAG,">>>Movies from MovieListAdapter size: " + movies.size());
            for(int i=0;i<movies.size();i++){
                Log.d(LOG_TAG,i + ": " + movies.get(i).getTitle());
            }
        }
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        public View mView;
        public TextView mTitleTextview;
        public ImageView mThumbnail;

        public ViewHolder(View itemView) {
            super(itemView);
            mView = itemView;
            mTitleTextview = (TextView) mView.findViewById(R.id.title);
            mThumbnail = (ImageView)mView.findViewById(R.id.thumbnail);


        }
    }
}
