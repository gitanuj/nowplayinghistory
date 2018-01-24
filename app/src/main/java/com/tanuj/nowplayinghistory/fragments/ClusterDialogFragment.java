package com.tanuj.nowplayinghistory.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetDialogFragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.maps.android.clustering.Cluster;
import com.tanuj.nowplayinghistory.MapItem;
import com.tanuj.nowplayinghistory.R;
import com.tanuj.nowplayinghistory.Utils;
import com.tanuj.nowplayinghistory.adapters.SongsListAdapter;
import com.tanuj.nowplayinghistory.persistence.Song;

import java.util.ArrayList;
import java.util.List;

public class ClusterDialogFragment extends BottomSheetDialogFragment implements OnMapReadyCallback {

    private static final String EXTRA_CLUSTER_ITEMS = "cluster";
    private static final String EXTRA_CLUSTER_POSITION = "cluster-position";

    private MapView mapView;
    private RecyclerView recyclerView;
    private ArrayList<MapItem> mapItems;
    private LatLng position;

    public static ClusterDialogFragment newInstance(Cluster<MapItem> cluster) {
        ClusterDialogFragment fragment = new ClusterDialogFragment();

        Bundle args = new Bundle();
        args.putParcelableArrayList(EXTRA_CLUSTER_ITEMS, new ArrayList<>(cluster.getItems()));
        args.putParcelable(EXTRA_CLUSTER_POSITION, cluster.getPosition());
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public int getTheme() {
        return R.style.BottomSheetDialogHack;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mapItems = getArguments().getParcelableArrayList(EXTRA_CLUSTER_ITEMS);
            position = getArguments().getParcelable(EXTRA_CLUSTER_POSITION);
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.cluster_dialog, container, false);

        mapView = view.findViewById(R.id.map);
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(this);
        recyclerView = view.findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        SongsListAdapter adapter = new SongsListAdapter();
        adapter.setList(getSongs());
        recyclerView.setAdapter(adapter);

        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mapView.onDestroy();
    }

    private List<Song> getSongs() {
        List<Song> songs = new ArrayList<>();
        for (MapItem mapItem : mapItems) {
            songs.add(mapItem.getSong());
        }
        return songs;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        Utils.styleMap(getResources(), googleMap);
        googleMap.addMarker(new MarkerOptions().position(position));
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(position, 16));
    }

    @Override
    public void onStart() {
        super.onStart();
        mapView.onStart();
    }

    @Override
    public void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    public void onStop() {
        super.onStop();
        mapView.onStop();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }
}
