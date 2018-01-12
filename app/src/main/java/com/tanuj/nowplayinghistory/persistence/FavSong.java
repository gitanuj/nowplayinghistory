package com.tanuj.nowplayinghistory.persistence;

import android.arch.persistence.room.Entity;

@Entity(tableName = "fav_songs")
public class FavSong extends Song {
    public FavSong(Song song) {
        this(song.getTimestamp(), song.getSongText());
        setLat(song.getLat());
        setLon(song.getLon());
    }

    public FavSong(long timestamp, String songText) {
        super(timestamp, songText);
    }
}
