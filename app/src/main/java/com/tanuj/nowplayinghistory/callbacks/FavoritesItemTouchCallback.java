package com.tanuj.nowplayinghistory.callbacks;

import android.graphics.Canvas;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.View;

import com.tanuj.nowplayinghistory.App;
import com.tanuj.nowplayinghistory.SwipeAction;
import com.tanuj.nowplayinghistory.Utils;
import com.tanuj.nowplayinghistory.adapters.SongsPagedListAdapter;
import com.tanuj.nowplayinghistory.persistence.FavSong;
import com.tanuj.nowplayinghistory.persistence.Song;

public class FavoritesItemTouchCallback extends ItemTouchHelper.SimpleCallback {

    private final SwipeAction removeSwipeAction = new SwipeAction(SwipeAction.Dir.LEFT, "Remove");

    private final View anchor;
    private final SongsPagedListAdapter adapter;

    public FavoritesItemTouchCallback(View anchor, SongsPagedListAdapter adapter) {
        super(0, ItemTouchHelper.LEFT);
        this.anchor = anchor;
        this.adapter = adapter;
    }

    @Override
    public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
        return false;
    }

    @Override
    public void onSwiped(final RecyclerView.ViewHolder viewHolder, int swipeDir) {
        final int position = viewHolder.getAdapterPosition();
        Song song = adapter.getItem(position);
        final FavSong favSong = new FavSong(song);

        if (swipeDir == ItemTouchHelper.LEFT) {
            Utils.executeAsync(() -> App.getDb().songDao().delete(favSong));

            String message = "Removed " + favSong.getSongText() + " from favorites";
            Snackbar.make(anchor, message, Snackbar.LENGTH_SHORT)
                    .setAction("Undo", (view) -> Utils.executeAsync(() -> App.getDb().songDao().insert(favSong)))
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
}
