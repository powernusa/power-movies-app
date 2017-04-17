package com.powernusa.andy.powermovies.network;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class Movies {
    @SerializedName("results")
    private ArrayList<Movie> movies = new ArrayList<>();


    public ArrayList<Movie> getMovies(){
        return this.movies;
    }
}