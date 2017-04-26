package com.powernusa.andy.powermovies.details;

import android.os.AsyncTask;
import android.util.Log;

import com.powernusa.andy.powermovies.network.MovieDatabaseService;
import com.powernusa.andy.powermovies.network.Review;
import com.powernusa.andy.powermovies.network.Reviews;
import com.powernusa.andy.powermovies.network.Trailers;
import com.powernusa.andy.powermovies.utility.Constants;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by andy on 26/04/2017.
 */

public class FetchReviewsTask extends AsyncTask<Long,Void,ArrayList<Review>> {
    public static final String LOG_TAG = FetchReviewsTask.class.getSimpleName();
    private final Listener mListener;

    public FetchReviewsTask(Listener listener) {
        this.mListener = listener;
    }

    public interface Listener{
        void onReviewsFetchFinished(ArrayList<Review> reviews);
    }

    @Override
    protected ArrayList<Review> doInBackground(Long... params) {
        if(params.length == 0){
            return null;
        }
        long movieId = params[0];

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://api.themoviedb.org/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        MovieDatabaseService service = retrofit.create(MovieDatabaseService.class);
        Call<Reviews> call = service.findReviewsById(movieId, Constants.API_KEY);

        try {
            Response<Reviews> response = call.execute();
            Reviews reviews = response.body();
            return (ArrayList<Review>) reviews.getReviews();
        } catch (IOException e) {
            e.printStackTrace();
            Log.e(LOG_TAG,"A problem occured talking to the movie db.",e);
        }
        return null;
    }

    @Override
    protected void onPostExecute(ArrayList<Review> reviews) {
        if(reviews != null){
            mListener.onReviewsFetchFinished(reviews);
        }else{
            mListener.onReviewsFetchFinished(new ArrayList<Review>());
        }
    }
}
