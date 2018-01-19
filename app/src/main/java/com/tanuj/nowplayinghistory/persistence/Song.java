package com.tanuj.nowplayinghistory.persistence;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

import com.tanuj.nowplayinghistory.Utils;

@Entity(tableName = "songs")
public class Song {
    @PrimaryKey
    private long timestamp;
    private String songText;
    private double lat = -1;
    private double lon = -1;

    public Song(long timestamp, String songText) {
        this.timestamp = timestamp;
        this.songText = songText;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public String getTimestampText() {
        return Utils.getTimestampString(timestamp);
    }

    public String getSongText() {
        return songText;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLon() {
        return lon;
    }

    public void setLon(double lon) {
        this.lon = lon;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Song song = (Song) o;

        return songText.equals(song.songText);
    }

    @Override
    public int hashCode() {
        return songText.hashCode();
    }
}
