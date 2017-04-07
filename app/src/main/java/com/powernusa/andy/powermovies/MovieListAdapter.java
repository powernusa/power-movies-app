package com.powernusa.andy.powermovies;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.powernusa.andy.powermovies.network.Movie;

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
        return null;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return mMovies.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        public ViewHolder(View itemView) {
            super(itemView);
        }
    }
}
