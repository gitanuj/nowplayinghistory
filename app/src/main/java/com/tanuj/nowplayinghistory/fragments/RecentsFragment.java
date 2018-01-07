package com.tanuj.nowplayinghistory.fragments;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.database.sqlite.SQLiteConstraintException;
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
import com.tanuj.nowplayinghistory.viewmodels.RecentsViewModel;

import java.util.ArrayList;
import java.util.List;

public class RecentsFragment extends Fragment {

    private RecentsViewModel viewModel;
    private SongsAdapter songsAdapter;
    private ItemTouchHelper itemTouchHelper;
    private LinearLayoutManager linearLayoutManager;

    private RecyclerView recentsRecyclerView;

    private SwipeAction deleteSwipeAction = new SwipeAction(SwipeAction.Dir.LEFT, "Delete");
    private SwipeAction favoriteSwipeAction = new SwipeAction(SwipeAction.Dir.RIGHT, "Favorite");

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        songsAdapter = new SongsAdapter(new ArrayList<>());
        songsAdapter.setHasStableIds(true);

        viewModel = ViewModelProviders.of(this).get(RecentsViewModel.class);
        viewModel.init(App.getDb().songHistoryDao());

        final Observer<List<Song>> songHistoryObserver = songs -> {
            if (songs != null) {
                songsAdapter.setSongsData(songs);
            }
        };

        viewModel.getData().observe(this, songHistoryObserver);

        ItemTouchHelper.SimpleCallback simpleItemTouchCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {

            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(final RecyclerView.ViewHolder viewHolder, int swipeDir) {
                final int position = viewHolder.getAdapterPosition();
                final Song song = songsAdapter.getItem(position);

                if (swipeDir == ItemTouchHelper.LEFT) {
                    Utils.executeAsync(() -> App.getDb().songHistoryDao().deleteSongs(song));

                    String message = "Deleted " + song.getSongText();
                    Snackbar.make(recentsRecyclerView, message, Snackbar.LENGTH_SHORT)
                            .setAction("Undo", (view) -> Utils.executeAsync(() -> App.getDb().songHistoryDao().insertSongs(song)))
                            .show();
                } else {
                    final FavSong favSong = new FavSong(song.getTimestamp(), song.getSongText());
                    Utils.executeAsync(() -> {
                        try {
                            App.getDb().favSongDao().insertSongs(favSong);
                        } catch (SQLiteConstraintException e) {
                        }
                    }, () -> songsAdapter.notifyItemChanged(position));

                    String message = "Added " + song.getSongText() + " to favorites";
                    Snackbar.make(recentsRecyclerView, message, Snackbar.LENGTH_SHORT)
                            .setAction("Undo", (view) -> Utils.executeAsync(() -> App.getDb().favSongDao().deleteSongs(favSong)))
                            .show();
                }
            }

            @Override
            public void onChildDraw(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
                View itemView = viewHolder.itemView;
                if (dX < 0) {
                    deleteSwipeAction.draw(c, itemView, dX);
                } else {
                    favoriteSwipeAction.draw(c, itemView, dX);
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
        View view = inflater.inflate(R.layout.song_history, container, false);
        recentsRecyclerView = view.findViewById(R.id.song_history_recycler_view);
        recentsRecyclerView.setHasFixedSize(true);
        recentsRecyclerView.setLayoutManager(linearLayoutManager);
        recentsRecyclerView.setAdapter(songsAdapter);
        itemTouchHelper.attachToRecyclerView(recentsRecyclerView);
        return view;
    }
}
