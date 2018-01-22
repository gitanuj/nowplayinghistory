package com.tanuj.nowplayinghistory.fragments;

import android.Manifest;
import android.annotation.SuppressLint;
import android.arch.lifecycle.ViewModelProviders;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.maps.android.clustering.ClusterManager;
import com.tanuj.nowplayinghistory.App;
import com.tanuj.nowplayinghistory.MapItem;
import com.tanuj.nowplayinghistory.R;
import com.tanuj.nowplayinghistory.Utils;
import com.tanuj.nowplayinghistory.persistence.Song;
import com.tanuj.nowplayinghistory.viewmodels.SongsMapViewModel;

public class MapFragment extends SupportMapFragment implements OnMapReadyCallback {

    private static final String EXTRA_SHOW_FAVORITES = "show_favorites";
    private static final String EXTRA_MIN_TIMESTAMP = "min_timestamp";

    private boolean showFavorites;
    private long minTimestamp;
    private Snackbar locationAccessSnackbar;
    private SongsMapViewModel viewModel;
    private GoogleMap googleMap;
    private CameraPosition prevCameraPosition;

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

        viewModel = ViewModelProviders.of(this).get(SongsMapViewModel.class);
        getMapAsync(this);
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

        if (googleMap != null) {
            tryEnableLocationOnMap(googleMap);
        }
    }

    @Override
    public void onMapReady(GoogleMap map) {
        googleMap = map;
        tryEnableLocationOnMap(map);
        new InitClusterManagerTask(map).execute();
    }

    @SuppressLint("MissingPermission")
    private void tryEnableLocationOnMap(@NonNull GoogleMap map) {
        if (Utils.isLocationAccessGranted()) {
            map.setMyLocationEnabled(true);
        }
    }

    private void initClusterManager(GoogleMap map) {
        ClusterManager<MapItem> clusterManager = new ClusterManager<>(App.getContext(), map);
        clusterManager.setOnClusterItemInfoWindowClickListener(mapItem -> {
            Utils.launchMusicApp(mapItem.getTitle());
        });
        clusterManager.setOnClusterClickListener(cluster -> {
            ClusterDialogFragment fragment = ClusterDialogFragment.newInstance(cluster);
            fragment.show(getFragmentManager(), "cluster-dialog");
            return false;
        });

        map.setOnMarkerClickListener(clusterManager);
        map.setOnInfoWindowClickListener(clusterManager);
        map.setOnCameraIdleListener(() -> {
            clusterManager.onCameraIdle();

            CameraPosition cameraPosition = map.getCameraPosition();
            if (prevCameraPosition == null || prevCameraPosition.zoom != cameraPosition.zoom) {
                prevCameraPosition = cameraPosition;

                LatLngBounds bounds = map.getProjection().getVisibleRegion().latLngBounds;
                viewModel.init(App.getDb().songDao(), bounds, minTimestamp, showFavorites);
                viewModel.getData().observe(this, songs -> {
                    clusterManager.clearItems();
                    for (Song song : songs) {
                        if (song.getLat() == -1 && song.getLon() == -1) {
                            // Location not available
                            continue;
                        }
                        clusterManager.addItem(new MapItem(song));
                    }
                    clusterManager.cluster();
                });
            }
        });
    }

    @SuppressLint("StaticFieldLeak")
    private class InitClusterManagerTask extends AsyncTask<Void, Void, Location> {

        private GoogleMap map;

        InitClusterManagerTask(GoogleMap map) {
            this.map = map;
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
            initClusterManager(map);
        }
    }
}
