package com.tanuj.nowplayinghistory.persistence;

import androidx.room.Database;
import androidx.room.RoomDatabase;

@Database(entities = {Song.class, FavSong.class}, version = 5, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {

    public abstract SongDao songDao();
}
