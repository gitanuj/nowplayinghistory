package com.tanuj.nowplayinghistory.persistence;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.List;

@Dao
public interface FavSongDao {

    @Insert
    public void insertSongs(FavSong... songs);

    @Delete
    public void deleteSongs(FavSong... songs);

    @Query("SELECT * FROM fav_songs ORDER BY timestamp DESC")
    public LiveData<List<FavSong>> loadAllSongs();
}
