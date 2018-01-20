package com.tanuj.nowplayinghistory.viewmodels;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;
import android.arch.paging.LivePagedListBuilder;
import android.arch.paging.PagedList;

import com.tanuj.nowplayinghistory.persistence.FavoritesDao;
import com.tanuj.nowplayinghistory.persistence.Song;

public class FavoritesViewModel extends ViewModel {
    private LiveData<PagedList<Song>> data;

    public void init(FavoritesDao favoritesDao, long minTimestamp) {
        data = new LivePagedListBuilder<>(favoritesDao.loadAll(minTimestamp), 20).build();
    }

    public LiveData<PagedList<Song>> getData() {
        return data;
    }
}
