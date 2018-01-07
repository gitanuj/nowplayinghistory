package com.tanuj.nowplayinghistory.persistence;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.List;

@Dao
public interface SongHistoryDao {

    @Insert
    public void insertSongs(Song... songs);

    @Delete
    public void deleteSongs(Song... songs);

    @Query("SELECT * FROM songs ORDER BY timestamp DESC")
    public LiveData<List<Song>> loadAllSongs();

    @Query("SELECT * FROM songs WHERE timestamp = (SELECT MAX(timestamp) FROM songs)")
    public Song loadLatestSong();
}
