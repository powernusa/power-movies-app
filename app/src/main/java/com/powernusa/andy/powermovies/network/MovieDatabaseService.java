package com.powernusa.andy.powermovies.network;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by Andy on 4/5/2017.
 */

public interface MovieDatabaseService {
    @GET("3/movie/{sort_by}")
    Call<Movies> discoverMovies(@Path("sort_by")String sortBy, @Query("api_key")String apiKey);

    //http://api.themoviedb.org/3/movie/209112/videos?api_key=b41491fab3366590c929b8668512cb03
    @GET("3/movie/{id}/videos")
    Call<Trailers> findTrailersById(@Path("id") long movieId,@Query("api_key")String apiKey);
}
