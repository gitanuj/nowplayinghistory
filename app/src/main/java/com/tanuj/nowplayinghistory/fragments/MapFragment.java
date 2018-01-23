package com.tanuj.nowplayinghistory.fragments;

import android.Manifest;
import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.tanuj.nowplayinghistory.R;
import com.tanuj.nowplayinghistory.Utils;
import com.tanuj.nowplayinghistory.tasks.InitClusterManagerTask;

public class MapFragment extends SupportMapFragment implements OnMapReadyCallback {

    private static final String EXTRA_SHOW_FAVORITES = "show_favorites";
    private static final String EXTRA_MIN_TIMESTAMP = "min_timestamp";

    private boolean showFavorites;
    private long minTimestamp;
    private Snackbar locationAccessSnackbar;
    private GoogleMap googleMap;
    private InitClusterManagerTask initClusterManagerTask;

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
    }

    @Override
    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        locationAccessSnackbar = Snackbar.make(viewGroup.findViewById(R.id.container), "Need location access", Snackbar.LENGTH_INDEFINITE)
                .setAction("Grant access", v -> requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 0));
        return super.onCreateView(layoutInflater, viewGroup, bundle);
    }

    @Override
    public void onDestroyView() {
        if (initClusterManagerTask != null) {
            initClusterManagerTask.finish();
        }
        super.onDestroyView();
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
        initClusterManagerTask = new InitClusterManagerTask(map, showFavorites, minTimestamp);
        initClusterManagerTask.setOnClusterClickListener(cluster -> {
            ClusterDialogFragment.newInstance(cluster).show(getFragmentManager(), "cluster-dialog");
            return true;
        });
        initClusterManagerTask.setOnClusterItemInfoWindowClickListener(mapItem -> Utils.launchMusicApp(mapItem.getTitle()));
        initClusterManagerTask.execute();
    }

    @SuppressLint("MissingPermission")
    private void tryEnableLocationOnMap(@NonNull GoogleMap map) {
        if (Utils.isLocationAccessGranted()) {
            map.setMyLocationEnabled(true);
        }
    }
}
