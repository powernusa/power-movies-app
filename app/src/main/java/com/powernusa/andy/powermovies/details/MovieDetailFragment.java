package com.powernusa.andy.powermovies.details;


import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.powernusa.andy.powermovies.R;
import com.powernusa.andy.powermovies.network.Movie;
import com.powernusa.andy.powermovies.utility.Constants;
import com.squareup.picasso.Picasso;

/**
 * A simple {@link Fragment} subclass.
 */
public class MovieDetailFragment extends Fragment {
    private Movie mMovie;

    public MovieDetailFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(getArguments().containsKey(Constants.ARG_MOVIE)){
            mMovie = getArguments().getParcelable(Constants.ARG_MOVIE);
            Toast.makeText(getActivity(), "Movie in fragment: " + mMovie.getTitle(), Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.movie_detail,container,false);
        initializeWidget(view);

        Picasso.with(getActivity())
                .load(mMovie.getPosterUrl(getActivity()))
                .into(mMoviePoster);

        mMovieTitle.setText(mMovie.getTitle());

        return view;
    }

    private ImageView mMoviePoster;
    private TextView mMovieTitle;

    private void initializeWidget(View view){
        mMoviePoster = (ImageView) view.findViewById(R.id.movie_poster);
        mMovieTitle = (TextView)view.findViewById(R.id.movie_title);

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        Activity activity = getActivity();
        CollapsingToolbarLayout appBarLayout = (CollapsingToolbarLayout)
                activity.findViewById(R.id.toolbar_layout);
        if (appBarLayout != null && activity instanceof MovieDetailActivity) {
            appBarLayout.setTitle(mMovie.getTitle());
        }

        ImageView movieBackdrop = ((ImageView) activity.findViewById(R.id.movie_backdrop));
        if (movieBackdrop != null) {
            Picasso.with(activity)
                    .load(mMovie.getBackdropUrl(getContext()))
                    .config(Bitmap.Config.RGB_565)
                    .into(movieBackdrop);
        }
    }

}
