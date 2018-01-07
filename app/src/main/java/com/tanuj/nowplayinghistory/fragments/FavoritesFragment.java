package com.tanuj.nowplayinghistory.fragments;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.graphics.Canvas;
import android.os.Bundle;
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
import com.tanuj.nowplayinghistory.SwipeAction;
import com.tanuj.nowplayinghistory.Utils;
import com.tanuj.nowplayinghistory.adapters.SongsAdapter;
import com.tanuj.nowplayinghistory.persistence.FavSong;
import com.tanuj.nowplayinghistory.persistence.Song;
import com.tanuj.nowplayinghistory.viewmodels.FavoritesViewModel;

import java.util.ArrayList;
import java.util.List;

public class FavoritesFragment extends Fragment {

    private FavoritesViewModel viewModel;
    private SongsAdapter songsAdapter;
    private ItemTouchHelper itemTouchHelper;
    private LinearLayoutManager linearLayoutManager;

    private RecyclerView favoritesRecyclerView;

    private SwipeAction removeSwipeAction = new SwipeAction(SwipeAction.Dir.LEFT, "Remove");

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        songsAdapter = new SongsAdapter(new ArrayList<>());
        songsAdapter.setHasStableIds(true);

        viewModel = ViewModelProviders.of(this).get(FavoritesViewModel.class);
        viewModel.init(App.getDb().favSongDao());

        final Observer<List<FavSong>> favoritesObserver = favSongs -> {
            if (favSongs != null) {
                songsAdapter.setFavSongsData(favSongs);
            }
        };

        viewModel.getData().observe(this, favoritesObserver);

        ItemTouchHelper.SimpleCallback simpleItemTouchCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {

            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(final RecyclerView.ViewHolder viewHolder, int swipeDir) {
                final int position = viewHolder.getAdapterPosition();
                Song song = songsAdapter.getItem(position);
                final FavSong favSong = new FavSong(song.getTimestamp(), song.getSongText());

                if (swipeDir == ItemTouchHelper.LEFT) {
                    Utils.executeAsync(() -> App.getDb().favSongDao().deleteSongs(favSong));

                    String message = "Removed " + favSong.getSongText() + " from favorites";
                    Snackbar.make(favoritesRecyclerView, message, Snackbar.LENGTH_SHORT)
                            .setAction("Undo", (view) -> Utils.executeAsync(() -> App.getDb().favSongDao().insertSongs(favSong)))
                            .show();
                }
            }

            @Override
            public void onChildDraw(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
                View itemView = viewHolder.itemView;
                if (dX < 0) {
                    removeSwipeAction.draw(c, itemView, dX);
                }

                super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
            }
        };

        itemTouchHelper = new ItemTouchHelper(simpleItemTouchCallback);

        linearLayoutManager = new LinearLayoutManager(getContext()) {
            @Override
            public boolean supportsPredictiveItemAnimations() {
                return true;
            }
        };
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fav_songs, container, false);
        favoritesRecyclerView = view.findViewById(R.id.song_history_recycler_view);
        favoritesRecyclerView.setHasFixedSize(true);
        favoritesRecyclerView.setLayoutManager(linearLayoutManager);
        favoritesRecyclerView.setAdapter(songsAdapter);
        itemTouchHelper.attachToRecyclerView(favoritesRecyclerView);
        return view;
    }
}
