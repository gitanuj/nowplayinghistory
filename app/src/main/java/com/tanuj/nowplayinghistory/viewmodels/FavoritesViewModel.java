package com.tanuj.nowplayinghistory.viewmodels;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;

import com.tanuj.nowplayinghistory.persistence.FavSong;
import com.tanuj.nowplayinghistory.persistence.FavoritesDao;

import java.util.List;

public class FavoritesViewModel extends ViewModel {
    private LiveData<List<FavSong>> data;

    public void init(FavoritesDao favoritesDao, long minTimestamp) {
        data = favoritesDao.loadAll(minTimestamp);
    }

    public LiveData<List<FavSong>> getData() {
        return data;
    }
}
