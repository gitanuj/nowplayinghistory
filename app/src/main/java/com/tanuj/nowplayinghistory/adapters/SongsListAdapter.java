package com.tanuj.nowplayinghistory.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.tanuj.nowplayinghistory.R;
import com.tanuj.nowplayinghistory.Utils;
import com.tanuj.nowplayinghistory.databinding.SongInfoBinding;
import com.tanuj.nowplayinghistory.lastfm.GlideApp;
import com.tanuj.nowplayinghistory.persistence.Song;

import java.util.ArrayList;
import java.util.List;

import androidx.databinding.BindingAdapter;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

public class SongsListAdapter extends RecyclerView.Adapter<SongsListAdapter.SongViewHolder> {

    private List<Song> songsList = new ArrayList<>();

    public static class SongViewHolder extends RecyclerView.ViewHolder {
        public SongInfoBinding binding;

        public SongViewHolder(SongInfoBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public Song getSong() {
            return binding.getSong();
        }

        @BindingAdapter({"bind:song"})
        public static void loadImage(ImageView view, Song song) {
            GlideApp.with(view).load(song).placeholder(R.drawable.ic_play_circle_outline).into(view);
        }

        public void onSongClick(View view) {
            Utils.launchMusicApp(binding.getSong().getSongText());
        }
    }

    public void setList(List<Song> songs) {
        songsList.clear();
        songsList.addAll(songs);
        notifyDataSetChanged();
    }

    @Override
    public SongViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        SongInfoBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.song_info, parent, false);
        return new SongViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(SongViewHolder holder, int position) {
        Song song = songsList.get(position);
        onBindViewHolderImpl(holder, song);
    }

    @Override
    public int getItemCount() {
        return songsList.size();
    }

    void onBindViewHolderImpl(SongViewHolder holder, Song song) {
        holder.binding.setSong(song);
        holder.binding.setSongViewHolder(holder);
        holder.binding.executePendingBindings();
    }
}
