package com.tanuj.nowplayinghistory.viewmodels;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;

import com.google.android.gms.maps.model.LatLngBounds;
import com.tanuj.nowplayinghistory.persistence.Song;
import com.tanuj.nowplayinghistory.persistence.SongDao;

import java.util.List;

public class SongsMapViewModel extends ViewModel {
    private LiveData<List<Song>> data;

    public void init(SongDao songDao, LatLngBounds bounds, long minTimestamp, boolean favs) {
        // [southwest.latitude, northeast.latitude]
        double minLat = bounds.southwest.latitude;
        double maxLat = bounds.northeast.latitude;

        double minLon1;
        double maxLon1;
        double minLon2;
        double maxLon2;

        if (bounds.southwest.longitude <= bounds.northeast.longitude) {
            // [southwest.longitude, northeast.longitude]
            minLon1 = bounds.southwest.longitude;
            maxLon1 = bounds.northeast.longitude;
            minLon2 = 0;
            maxLon2 = 0;
        } else {
            // [southwest.longitude, 180) ∪ [-180, northeast.longitude]
            minLon1 = bounds.southwest.longitude;
            maxLon1 = 180;
            minLon2 = -180;
            maxLon2 = bounds.northeast.longitude;
        }

        if (favs) {
            data = songDao.loadAllFavSongs(minLat, maxLat, minLon1, maxLon1, minLon2, maxLon2, minTimestamp);
        } else {
            data = songDao.loadAllSongs(minLat, maxLat, minLon1, maxLon1, minLon2, maxLon2, minTimestamp);
        }
    }

    public LiveData<List<Song>> getData() {
        return data;
    }
}
