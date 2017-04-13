package com.powernusa.andy.powermovies;

import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.powernusa.andy.powermovies.network.Movie;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

public class MovieListActivity extends AppCompatActivity implements FetchMoviesTask.Listener,
    MovieListAdapter.Callbacks{
    public static final String LOG_TAG = MovieListActivity.class.getSimpleName();

    //@Bind(R.id.toolbar)
    Toolbar mToolbar;

    private RecyclerView mRecyclerView;
    private MovieListAdapter mAdapter;
    private boolean mTwoPane;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_list);
        //ButterKnife.bind(this);
        initializeWidgets();

        mToolbar.setTitle("Popular Movies App");
        setSupportActionBar(mToolbar);

        mAdapter = new MovieListAdapter(new ArrayList<Movie>(),this);
        int num_cols = getResources().getInteger(R.integer.grid_num_cols);
        mRecyclerView.setLayoutManager(new GridLayoutManager(this,num_cols));
        mRecyclerView.setAdapter(mAdapter);

        //new FetchMoviesTask().execute();
        new Extend_FetchMoviesTask().execute();
    }

    private void initializeWidgets(){
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mRecyclerView = (RecyclerView) findViewById(R.id.movie_list);

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
                Toast.makeText(getApplicationContext(),"Most Popular",Toast.LENGTH_SHORT).show();
                item.setChecked(true);
                break;
            case R.id.sort_by_top_rated:
                Toast.makeText(getApplicationContext(),"Top Rated",Toast.LENGTH_SHORT).show();
                item.setChecked(true);
                break;
            case R.id.sort_by_favorites:
                Toast.makeText(getApplicationContext(),"Favorites",Toast.LENGTH_SHORT).show();
                item.setChecked(true);
                break;

            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onFetchFinished(Command command) {

    }

    //MovieListAdapter callbacks
    @Override
    public void open(Movie movie, int position) {
        Snackbar.make(findViewById(R.id.coordinatorLayout),"Movie returned: " + movie.getTitle(),Snackbar.LENGTH_LONG).show();

    }

    public class Extend_FetchMoviesTask extends FetchMoviesTask{
        @Override
        protected void onPostExecute(List<Movie> movies) {
            if(movies != null && !movies.isEmpty()){
                Log.d(LOG_TAG,">>>Movies size in MovieListActivity : " + movies.size());
                for(int i =0;i< movies.size();i++){
                    Log.d(LOG_TAG,"Movie: " + movies.get(i).getTitle());
                }

            }
            mAdapter.add(new ArrayList<>(movies));
        }
    }
}
