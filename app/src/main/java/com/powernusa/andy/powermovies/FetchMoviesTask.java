package com.powernusa.andy.powermovies;

import android.os.AsyncTask;
import android.util.Log;

import com.powernusa.andy.powermovies.network.Movie;
import com.powernusa.andy.powermovies.network.MovieDatabaseService;
import com.powernusa.andy.powermovies.network.Movies;
import com.powernusa.andy.powermovies.utility.Constants;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Andy on 4/7/2017.
 */

public class FetchMoviesTask extends AsyncTask<Void,Void,ArrayList<Movie>>{
    public static final String LOG_TAG = FetchMoviesTask.class.getSimpleName();

    private String mSortBy;
    private FetchMoviesTask.Listener mListener;

    interface Listener{
        void onFetchFinished(ArrayList<Movie> movies);
    }

    //public FetchMoviesTask(){}
    public FetchMoviesTask(String sortBy, FetchMoviesTask.Listener listener){

        mSortBy = sortBy;
        mListener = listener;
    }

    @Override
    protected ArrayList<Movie> doInBackground(Void... voids) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constants.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        MovieDatabaseService service = retrofit.create(MovieDatabaseService.class);
        Call<Movies> call = service.discoverMovies(mSortBy,Constants.API_KEY);

        try {
            Response<Movies> response = call.execute();
            Movies movies = response.body();
            return movies.getMovies();
        } catch (IOException e) {
            Log.e(LOG_TAG,"A problem occured talking to the movie db",e);
            e.printStackTrace();
        }
        return null;
    }


    @Override
    protected void onPostExecute(ArrayList<Movie> movies) {
        if(movies!= null && !movies.isEmpty()){

            mListener.onFetchFinished(movies);
        }
        else{
            //no movies returned
        }
    }


}
