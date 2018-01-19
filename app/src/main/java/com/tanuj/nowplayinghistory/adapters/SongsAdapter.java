package com.tanuj.nowplayinghistory.adapters;

import android.databinding.DataBindingUtil;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tanuj.nowplayinghistory.R;
import com.tanuj.nowplayinghistory.Utils;
import com.tanuj.nowplayinghistory.databinding.SongInfoBinding;
import com.tanuj.nowplayinghistory.persistence.FavSong;
import com.tanuj.nowplayinghistory.persistence.Song;

import java.util.List;

public class SongsAdapter extends RecyclerView.Adapter<SongsAdapter.ViewHolder> {

    private List<Song> songs;

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

    public SongsAdapter(List<Song> songs) {
        this.songs = songs;
    }

    public void setSongsData(@NonNull List<Song> songs) {
        this.songs.clear();
        this.songs.addAll(songs);
        notifyDataSetChanged();
    }

    public void setFavSongsData(List<FavSong> favSongs) {
        this.songs.clear();
        this.songs.addAll(favSongs);
        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        SongInfoBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.song_info, parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.binding.setSong(songs.get(position));
        holder.binding.setListener(holder);
        holder.binding.executePendingBindings();
    }

    @Override
    public long getItemId(int position) {
        return getItem(position).getTimestamp();
    }

    @Override
    public int getItemCount() {
        return songs.size();
    }

    public Song getItem(int position) {
        return songs.get(position);
    }
}
