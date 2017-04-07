package com.powernusa.andy.powermovies;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import butterknife.Bind;
import butterknife.ButterKnife;

public class MovieListActivity extends AppCompatActivity implements FetchMoviesTask.Listener{


    @Bind(R.id.toolbar)
    Toolbar mToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_list);
        ButterKnife.bind(this);

        mToolbar.setTitle("Power Movies App");
        setSupportActionBar(mToolbar);

        new FetchMoviesTask().execute();
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
}
