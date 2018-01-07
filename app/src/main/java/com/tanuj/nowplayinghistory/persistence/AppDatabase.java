package com.tanuj.nowplayinghistory.persistence;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;

@Database(entities = {Song.class, FavSong.class}, version = 4, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {

    public abstract SongHistoryDao songHistoryDao();

    public abstract FavSongDao favSongDao();
}
