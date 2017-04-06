package com.powernusa.andy.powermovies.network;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class Movies {
    @SerializedName("results")
    private List<Movie> movies = new ArrayList<>();


    public List<Movie> getMovies(){
        return this.movies;
    }
}