package com.tanuj.nowplayinghistory.persistence;

import android.arch.persistence.room.Entity;

@Entity(tableName = "fav_songs")
public class FavSong extends Song {
    public FavSong(long timestamp, String songText) {
        super(timestamp, songText);
    }
}
