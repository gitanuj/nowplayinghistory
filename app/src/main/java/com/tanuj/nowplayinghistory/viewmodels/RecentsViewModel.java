package com.tanuj.nowplayinghistory.viewmodels;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;

import com.tanuj.nowplayinghistory.persistence.RecentsDao;
import com.tanuj.nowplayinghistory.persistence.Song;

import java.util.List;

public class RecentsViewModel extends ViewModel {
    private LiveData<List<Song>> data;

    public void init(RecentsDao recentsDao, long minTimestamp) {
        data = recentsDao.loadAll(minTimestamp);
    }

    public LiveData<List<Song>> getData() {
        return data;
    }
}
