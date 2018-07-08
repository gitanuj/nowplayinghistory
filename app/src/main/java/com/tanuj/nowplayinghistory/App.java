package com.tanuj.nowplayinghistory;

import android.app.Application;
import androidx.room.Room;
import android.content.Context;
import androidx.appcompat.app.AppCompatDelegate;

import com.tanuj.nowplayinghistory.persistence.AppDatabase;
import com.tanuj.nowplayinghistory.persistence.Song;

import static androidx.appcompat.app.AppCompatDelegate.MODE_NIGHT_NO;
import static androidx.appcompat.app.AppCompatDelegate.MODE_NIGHT_YES;

public class App extends Application {

    static {
        AppCompatDelegate.setDefaultNightMode(MODE_NIGHT_YES);
    }

    private static Context CONTEXT;
    private static AppDatabase DB;

    @Override
    public void onCreate() {
        super.onCreate();

        CONTEXT = getApplicationContext();
        DB = Room.databaseBuilder(getApplicationContext(), AppDatabase.class, "app-db").fallbackToDestructiveMigration().build();

        Utils.executeAsync(() -> {
            for (int i = 0; i < 20; i++) {
                try {
                    Song song = new Song(i, "song by artist");
                    song.setLat(10);
                    song.setLon(10);
                    DB.songDao().insert(song);
                } catch (Exception e) {
                }
            }
        });
    }

    public static Context getContext() {
        return CONTEXT;
    }

    public static AppDatabase getDb() {
        return DB;
    }
}
