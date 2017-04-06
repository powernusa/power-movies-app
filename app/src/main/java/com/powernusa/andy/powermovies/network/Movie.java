package com.powernusa.andy.powermovies.network;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class Movie implements Parcelable {
    @SerializedName("id")
    private long mId;

    @SerializedName("original_title")
    private String mTitle;

    @SerializedName("poster_path")
    private String mPoster;

    @SerializedName("overview")
    private String mOverview;

    @SerializedName("vote_average")
    private String mUserRating;

    @SerializedName("release_date")
    private String mReleaseDate;


    @SerializedName("backdrop_path")
    private String mBackdrop;

    public Movie(long id,String title,String poster,String overview, String userRating,
                 String releaseDate,String backgrop){
        mId = id;
        mTitle = title;
        mPoster = poster;
        mOverview = overview;
        mUserRating = userRating;
        mReleaseDate = releaseDate;
        mBackdrop = backgrop;
    }

    protected Movie(Parcel in) {
        mId = in.readLong();
        mTitle = in.readString();
        mPoster = in.readString();
        mOverview = in.readString();
        mUserRating = in.readString();
        mReleaseDate = in.readString();
        mBackdrop = in.readString();
    }

    public static final Creator<Movie> CREATOR = new Creator<Movie>() {
        @Override
        public Movie createFromParcel(Parcel in) {
            return new Movie(in);
        }

        @Override
        public Movie[] newArray(int size) {
            return new Movie[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeLong(mId);
        parcel.writeString(mTitle);
        parcel.writeString(mPoster);
        parcel.writeString(mOverview);
        parcel.writeString(mUserRating);
        parcel.writeString(mReleaseDate);
        parcel.writeString(mBackdrop);
    }
}