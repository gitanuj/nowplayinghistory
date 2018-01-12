package com.tanuj.nowplayinghistory.callbacks;

import android.database.sqlite.SQLiteConstraintException;
import android.graphics.Canvas;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.View;

import com.tanuj.nowplayinghistory.App;
import com.tanuj.nowplayinghistory.SwipeAction;
import com.tanuj.nowplayinghistory.Utils;
import com.tanuj.nowplayinghistory.adapters.SongsAdapter;
import com.tanuj.nowplayinghistory.persistence.FavSong;
import com.tanuj.nowplayinghistory.persistence.Song;

public class RecentsItemTouchCallback extends ItemTouchHelper.SimpleCallback {

    private final SwipeAction deleteSwipeAction = new SwipeAction(SwipeAction.Dir.LEFT, "Delete");
    private final SwipeAction favoriteSwipeAction = new SwipeAction(SwipeAction.Dir.RIGHT, "Favorite");

    private final View anchor;
    private final SongsAdapter songsAdapter;

    public RecentsItemTouchCallback(View anchor, SongsAdapter songsAdapter) {
        super(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT);
        this.anchor = anchor;
        this.songsAdapter = songsAdapter;
    }

    @Override
    public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
        return false;
    }

    @Override
    public void onSwiped(final RecyclerView.ViewHolder viewHolder, int swipeDir) {
        final int position = viewHolder.getAdapterPosition();
        final Song song = songsAdapter.getItem(position);

        if (swipeDir == ItemTouchHelper.LEFT) {
            Utils.executeAsync(() -> App.getDb().recentsDao().delete(song));

            String message = "Deleted " + song.getSongText();
            Snackbar.make(anchor, message, Snackbar.LENGTH_SHORT)
                    .setAction("Undo", (view) -> Utils.executeAsync(() -> App.getDb().recentsDao().insert(song)))
                    .show();
        } else {
            final FavSong favSong = new FavSong(song);
            Utils.executeAsync(() -> {
                try {
                    App.getDb().favSongDao().insert(favSong);
                } catch (SQLiteConstraintException e) {
                }
            }, () -> songsAdapter.notifyItemChanged(position));

            String message = "Added " + song.getSongText() + " to favorites";
            Snackbar.make(anchor, message, Snackbar.LENGTH_SHORT)
                    .setAction("Undo", (view) -> Utils.executeAsync(() -> App.getDb().favSongDao().delete(favSong)))
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
}
