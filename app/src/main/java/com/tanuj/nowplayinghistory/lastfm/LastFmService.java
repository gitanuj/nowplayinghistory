package com.tanuj.nowplayinghistory.lastfm;

import com.tanuj.nowplayinghistory.lastfm.pojos.TrackInfo;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface LastFmService {

    @GET("/2.0/?method=track.getInfo&format=json")
    Call<TrackInfo> trackInfo(@Query("api_key") String key, @Query("artist") String artist, @Query("track") String track);
}
