package com.tanuj.nowplayinghistory.viewmodels;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;

import com.tanuj.nowplayinghistory.persistence.FavSong;
import com.tanuj.nowplayinghistory.persistence.FavSongDao;

import java.util.List;

public class FavoritesViewModel extends ViewModel {
    private LiveData<List<FavSong>> data;

    public void init(FavSongDao favSongDao) {
        data = favSongDao.loadAllSongs();
    }

    public LiveData<List<FavSong>> getData() {
        return data;
    }
}
