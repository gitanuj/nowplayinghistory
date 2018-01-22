package com.tanuj.nowplayinghistory.adapters;

import android.arch.paging.PagedListAdapter;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.recyclerview.extensions.DiffCallback;
import android.view.ViewGroup;

import com.tanuj.nowplayinghistory.persistence.Song;

public class SongsPagedListAdapter extends PagedListAdapter<Song, SongsListAdapter.SongViewHolder> {

    private SongsListAdapter songsListAdapter = new SongsListAdapter();

    public static final DiffCallback<Song> DIFF_CALLBACK = new DiffCallback<Song>() {

        @Override
        public boolean areItemsTheSame(@NonNull Song oldItem, @NonNull Song newItem) {
            return oldItem.getTimestamp() == newItem.getTimestamp();
        }

        @Override
        public boolean areContentsTheSame(@NonNull Song oldItem, @NonNull Song newItem) {
            return oldItem.equals(newItem);
        }
    };

    public SongsPagedListAdapter() {
        super(DIFF_CALLBACK);
    }

    @Override
    public SongsListAdapter.SongViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return songsListAdapter.onCreateViewHolder(parent, viewType);
    }

    @Override
    public void onBindViewHolder(SongsListAdapter.SongViewHolder holder, int position) {
        Song song = getItem(position);
        songsListAdapter.onBindViewHolderImpl(holder, song);
    }

    @Nullable
    @Override
    public Song getItem(int position) {
        return super.getItem(position);
    }
}
