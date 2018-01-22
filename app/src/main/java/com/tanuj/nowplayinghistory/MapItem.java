package com.tanuj.nowplayinghistory;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.clustering.ClusterItem;
import com.tanuj.nowplayinghistory.persistence.Song;

public class MapItem implements ClusterItem, Parcelable {

    private Song song;

    public MapItem(Song song) {
        this.song = song;
    }

    protected MapItem(Parcel in) {
        song = in.readParcelable(Song.class.getClassLoader());
    }

    public static final Creator<MapItem> CREATOR = new Creator<MapItem>() {
        @Override
        public MapItem createFromParcel(Parcel in) {
            return new MapItem(in);
        }

        @Override
        public MapItem[] newArray(int size) {
            return new MapItem[size];
        }
    };

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

    public Song getSong() {
        return song;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(song, flags);
    }
}
