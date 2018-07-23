package com.tanuj.nowplayinghistory.lastfm;

import com.tanuj.nowplayinghistory.lastfm.pojos.TrackInfo;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface LastFmService {

    @GET("/2.0/?method=track.getInfo&api_key=31c90de21f9ddc4afb4dbc11f8807df3&format=json")
    Call<TrackInfo> trackInfo(@Query("artist") String artist, @Query("track") String track);
}
