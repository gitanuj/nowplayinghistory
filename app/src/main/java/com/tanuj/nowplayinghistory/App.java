package com.tanuj.nowplayinghistory;

import android.app.Application;
import android.arch.persistence.room.Room;
import android.content.Context;
import android.support.v7.app.AppCompatDelegate;

import com.tanuj.nowplayinghistory.persistence.AppDatabase;

import static android.support.v7.app.AppCompatDelegate.MODE_NIGHT_AUTO;

public class App extends Application {

    static {
        AppCompatDelegate.setDefaultNightMode(MODE_NIGHT_AUTO);
    }

    public static final String PREF_KEY_SHOW_INTRO = "show_intro";
    public static final String PREF_KEY_SHOW_MAP = "show_map";
    public static final String PREF_KEY_FILTER_TIME = "filter_time";

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
