package com.tanuj.nowplayinghistory.viewmodels;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;
import android.arch.paging.LivePagedListBuilder;
import android.arch.paging.PagedList;

import com.tanuj.nowplayinghistory.persistence.RecentsDao;
import com.tanuj.nowplayinghistory.persistence.Song;

public class RecentsViewModel extends ViewModel {
    private LiveData<PagedList<Song>> data;

    public void init(RecentsDao recentsDao, long minTimestamp) {
        data = new LivePagedListBuilder<>(recentsDao.loadAll(minTimestamp), 20).build();
    }

    public LiveData<PagedList<Song>> getData() {
        return data;
    }
}
