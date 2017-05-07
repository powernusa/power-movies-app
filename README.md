# Power-Movies-app
The purpose of this project was to built an app, optimized for tablets, to help users discover popular and highly rated movies on the web. It displays a scrolling grid of movie trailers, launches a details screen whenever a particular movie is selected, allows users to save favorites, play trailers, and read user reviews. This app utilizes core Android user interface components and fetches movie information using themoviedb.org web API.

### Screenshot and animation
<img width="30%" border="30" src="https://cloud.githubusercontent.com/assets/13763933/25564424/3cdd1186-2ddd-11e7-8730-e020770cd6f2.jpg"/>  <img width="30%" border="30" src="https://media.giphy.com/media/3o7bujOQtZ9mrKokRq/giphy.gif"/>

### Tablet Two Pane
<img width="80%" border="30" src="https://cloud.githubusercontent.com/assets/13763933/25564508/e9bc7274-2dde-11e7-8e9d-27c9e6a23f16.jpg"/>
<br>

### Youtube Demo
<a href="https://www.youtube.com/watch?v=Br8zCcp1mGg" target="_blank"><img src="https://cloud.githubusercontent.com/assets/13763933/25564539/b8c0f8c4-2ddf-11e7-8900-dbe1d79f4e07.jpg" 
alt="better youtube text here" width="100%" height="540" border="10" /></a>

### Notes On Using Retrofit
<pre><code>
public interface MovieDatabaseService { <br>
    @GET("3/movie/{sort_by}")<br>
    Call<Movies> discoverMovies(@Path("sort_by")String sortBy, @Query("api_key")String apiKey);

    //http://api.themoviedb.org/3/movie/209112/videos?api_key=-----------------------------
    @GET("3/movie/{id}/videos")
    Call<Trailers> findTrailersById(@Path("id") long movieId,@Query("api_key")String apiKey);

    //http://api.themoviedb.org/3/movie/209112/reviews?api_key=-----------------------------
    @GET("3/movie/{id}/reviews")
    Call<Reviews> findReviewsById(@Path("id") long movieId,@Query("api_key")String apiKey);
}

public class FetchMoviesTask extends AsyncTask<Void,Void,ArrayList<Movie>>{<br>
    public static final String LOG_TAG = FetchMoviesTask.class.getSimpleName();<br>

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
</code></pre>

# License
Copyright 2017 Andy Soelistio

Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at

http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions and limitations under the License.
