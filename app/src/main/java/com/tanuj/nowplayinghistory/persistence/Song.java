package com.tanuj.nowplayinghistory.persistence;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

@Entity(tableName = "songs")
public class Song {
    @PrimaryKey
    private long timestamp;
    private String songText;

    public Song(long timestamp, String songText) {
        this.timestamp = timestamp;
        this.songText = songText;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public String getSongText() {
        return songText;
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
