package com.powernusa.andy.powermovies.details;

import android.os.AsyncTask;
import android.util.Log;

import com.powernusa.andy.powermovies.network.MovieDatabaseService;
import com.powernusa.andy.powermovies.network.Trailer;
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
 * Created by andy on 17/04/2017.
 */

public class FetchTrailersTask extends AsyncTask<Long,Void,List<Trailer>> {

    public static final String LOG_TAG = FetchTrailersTask.class.getSimpleName();
    private final Listener mListener;

    public FetchTrailersTask(Listener mListener) {
        this.mListener = mListener;
    }

    public interface Listener{
        void onFetchFinished(List<Trailer> trailers);
    }
    @Override
    protected List<Trailer> doInBackground(Long... params) {
        if(params.length ==0){
            return null;
        }

        long movieId = params[0];

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://api.themoviedb.org/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        MovieDatabaseService service = retrofit.create(MovieDatabaseService.class);
        Call<Trailers> call = service.findTrailersById(movieId, Constants.API_KEY);

        try {
            Response<Trailers> response = call.execute();
            Trailers trailers = response.body();
            return trailers.getTrailers();
        } catch (IOException e) {
            e.printStackTrace();
            Log.e(LOG_TAG,"A problem occured talking to the movie db.",e);
        }

        return null;
    }

    @Override
    protected void onPostExecute(List<Trailer> trailers) {
        if(trailers != null){
            mListener.onFetchFinished(trailers);
        }else{
            mListener.onFetchFinished(new ArrayList<Trailer>());
        }
    }
}
