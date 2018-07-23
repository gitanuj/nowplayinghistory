package com.tanuj.nowplayinghistory.lastfm.pojos;

import com.squareup.moshi.Json;

public class TrackInfo {

    @Json(name = "track")
    private Track track;

    public Track getTrack() {
        return track;
    }

    public void setTrack(Track track) {
        this.track = track;
    }
}
