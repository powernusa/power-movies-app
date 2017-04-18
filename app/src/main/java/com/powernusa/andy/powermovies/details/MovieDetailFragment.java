package com.powernusa.andy.powermovies.details;


import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.ShareActionProvider;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.powernusa.andy.powermovies.R;
import com.powernusa.andy.powermovies.network.Movie;
import com.powernusa.andy.powermovies.network.Trailer;
import com.powernusa.andy.powermovies.utility.Constants;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class MovieDetailFragment extends Fragment implements FetchTrailersTask.Listener
            ,TrailerListAdapter.Callbacks{
    private Movie mMovie;
    private ShareActionProvider mShareActionProvider;
    public static final String LOG_TAG = MovieDetailFragment.class.getSimpleName();

    private RecyclerView mRecylerViewForTrailers;
    private TrailerListAdapter mTrailerListAdapter;

    public MovieDetailFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        if(getArguments().containsKey(Constants.ARG_MOVIE)){
            mMovie = getArguments().getParcelable(Constants.ARG_MOVIE);
            //Toast.makeText(getActivity(), "Movie in fragment: " + mMovie.getTitle(), Toast.LENGTH_SHORT).show();
        }
        setHasOptionsMenu(true);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.movie_detail,container,false);

        initializeWidget(view);

        Picasso.with(getActivity())
                .load(mMovie.getPosterUrl(getActivity()))
                .config(Bitmap.Config.RGB_565)
                .into(mMoviePoster);

        mMovieTitle.setText(mMovie.getTitle());
        mReleaseDate.setText(mMovie.getReleaseDate(getActivity()));
        mOverview.setText(mMovie.getmOverview());
        updateRating();

        //For horizontal list of trailers
        LinearLayoutManager layoutManager
                = new LinearLayoutManager(getContext(),LinearLayoutManager.HORIZONTAL,false);
        mRecylerViewForTrailers.setLayoutManager(layoutManager);
        mTrailerListAdapter = new TrailerListAdapter(new ArrayList<Trailer>(),this);
        mRecylerViewForTrailers.setAdapter(mTrailerListAdapter);
        mRecylerViewForTrailers.setNestedScrollingEnabled(false);


        fetchTrailers();
        return view;
    }

    private void fetchTrailers(){
        FetchTrailersTask task = new FetchTrailersTask(this);
        task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR,mMovie.getId());
    }


    private void updateRating(){
        if(mMovie.getUserRating() != null && !mMovie.getUserRating().isEmpty()){
            String userRatingStr = getResources().getString(R.string.user_rating_movie,mMovie.getUserRating());
            mUserRating.setText(userRatingStr);

            float userRating = Float.valueOf(mMovie.getUserRating()) / 2;
            int integerPart = (int) userRating;

            // Fill stars
            for (int i = 0; i < integerPart; i++) {
                mRatingStars.get(i).setImageResource(R.drawable.ic_star_black_24dp);
            }

            // Fill half star
            if (Math.round(userRating) > integerPart) {
                mRatingStars.get(integerPart).setImageResource(
                        R.drawable.ic_star_half_black_24dp);
            }
        }else {
            mUserRating.setVisibility(View.GONE);
        }

    }
    private ImageView mMoviePoster;
    private TextView mMovieTitle;
    private TextView mReleaseDate;
    private TextView mOverview;
    private TextView mUserRating;
    private ImageView mFirstStar,mSecondStar,mThirdStar,mFourthStar,mFifthStar;
    private ArrayList<ImageView> mRatingStars = new ArrayList<ImageView>();

    private void initializeWidget(View view){
        mMoviePoster = (ImageView) view.findViewById(R.id.movie_poster);
        mMovieTitle = (TextView)view.findViewById(R.id.movie_title);
        mReleaseDate = (TextView)view.findViewById(R.id.movie_release_date);
        mUserRating = (TextView)view.findViewById(R.id.movie_user_rating);
        mOverview = (TextView)view.findViewById(R.id.movie_overview);

        //Initialize mRatingStars
        mFirstStar = (ImageView) view.findViewById(R.id.rating_first_star);
        mSecondStar = (ImageView) view.findViewById(R.id.rating_second_star);
        mThirdStar = (ImageView) view.findViewById(R.id.rating_third_star);
        mFourthStar = (ImageView) view.findViewById(R.id.rating_fourth_star);
        mFifthStar = (ImageView) view.findViewById(R.id.rating_fifth_star);
        mRatingStars.add(0,mFirstStar);mRatingStars.add(1,mSecondStar);
        mRatingStars.add(2,mThirdStar);mRatingStars.add(3,mFourthStar);
        mRatingStars.add(4,mFifthStar);

        mRecylerViewForTrailers = (RecyclerView) view.findViewById(R.id.trailer_list);


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

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.movie_detail_fragment, menu);
        MenuItem shareTrailerMenuItem = menu.findItem(R.id.share_trailer);
        mShareActionProvider = (ShareActionProvider) MenuItemCompat.getActionProvider(shareTrailerMenuItem);
    }

    @Override
    public void onFetchFinished(List<Trailer> trailers) {

        mTrailerListAdapter.add(trailers);

        /*
        for(int i =0;i<trailers.size();i++){
            Log.d(LOG_TAG,"trailer url: " + trailers.get(i).getTrailerUrl());
        }
        */
    }

    @Override
    public void watch(Trailer trailer, int position) {
        //Log.d(LOG_TAG,">>>Trailer URL: " + Uri.parse(trailer.getTrailerUrl()) + " Position: " + position);
        //Log.d(LOG_TAG,"---Trailer URL: " + trailer.getTrailerUrl() + " Position: " + position);
        startActivity(new Intent(Intent.ACTION_VIEW,Uri.parse(trailer.getTrailerUrl())));
    }
}
