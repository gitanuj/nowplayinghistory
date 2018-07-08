package com.tanuj.nowplayinghistory;

import android.app.Application;
import android.content.Context;

import com.tanuj.nowplayinghistory.persistence.AppDatabase;

import androidx.appcompat.app.AppCompatDelegate;
import androidx.room.Room;

import static androidx.appcompat.app.AppCompatDelegate.MODE_NIGHT_AUTO;

public class App extends Application {

    static {
        AppCompatDelegate.setDefaultNightMode(MODE_NIGHT_AUTO);
    }

    private static Context CONTEXT;
    private static AppDatabase DB;

    @Override
    public void onCreate() {
        super.onCreate();

        CONTEXT = getApplicationContext();
        DB = Room.databaseBuilder(getApplicationContext(), AppDatabase.class, "app-db").fallbackToDestructiveMigration().build();
    }

    public static Context getContext() {
        return CONTEXT;
    }

    public static AppDatabase getDb() {
        return DB;
    }
}
