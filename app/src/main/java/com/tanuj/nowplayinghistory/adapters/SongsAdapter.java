package com.tanuj.nowplayinghistory.adapters;

import android.arch.paging.PagedListAdapter;
import android.databinding.DataBindingUtil;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.recyclerview.extensions.DiffCallback;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tanuj.nowplayinghistory.R;
import com.tanuj.nowplayinghistory.Utils;
import com.tanuj.nowplayinghistory.databinding.SongInfoBinding;
import com.tanuj.nowplayinghistory.persistence.Song;

public class SongsAdapter extends PagedListAdapter<Song, SongsAdapter.ViewHolder> {

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public SongInfoBinding binding;

        public ViewHolder(SongInfoBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void onSongClick(View view) {
            Utils.launchMusicApp(binding.getSong().getSongText());
        }
    }

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

    public SongsAdapter() {
        super(DIFF_CALLBACK);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        SongInfoBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.song_info, parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Song song = getItem(position);
        holder.binding.setSong(song);
        holder.binding.setListener(holder);
        holder.binding.executePendingBindings();
    }

    @Nullable
    @Override
    public Song getItem(int position) {
        return super.getItem(position);
    }
}
