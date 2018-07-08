package com.tanuj.nowplayinghistory.persistence;

import androidx.lifecycle.LiveData;
import androidx.paging.DataSource;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

// Manages db operations for Song and FavSong

@Dao
public interface SongDao {

    // Song
    @Insert
    public void insert(Song... songs);

    @Delete
    public void delete(Song... songs);

    @Query("DELETE FROM songs WHERE timestamp < :maxTimestamp")
    public void deleteAllSongs(long maxTimestamp);

    @Query("SELECT * FROM songs WHERE timestamp > :minTimestamp ORDER BY timestamp DESC")
    public DataSource.Factory<Integer, Song> loadAllSongs(long minTimestamp);

    @Query("SELECT * FROM songs WHERE (lat BETWEEN :minLat AND :maxLat) AND ((lon BETWEEN :minLon1 AND :maxLon1) OR (lon BETWEEN :minLon2 AND :maxLon2)) AND (timestamp > :minTimestamp) ORDER BY timestamp DESC")
    public LiveData<List<Song>> loadAllSongs(double minLat, double maxLat, double minLon1, double maxLon1, double minLon2, double maxLon2, long minTimestamp);

    @Query("SELECT * FROM songs WHERE timestamp = (SELECT MAX(timestamp) FROM songs)")
    public Song loadLatestSong();

    // FavSong
    @Insert
    public void insert(FavSong... favSongs);

    @Delete
    public void delete(FavSong... favSongs);

    @Query("DELETE FROM fav_songs WHERE timestamp < :maxTimestamp")
    public void deleteAllFavSongs(long maxTimestamp);

    @Query("SELECT * FROM fav_songs WHERE timestamp > :minTimestamp ORDER BY timestamp DESC")
    public DataSource.Factory<Integer, Song> loadAllFavSongs(long minTimestamp);

    @Query("SELECT * FROM fav_songs WHERE (lat BETWEEN :minLat AND :maxLat) AND ((lon BETWEEN :minLon1 AND :maxLon1) OR (lon BETWEEN :minLon2 AND :maxLon2)) AND (timestamp > :minTimestamp) ORDER BY timestamp DESC")
    public LiveData<List<Song>> loadAllFavSongs(double minLat, double maxLat, double minLon1, double maxLon1, double minLon2, double maxLon2, long minTimestamp);
}
