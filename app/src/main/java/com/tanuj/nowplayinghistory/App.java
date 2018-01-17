package com.tanuj.nowplayinghistory;

import android.app.Application;
import android.arch.persistence.room.Room;
import android.content.Context;

import com.tanuj.nowplayinghistory.persistence.AppDatabase;

public class App extends Application {

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
