package com.tanuj.nowplayinghistory.viewmodels;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;

import com.tanuj.nowplayinghistory.persistence.Song;
import com.tanuj.nowplayinghistory.persistence.SongHistoryDao;

import java.util.List;

public class RecentsViewModel extends ViewModel {
    private LiveData<List<Song>> data;

    public void init(SongHistoryDao songHistoryDao) {
        data = songHistoryDao.loadAllSongs();
    }

    public LiveData<List<Song>> getData() {
        return data;
    }
}
