package com.tanuj.nowplayinghistory.persistence;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;

@Database(entities = {Song.class, FavSong.class}, version = 5, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {

    public abstract RecentsDao recentsDao();

    public abstract FavoritesDao favSongDao();
}
