package com.tanuj.nowplayinghistory.persistence;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.List;

@Dao
public interface RecentsDao {

    @Insert
    public void insert(Song... songs);

    @Delete
    public void delete(Song... songs);

    @Query("SELECT * FROM songs WHERE timestamp > :minTimestamp ORDER BY timestamp DESC")
    public LiveData<List<Song>> loadAll(long minTimestamp);

    @Query("SELECT * FROM songs WHERE timestamp = (SELECT MAX(timestamp) FROM songs)")
    public Song loadLatest();
}
