package com.tanuj.nowplayinghistory.viewmodels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;
import androidx.paging.LivePagedListBuilder;
import androidx.paging.PagedList;

import com.tanuj.nowplayinghistory.persistence.Song;
import com.tanuj.nowplayinghistory.persistence.SongDao;

public class SongsListViewModel extends ViewModel {
    private LiveData<PagedList<Song>> data;

    public void init(SongDao songDao, long minTimestamp, boolean favs) {
        if (favs) {
            data = new LivePagedListBuilder<>(songDao.loadAllFavSongs(minTimestamp), 20).build();
        } else {
            data = new LivePagedListBuilder<>(songDao.loadAllSongs(minTimestamp), 20).build();
        }
    }

    public LiveData<PagedList<Song>> getData() {
        return data;
    }
}
