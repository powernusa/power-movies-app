package com.powernusa.andy.powermovies.network;

import android.content.Context;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.util.Log;

import com.google.gson.annotations.SerializedName;
import com.powernusa.andy.powermovies.R;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class Movie implements Parcelable {
    public static final String LOG_TAG = Movie.class.getSimpleName();
    public static final float POSTER_ASPECT_RATIO = 1.5f;

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

    @Nullable
    public String getTitle(){
        return mTitle;
    }

    public long getId(){
        return mId;
    }

    @Nullable
    public String getPosterUrl(Context context){
        if(mPoster != null && !mPoster.isEmpty()){
            return context.getResources().getString(R.string.url_for_downloading_poster) + mPoster;
        }
        return null;
    }

    public String getPoster(){
        return mPoster;
    }

    public String getReleaseDate(Context context){
        String inputPattern = "yyyy-MM-dd";
        SimpleDateFormat inputFormat = new SimpleDateFormat(inputPattern, Locale.US);
        if(mReleaseDate != null && !mReleaseDate.isEmpty()){
            try {
                Date date = inputFormat.parse(mReleaseDate);
                return DateFormat.getDateInstance().format(date);
            } catch (ParseException e) {
                e.printStackTrace();
                Log.e(LOG_TAG,"The release date not parsed successfully: " + mReleaseDate);
            }
        }else{
            mReleaseDate = context.getString(R.string.release_date_unknown);
        }

        return mReleaseDate;
    }

    public String getReleaseDate(){
        return mReleaseDate;
    }

    public String getmOverview(){
        return mOverview;
    }
    public String getUserRating(){
        return mUserRating;
    }

    public String getBackdrop(){
        return mBackdrop;
    }

    public String getBackdropUrl(Context context){
        if(mBackdrop != null && !mBackdrop.isEmpty()){
            return context.getString(R.string.url_for_downloading_backdrop) + mBackdrop;
        }
        return null;
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