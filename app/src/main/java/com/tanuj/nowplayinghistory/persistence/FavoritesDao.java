package com.tanuj.nowplayinghistory.persistence;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.List;

@Dao
public interface FavoritesDao {

    @Insert
    public void insert(FavSong... songs);

    @Delete
    public void delete(FavSong... songs);

    @Query("SELECT * FROM fav_songs WHERE timestamp > :minTimestamp ORDER BY timestamp DESC")
    public LiveData<List<FavSong>> loadAll(long minTimestamp);
}
