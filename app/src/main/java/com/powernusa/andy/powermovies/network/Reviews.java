package com.powernusa.andy.powermovies.network;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by andy on 26/04/2017.
 */

public class Reviews {

    @SerializedName("results")
    public List<Review> mReviews = new ArrayList<>();

    public List<Review> getReviews(){
        return mReviews;
    }
}
