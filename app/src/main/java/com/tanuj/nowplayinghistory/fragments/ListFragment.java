package com.tanuj.nowplayinghistory.fragments;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tanuj.nowplayinghistory.App;
import com.tanuj.nowplayinghistory.R;
import com.tanuj.nowplayinghistory.Utils;
import com.tanuj.nowplayinghistory.adapters.SongsAdapter;
import com.tanuj.nowplayinghistory.callbacks.FavoritesItemTouchCallback;
import com.tanuj.nowplayinghistory.callbacks.RecentsItemTouchCallback;
import com.tanuj.nowplayinghistory.viewmodels.FavoritesViewModel;
import com.tanuj.nowplayinghistory.viewmodels.RecentsViewModel;

import java.util.ArrayList;

public class ListFragment extends Fragment {

    private static final String EXTRA_SHOW_FAVORITES = "show_favorites";
    private static final String EXTRA_MIN_TIMESTAMP = "min_timestamp";

    private boolean showFavorites;
    private long minTimestamp;
    private SongsAdapter songsAdapter;
    private Snackbar notificationAccessSnackbar;

    public static ListFragment newInstance(boolean showFavorites, long minTimestamp) {
        ListFragment fragment = new ListFragment();

        Bundle args = new Bundle();
        args.putBoolean(EXTRA_SHOW_FAVORITES, showFavorites);
        args.putLong(EXTRA_MIN_TIMESTAMP, minTimestamp);
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            showFavorites = getArguments().getBoolean(EXTRA_SHOW_FAVORITES);
            minTimestamp = getArguments().getLong(EXTRA_MIN_TIMESTAMP);
        }

        songsAdapter = new SongsAdapter(new ArrayList<>());
        songsAdapter.setHasStableIds(true);

        if (showFavorites) {
            initFavoritesViewModel();
        } else {
            initRecentsViewModel();
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        notificationAccessSnackbar = Snackbar.make(container.findViewById(R.id.container),
                "Need notification access", Snackbar.LENGTH_INDEFINITE)
                .setAction("Grant access", v -> startActivity(new Intent(Settings.ACTION_NOTIFICATION_LISTENER_SETTINGS)));

        View view = inflater.inflate(R.layout.list_songs, container, false);
        RecyclerView recyclerView = view.findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()) {
            @Override
            public boolean supportsPredictiveItemAnimations() {
                return true;
            }
        });
        recyclerView.setAdapter(songsAdapter);
        initItemTouchHelper(recyclerView);
        return view;
    }

    @Override
    public void onPause() {
        super.onPause();
        notificationAccessSnackbar.dismiss();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (!Utils.isNotificationAccessGranted()) {
            notificationAccessSnackbar.show();
        }
    }

    private void initRecentsViewModel() {
        RecentsViewModel viewModel = ViewModelProviders.of(this).get(RecentsViewModel.class);
        viewModel.init(App.getDb().recentsDao(), minTimestamp);
        viewModel.getData().observe(this, songs -> {
            if (songs != null) {
                songsAdapter.setSongsData(songs);
            }
        });
    }

    private void initFavoritesViewModel() {
        FavoritesViewModel viewModel = ViewModelProviders.of(this).get(FavoritesViewModel.class);
        viewModel.init(App.getDb().favSongDao(), minTimestamp);
        viewModel.getData().observe(this, favSongs -> {
            if (favSongs != null) {
                songsAdapter.setFavSongsData(favSongs);
            }
        });
    }

    private void initItemTouchHelper(RecyclerView recyclerView) {
        ItemTouchHelper.SimpleCallback callback;
        if (showFavorites) {
            callback = new FavoritesItemTouchCallback(recyclerView, songsAdapter);
        } else {
            callback = new RecentsItemTouchCallback(recyclerView, songsAdapter);
        }
        new ItemTouchHelper(callback).attachToRecyclerView(recyclerView);
    }
}
