package com.tanuj.nowplayinghistory.tasks;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import android.location.Location;
import android.os.AsyncTask;
import androidx.annotation.Nullable;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.maps.android.clustering.ClusterManager;
import com.tanuj.nowplayinghistory.App;
import com.tanuj.nowplayinghistory.MapItem;
import com.tanuj.nowplayinghistory.Utils;
import com.tanuj.nowplayinghistory.persistence.Song;
import com.tanuj.nowplayinghistory.persistence.SongDao;

import java.util.List;

public class InitClusterManagerTask extends AsyncTask<Void, Void, Location> implements GoogleMap.OnCameraIdleListener, Observer<List<Song>> {

    private final GoogleMap map;
    private final boolean showFavorites;
    private final long minTimestamp;
    private final ClusterManager<MapItem> clusterManager;
    private LatLngBounds prevLatLngBounds;
    private LiveData<List<Song>> prevData;
    private boolean skipReload;

    public InitClusterManagerTask(GoogleMap map, boolean showFavorites, long minTimestamp) {
        this.map = map;
        this.showFavorites = showFavorites;
        this.minTimestamp = minTimestamp;
        this.clusterManager = new ClusterManager<>(App.getContext(), map);

        clusterManager.setAnimation(false);
        clusterManager.setOnClusterItemClickListener(mapItem -> {
            skipReload = true;
            return false;
        });
    }

    public void setOnClusterItemInfoWindowClickListener(ClusterManager.OnClusterItemInfoWindowClickListener<MapItem> listener) {
        clusterManager.setOnClusterItemInfoWindowClickListener(listener);
    }

    public void setOnClusterClickListener(ClusterManager.OnClusterClickListener<MapItem> listener) {
        clusterManager.setOnClusterClickListener(listener);
    }

    @Override
    protected Location doInBackground(Void... voids) {
        return Utils.getCurrentLocation();
    }

    @Override
    protected void onPostExecute(Location location) {
        if (location != null) {
            map.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(location.getLatitude(), location.getLongitude()), 14));
        }

        map.setOnMarkerClickListener(clusterManager);
        map.setOnInfoWindowClickListener(clusterManager);
        map.setOnCameraIdleListener(this);
    }

    public void finish() {
        cancel(true);
        clusterManager.setOnClusterItemInfoWindowClickListener(null);
        clusterManager.setOnClusterClickListener(null);
        map.setOnMarkerClickListener(null);
        map.setOnInfoWindowClickListener(null);
        map.setOnCameraIdleListener(null);
        if (prevData != null) {
            prevData.removeObserver(this);
        }
    }

    private void reloadData(LatLngBounds latLngBounds) {
        if (prevData != null) {
            prevData.removeObserver(this);
        }
        prevData = getData(App.getDb().songDao(), latLngBounds, minTimestamp, showFavorites);
        prevData.observeForever(this);
    }

    @Override
    public void onCameraIdle() {
        if (skipReload) {
            skipReload = false;
            return;
        }

        LatLngBounds latLngBounds = map.getProjection().getVisibleRegion().latLngBounds;
        if (prevLatLngBounds == null || !prevLatLngBounds.equals(latLngBounds)) {
            prevLatLngBounds = latLngBounds;
            reloadData(latLngBounds);
        }
    }

    @Override
    public void onChanged(@Nullable List<Song> songs) {
        clusterManager.clearItems();
        for (Song song : songs) {
            if (song.getLat() == -1 && song.getLon() == -1) {
                // Location not available
                continue;
            }
            clusterManager.addItem(new MapItem(song));
        }
        clusterManager.cluster();
    }

    public LiveData<List<Song>> getData(SongDao songDao, LatLngBounds bounds, long minTimestamp, boolean favs) {
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
            return songDao.loadAllFavSongs(minLat, maxLat, minLon1, maxLon1, minLon2, maxLon2, minTimestamp);
        } else {
            return songDao.loadAllSongs(minLat, maxLat, minLon1, maxLon1, minLon2, maxLon2, minTimestamp);
        }
    }
}
