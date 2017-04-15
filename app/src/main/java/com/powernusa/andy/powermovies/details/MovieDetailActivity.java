package com.powernusa.andy.powermovies.details;

import android.graphics.Color;
import android.os.Build;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.powernusa.andy.powermovies.R;
import com.powernusa.andy.powermovies.network.Movie;
import com.powernusa.andy.powermovies.utility.Constants;

public class MovieDetailActivity extends AppCompatActivity {
    private Toolbar mToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);
        mToolbar = (Toolbar) findViewById(R.id.detail_toolbar);
        setSupportActionBar(mToolbar);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(Color.TRANSPARENT);  //WOW nice!
        }

        ActionBar actionBar = getSupportActionBar();
        if(actionBar != null){
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        if(savedInstanceState == null){
            Movie movie =  getIntent().getParcelableExtra(Constants.ARG_MOVIE);
            //Snackbar.make(findViewById(R.id.coordinatorLayout),"Movie passed: " + movie.getTitle(),Snackbar.LENGTH_LONG).show();
            MovieDetailFragment fragment = new MovieDetailFragment();
            Bundle args = new Bundle();
            args.putParcelable(Constants.ARG_MOVIE,movie);
            fragment.setArguments(args);
            getSupportFragmentManager().beginTransaction().add(R.id.movie_detail_container,fragment).commit();
        }


    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            //onBackPressed();
            finish();
            //Toast.makeText(this,"home up clicked",Toast.LENGTH_LONG).show();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
