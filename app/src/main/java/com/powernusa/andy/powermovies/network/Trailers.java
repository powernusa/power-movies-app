package com.powernusa.andy.powermovies.network;

/**
 * Created by andy on 17/04/2017.
 */

import java.util.List;
import com.google.gson.annotations.SerializedName;

public class Trailers {

    @SerializedName("results")
    private List<Trailer> mTrailers = null;

    public List<Trailer> getTrailers() {
        return mTrailers;
    }



}
