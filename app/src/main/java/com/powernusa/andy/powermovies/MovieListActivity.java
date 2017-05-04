package com.powernusa.andy.powermovies;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.powernusa.andy.powermovies.data.MovieContract;
import com.powernusa.andy.powermovies.details.MovieDetailActivity;
import com.powernusa.andy.powermovies.details.MovieDetailFragment;
import com.powernusa.andy.powermovies.network.Movie;
import com.powernusa.andy.powermovies.utility.Constants;

import java.util.ArrayList;

public class MovieListActivity extends AppCompatActivity implements FetchMoviesTask.Listener,
        MovieListAdapter.Callbacks, LoaderManager.LoaderCallbacks<Cursor> {
    public static final String LOG_TAG = MovieListActivity.class.getSimpleName();
    public static final int FAV_MOVIE_LOADER = 1;

    //@Bind(R.id.toolbar)
    Toolbar mToolbar;

    private RecyclerView mRecyclerView;
    private MovieListAdapter mMovieListAdapter;
    private boolean mTwoPane;
    private String mSortBy = Constants.MOST_POPULAR;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_list);
        //ButterKnife.bind(this);
        initializeWidgets();

        mToolbar.setTitle("Popular Movies App");
        setSupportActionBar(mToolbar);

        mTwoPane = findViewById(R.id.movie_detail_container) != null;

        mMovieListAdapter = new MovieListAdapter(new ArrayList<Movie>(), this);
        int num_cols = getResources().getInteger(R.integer.grid_num_cols);
        mRecyclerView.setLayoutManager(new GridLayoutManager(this, num_cols));
        mRecyclerView.setAdapter(mMovieListAdapter);

        if (savedInstanceState != null) {
            mSortBy = savedInstanceState.getString(Constants.EXTRA_SORT_BY);
            if (savedInstanceState.containsKey(Constants.EXTRA_MOVIES)) {
                ArrayList<Movie> movies = savedInstanceState.getParcelableArrayList(Constants.EXTRA_MOVIES);
                mMovieListAdapter.add(movies);
                Toast.makeText(this,"size of movies: " + movies.size(),Toast.LENGTH_SHORT).show();
                Log.d(LOG_TAG,"movie size: " + movies.size());
                //mProgress.setVisibility(View.GONE);
                findViewById(R.id.progress).setVisibility(View.GONE);
                // For listening content updates for tow pane mode
                if (mSortBy.equals(Constants.FAVORITES)) {
                    getSupportLoaderManager().initLoader(FAV_MOVIE_LOADER, null, this);
                }
            }
            updateEmptyState();
        } else {
            fetchMovies(mSortBy);
        }

    }


    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        ArrayList<Movie> movies = mMovieListAdapter.getMovies();
        if (movies != null && !movies.isEmpty()) {
            outState.putParcelableArrayList(Constants.EXTRA_MOVIES, movies);
        }
        outState.putString(Constants.EXTRA_SORT_BY, mSortBy);

        if (!mSortBy.equals(Constants.FAVORITES)) {
            getSupportLoaderManager().destroyLoader(FAV_MOVIE_LOADER);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.sort_by_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.sort_by_most_popular:
                if (mSortBy.equals(Constants.FAVORITES)) {
                    getSupportLoaderManager().destroyLoader(FAV_MOVIE_LOADER);
                }
                mSortBy = Constants.MOST_POPULAR;
                fetchMovies(mSortBy);
                item.setChecked(true);
                break;
            case R.id.sort_by_top_rated:
                if (mSortBy.equals(Constants.FAVORITES)) {
                    getSupportLoaderManager().destroyLoader(FAV_MOVIE_LOADER);
                }
                mSortBy = Constants.TOP_RATED;
                fetchMovies(mSortBy);
                item.setChecked(true);
                break;
            case R.id.sort_by_favorites:
                mSortBy = Constants.FAVORITES;
                fetchMovies(mSortBy);
                item.setChecked(true);
                break;

            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onFetchFinished(ArrayList<Movie> movies) {
        mProgress.setVisibility(View.GONE);
        mMovieListAdapter.add(movies);
        updateEmptyState();

    }

    private void fetchMovies(String sortBy) {
        if (sortBy.equals(Constants.FAVORITES)) {
            getSupportLoaderManager().initLoader(FAV_MOVIE_LOADER, null, this);
        } else {
            mProgress.setVisibility(View.VISIBLE);
            new FetchMoviesTask(sortBy, this).execute();
        }

    }

    //MovieListAdapter callbacks
    @Override
    public void open(Movie movie, int position) {
        //Snackbar.make(findViewById(R.id.coordinatorLayout),"Movie returned: " + movie.getTitle(),Snackbar.LENGTH_LONG).show();
        if (mTwoPane) {

            MovieDetailFragment fragment = new MovieDetailFragment();
            Bundle args = new Bundle();
            args.putParcelable(Constants.ARG_MOVIE, movie);
            fragment.setArguments(args);
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.movie_detail_container, fragment)
                    .commit();


        } else {
            Intent intent = new Intent(this, MovieDetailActivity.class);
            intent.putExtra(Constants.ARG_MOVIE, movie);
            startActivity(intent);
        }

    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        findViewById(R.id.progress).setVisibility(View.VISIBLE);
        return new CursorLoader(this,
                MovieContract.MovieEntry.CONTENT_URI,
                null,
                null,
                null,
                null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        mMovieListAdapter.add(data);
        updateEmptyState();
        findViewById(R.id.progress).setVisibility(View.GONE);

    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }

    /**
     * *************************************************************************************************
     * <p>
     * <p>
     * *************************************************************************************************
     */
    private ProgressBar mProgress;

    private void initializeWidgets() {
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mRecyclerView = (RecyclerView) findViewById(R.id.movie_list);
        mProgress = (ProgressBar) findViewById(R.id.progress);

    }

    private void updateEmptyState() {
        if (mMovieListAdapter.getItemCount() == 0) {
            if (mSortBy.equals(Constants.FAVORITES)) {
                findViewById(R.id.empty_state_favorites_container).setVisibility(View.VISIBLE);
                findViewById(R.id.empty_state_connection_container).setVisibility(View.GONE);
            } else {
                findViewById(R.id.empty_state_connection_container).setVisibility(View.GONE);
                findViewById(R.id.empty_state_favorites_container).setVisibility(View.GONE);
            }

        } else {

            findViewById(R.id.empty_state_connection_container).setVisibility(View.GONE);
            findViewById(R.id.empty_state_favorites_container).setVisibility(View.GONE);
        }
    }

}
