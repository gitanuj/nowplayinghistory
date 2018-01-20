package com.tanuj.nowplayinghistory.persistence;

import android.arch.paging.DataSource;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

@Dao
public interface FavoritesDao {

    @Insert
    public void insert(FavSong... songs);

    @Delete
    public void delete(FavSong... songs);

    @Query("SELECT * FROM fav_songs WHERE timestamp > :minTimestamp ORDER BY timestamp DESC")
    public DataSource.Factory<Integer, Song> loadAll(long minTimestamp);
}
