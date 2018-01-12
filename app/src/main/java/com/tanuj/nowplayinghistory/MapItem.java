package com.tanuj.nowplayinghistory;

import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.clustering.ClusterItem;
import com.tanuj.nowplayinghistory.persistence.Song;

public class MapItem implements ClusterItem {

    private Song song;

    public MapItem(Song song) {
        this.song = song;
    }

    @Override
    public LatLng getPosition() {
        return new LatLng(song.getLat(), song.getLon());
    }

    @Override
    public String getTitle() {
        return song.getSongText();
    }

    @Override
    public String getSnippet() {
        return Utils.getTimestampString(song.getTimestamp());
    }
}
