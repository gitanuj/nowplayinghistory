package com.tanuj.nowplayinghistory;

import android.app.Application;
import android.content.Context;

import com.crashlytics.android.Crashlytics;
import com.tanuj.nowplayinghistory.lastfm.GlideApp;
import com.tanuj.nowplayinghistory.lastfm.LastFmService;
import com.tanuj.nowplayinghistory.persistence.AppDatabase;

import androidx.appcompat.app.AppCompatDelegate;
import androidx.room.Room;
import io.fabric.sdk.android.Fabric;
import retrofit2.Retrofit;
import retrofit2.converter.moshi.MoshiConverterFactory;

import static androidx.appcompat.app.AppCompatDelegate.MODE_NIGHT_AUTO;

public class App extends Application {

    static {
        AppCompatDelegate.setDefaultNightMode(MODE_NIGHT_AUTO);
    }

    private static Context CONTEXT;
    private static AppDatabase DB;
    private static LastFmService LAST_FM_SERVICE;

    @Override
    public void onCreate() {
        super.onCreate();
        Fabric.with(this, new Crashlytics());

        CONTEXT = getApplicationContext();
        DB = Room.databaseBuilder(getApplicationContext(), AppDatabase.class, "app-db").fallbackToDestructiveMigration().build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://ws.audioscrobbler.com")
                .addConverterFactory(MoshiConverterFactory.create())
                .build();
        LAST_FM_SERVICE = retrofit.create(LastFmService.class);
    }

    public static Context getContext() {
        return CONTEXT;
    }

    public static AppDatabase getDb() {
        return DB;
    }

    public static LastFmService getLastFmService() {
        return LAST_FM_SERVICE;
    }
}
