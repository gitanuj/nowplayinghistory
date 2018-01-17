package com.tanuj.nowplayinghistory.fragments;

import android.Manifest;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.clustering.ClusterManager;
import com.tanuj.nowplayinghistory.App;
import com.tanuj.nowplayinghistory.MapItem;
import com.tanuj.nowplayinghistory.R;
import com.tanuj.nowplayinghistory.Utils;
import com.tanuj.nowplayinghistory.persistence.Song;
import com.tanuj.nowplayinghistory.viewmodels.FavoritesViewModel;
import com.tanuj.nowplayinghistory.viewmodels.RecentsViewModel;

import java.util.ArrayList;
import java.util.List;

public class MapFragment extends SupportMapFragment implements OnMapReadyCallback {

    private static final String EXTRA_SHOW_FAVORITES = "show_favorites";
    private static final String EXTRA_MIN_TIMESTAMP = "min_timestamp";

    private boolean showFavorites;
    private long minTimestamp;
    private List<Song> songs = new ArrayList<>();
    private GoogleMap googleMap;
    private ClusterManager<MapItem> clusterManager;
    private Snackbar locationAccessSnackbar;

    public static MapFragment newInstance(boolean showFavorites, long minTimestamp) {
        MapFragment fragment = new MapFragment();

        Bundle args = new Bundle();
        args.putBoolean(EXTRA_SHOW_FAVORITES, showFavorites);
        args.putLong(EXTRA_MIN_TIMESTAMP, minTimestamp);
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        if (getArguments() != null) {
            showFavorites = getArguments().getBoolean(EXTRA_SHOW_FAVORITES);
            minTimestamp = getArguments().getLong(EXTRA_MIN_TIMESTAMP);
        }

        getMapAsync(this);

        if (showFavorites) {
            initFavoritesViewModel();
        } else {
            initRecentsViewModel();
        }
    }

    @Override
    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        locationAccessSnackbar = Snackbar.make(viewGroup.findViewById(R.id.container), "Need location access", Snackbar.LENGTH_INDEFINITE)
                .setAction("Grant access", v -> requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 0));
        return super.onCreateView(layoutInflater, viewGroup, bundle);
    }

    @Override
    public void onPause() {
        super.onPause();

        locationAccessSnackbar.dismiss();
    }

    @Override
    public void onResume() {
        super.onResume();

        if (!Utils.isLocationAccessGranted()) {
            locationAccessSnackbar.show();
        }
        tryEnableLocationOnMap();
    }

    private void initRecentsViewModel() {
        RecentsViewModel viewModel = ViewModelProviders.of(this).get(RecentsViewModel.class);
        viewModel.init(App.getDb().recentsDao(), minTimestamp);
        viewModel.getData().observe(this, songs -> {
            if (songs != null) {
                this.songs.clear();
                this.songs.addAll(songs);

                if (googleMap != null) {
                    setupClusters(googleMap, this.songs);
                }
            }
        });
    }

    private void initFavoritesViewModel() {
        FavoritesViewModel viewModel = ViewModelProviders.of(this).get(FavoritesViewModel.class);
        viewModel.init(App.getDb().favSongDao(), minTimestamp);
        viewModel.getData().observe(this, songs -> {
            if (songs != null) {
                this.songs.clear();
                this.songs.addAll(songs);

                if (googleMap != null) {
                    setupClusters(this.googleMap, this.songs);
                }
            }
        });
    }

    @Override
    public void onMapReady(GoogleMap map) {
        googleMap = map;
        tryEnableLocationOnMap();
        setupClusters(map, songs);
    }

    private void tryEnableLocationOnMap() {
        if (googleMap != null && Utils.isLocationAccessGranted()) {
            googleMap.setMyLocationEnabled(true);
        }
    }

    private void setupClusters(GoogleMap map, List<Song> songs) {
        if (clusterManager == null) {
            clusterManager = new ClusterManager<>(App.getContext(), map);
            clusterManager.setOnClusterItemInfoWindowClickListener(mapItem -> {
                Utils.launchMusicApp(mapItem.getTitle());
            });
            map.setOnCameraIdleListener(clusterManager);
            map.setOnInfoWindowClickListener(clusterManager);
        }

        // Reset cluster data
        clusterManager.clearItems();

        // Position the map to first song
        if (songs.size() > 0) {
            Song song = songs.get(0);
            centerAt(map, new LatLng(song.getLat(), song.getLon()));
        }

        // Add data to clusters
        for (Song song : songs) {
            // Skip invalid locations
            if (song.getLat() == -1 && song.getLon() == -1) {
                continue;
            }
            clusterManager.addItem(new MapItem(song));
        }
    }

    private void centerAt(GoogleMap map, LatLng latLng) {
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 14));
    }
}