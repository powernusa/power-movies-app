package com.powernusa.andy.powermovies.details;


import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
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
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.powernusa.andy.powermovies.R;
import com.powernusa.andy.powermovies.data.MovieContract;
import com.powernusa.andy.powermovies.network.Movie;
import com.powernusa.andy.powermovies.network.Review;
import com.powernusa.andy.powermovies.network.Trailer;
import com.powernusa.andy.powermovies.utility.Constants;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class MovieDetailFragment extends Fragment implements FetchTrailersTask.Listener,
                        TrailerListAdapter.Callbacks,ReviewListAdpater.Callbacks,FetchReviewsTask.Listener{
    private Movie mMovie;
    private ShareActionProvider mShareActionProvider;
    public static final String LOG_TAG = MovieDetailFragment.class.getSimpleName();

    private RecyclerView mRecylerViewForTrailers;
    private TrailerListAdapter mTrailerListAdapter;

    private RecyclerView mRecyclerViewForReviews;
    private ReviewListAdpater mReviewListAdapter;

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
        updateFavoriteButtons();

        //For horizontal list of trailers
        LinearLayoutManager trailerLayoutManager
                = new LinearLayoutManager(getContext(),LinearLayoutManager.HORIZONTAL,false);
        mRecylerViewForTrailers.setLayoutManager(trailerLayoutManager);
        mTrailerListAdapter = new TrailerListAdapter(new ArrayList<Trailer>(),this);
        mRecylerViewForTrailers.setAdapter(mTrailerListAdapter);
        mRecylerViewForTrailers.setNestedScrollingEnabled(false);

        //For vertical list of reviews
        LinearLayoutManager reviewLayoutManager
                = new LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL,false);
        mRecyclerViewForReviews.setLayoutManager(reviewLayoutManager);
        mReviewListAdapter = new ReviewListAdpater(new ArrayList<Review>(),this);
        mRecyclerViewForReviews.setAdapter(mReviewListAdapter);


        fetchTrailers();
        fetchReviews();
        return view;
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

    private void fetchTrailers(){
        FetchTrailersTask task = new FetchTrailersTask(this);
        task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR,mMovie.getId());
    }

    private void fetchReviews(){
        FetchReviewsTask task = new FetchReviewsTask(this);
        task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR,mMovie.getId());
    }

    @Override
    public void onTrailersFetchFinished(List<Trailer> trailers) {
        mTrailerListAdapter.add(trailers);

    }

    @Override
    public void onReviewsFetchFinished(ArrayList<Review> reviews) {
        mReviewListAdapter.add(reviews);
    }

    @Override
    public void watch(Trailer trailer, int position) {
        //Log.d(LOG_TAG,">>>Trailer URL: " + Uri.parse(trailer.getTrailerUrl()) + " Position: " + position);
        //Log.d(LOG_TAG,"---Trailer URL: " + trailer.getTrailerUrl() + " Position: " + position);
        startActivity(new Intent(Intent.ACTION_VIEW,Uri.parse(trailer.getTrailerUrl())));
    }

    @Override
    public void read(Review review, int position) {
        //Toast.makeText(getContext(),"Position clicked: " + position + " :" + review.getUrl(),Toast.LENGTH_SHORT).show();
        startActivity(new Intent(Intent.ACTION_VIEW,Uri.parse(review.getUrl())));

    }
    /***********************************************************************************************
     *
     *  Favorites + AsyncTask
     *
     * *********************************************************************************************
     */


    private View.OnClickListener mButtonListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.button_mark_as_favorite:
                    Toast.makeText(getContext(),"Mark Favorite clicked",Toast.LENGTH_SHORT).show();
                    //mWatchTrailerButtton.setEnabled(true);
                    //mRemoveFavoriteButton.setVisibility(View.VISIBLE);
                    //mMarkFavoriteButton.setVisibility(View.GONE);
                    markAsFavorite();
                    break;
                case R.id.button_remove_from_favorite:
                    Toast.makeText(getContext(),"Remove Favorite clicked",Toast.LENGTH_SHORT).show();
                    //mRemoveFavoriteButton.setVisibility(View.GONE);
                    //mMarkFavoriteButton.setVisibility(View.VISIBLE);
                    removeFromFavorite();
                    break;
                case R.id.button_watch_trailer:
                    Toast.makeText(getContext(),"Watch Trailer clicked",Toast.LENGTH_SHORT).show();
                    break;
                default:

                    break;
            }

        }
    };

    private boolean isFavorite(){
        Cursor movieCursor = getContext().getContentResolver().query(
                MovieContract.MovieEntry.CONTENT_URI,
                new String[]{MovieContract.MovieEntry.COLUMN_MOVIE_ID},
                MovieContract.MovieEntry.COLUMN_MOVIE_ID + "=?",
                new String[]{Long.toString(mMovie.getId())},
                null
        );

        if(movieCursor != null && movieCursor.moveToFirst()){
            movieCursor.close();
            return true;
        }else{
            return false;
        }
    }

    private void markAsFavorite(){
        new AsyncTask<Void,Void,Void>(){

            @Override
            protected Void doInBackground(Void... params) {
                if(!isFavorite()){
                    ContentValues cv = new ContentValues();
                    cv.put(MovieContract.MovieEntry.COLUMN_MOVIE_ID,mMovie.getId());
                    cv.put(MovieContract.MovieEntry.COLUMN_MOVIE_BACKDROP_PATH,mMovie.getBackdrop());
                    cv.put(MovieContract.MovieEntry.COLUMN_MOVIE_OVERVIEW,mMovie.getmOverview());
                    cv.put(MovieContract.MovieEntry.COLUMN_MOVIE_POSTER_PATH,mMovie.getPoster());
                    cv.put(MovieContract.MovieEntry.COLUMN_MOVIE_RELEASE_DATE,mMovie.getReleaseDate());
                    cv.put(MovieContract.MovieEntry.COLUMN_MOVIE_TITLE,mMovie.getTitle());
                    cv.put(MovieContract.MovieEntry.COLUMN_MOVIE_VOTE_AVERAGE,mMovie.getUserRating());
                    getContext().getContentResolver().insert(MovieContract.MovieEntry.CONTENT_URI,cv);
                }
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                updateFavoriteButtons();
                debugDatabase();
            }
        }.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }
    private void debugDatabase(){
        // this is not thread safe
        Cursor movieCursor = getContext().getContentResolver().query(MovieContract.MovieEntry.CONTENT_URI,
                null,
                null,
                null,
                null);
        //Log.d(LOG_TAG,"Number of rows: " + movieCursor.getCount());
        if(movieCursor!= null && movieCursor.moveToFirst()){
            Toast.makeText(getContext(),"Movie in database: " + movieCursor.getString(movieCursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_MOVIE_TITLE))
                    + "Movie count in database: " + movieCursor.getCount(), Toast.LENGTH_SHORT).show();
        }else{
            Toast.makeText(getContext(),"Movie removed",Toast.LENGTH_SHORT).show();
        }

    }
    private void removeFromFavorite(){
        new AsyncTask<Void,Void,Void>(){

            @Override
            protected Void doInBackground(Void... params) {
                if(isFavorite()){
                    getContext().getContentResolver().delete(MovieContract.MovieEntry.CONTENT_URI,
                            MovieContract.MovieEntry.COLUMN_MOVIE_ID + "=?",
                            new String[]{Long.toString(mMovie.getId())});
                }
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                updateFavoriteButtons();
                debugDatabase();
            }
        }.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }

    private void updateFavoriteButtons(){
        new AsyncTask<Void,Void,Boolean>(){

            @Override
            protected Boolean doInBackground(Void... params) {
                return isFavorite();
            }

            @Override
            protected void onPostExecute(Boolean aBoolean) {
                if(aBoolean){
                    mMarkFavoriteButton.setVisibility(View.GONE);
                    mRemoveFavoriteButton.setVisibility(View.VISIBLE);
                }else{
                    mMarkFavoriteButton.setVisibility(View.VISIBLE);
                    mRemoveFavoriteButton.setVisibility(View.GONE);
                }
            }
        }.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);


    }

    /***********************************************************************************************
     *
     *   Initiazlize Widget
     *
     * *********************************************************************************************
     */

    private ImageView mMoviePoster;
    private TextView mMovieTitle;
    private TextView mReleaseDate;
    private TextView mOverview;
    private TextView mUserRating;
    private ImageView mFirstStar,mSecondStar,mThirdStar,mFourthStar,mFifthStar;
    private ArrayList<ImageView> mRatingStars = new ArrayList<ImageView>();
    private Button mMarkFavoriteButton;
    private Button mRemoveFavoriteButton;
    private Button mWatchTrailerButtton;

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

        mMarkFavoriteButton = (Button) view.findViewById(R.id.button_mark_as_favorite);
        mRemoveFavoriteButton = (Button) view.findViewById(R.id.button_remove_from_favorite);
        mWatchTrailerButtton = (Button) view.findViewById(R.id.button_watch_trailer);
        mMarkFavoriteButton.setOnClickListener(mButtonListener);
        mRemoveFavoriteButton.setOnClickListener(mButtonListener);
        mWatchTrailerButtton.setOnClickListener(mButtonListener);

        mRecyclerViewForReviews = (RecyclerView) view.findViewById(R.id.review_list);

    }



}
