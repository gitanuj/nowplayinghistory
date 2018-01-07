package com.tanuj.nowplayinghistory.adapters;

import android.app.SearchManager;
import android.content.Intent;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.tanuj.nowplayinghistory.R;
import com.tanuj.nowplayinghistory.Utils;
import com.tanuj.nowplayinghistory.persistence.FavSong;
import com.tanuj.nowplayinghistory.persistence.Song;

import java.util.List;

public class SongsAdapter extends RecyclerView.Adapter<SongsAdapter.ViewHolder> {

    private List<Song> songs;

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private View view;
        private Song song;
        private TextView titleTv;
        private TextView timeTv;

        public ViewHolder(View v) {
            super(v);
            view = v;
            titleTv = v.findViewById(R.id.title);
            timeTv = v.findViewById(R.id.time);
        }

        public void bind(Song song) {
            this.song = song;
            view.setOnClickListener(this);
            titleTv.setText(song.getSongText());
            timeTv.setText(DateUtils.getRelativeTimeSpanString(
                    song.getTimestamp(),
                    System.currentTimeMillis(),
                    DateUtils.SECOND_IN_MILLIS,
                    DateUtils.FORMAT_ABBREV_ALL));
        }

        @Override
        public void onClick(View view) {
            Intent intent = new Intent(MediaStore.INTENT_ACTION_MEDIA_PLAY_FROM_SEARCH);
            intent.putExtra(MediaStore.EXTRA_MEDIA_FOCUS, "vnd.android.cursor.item/audio");

            String songTitle = Utils.extractSongTitleFromText(song.getSongText());
            if (!TextUtils.isEmpty(songTitle)) {
                intent.putExtra(MediaStore.EXTRA_MEDIA_TITLE, songTitle);
            }

            String artistTitle = Utils.extractArtistTitleFromText(song.getSongText());
            if (!TextUtils.isEmpty(artistTitle)) {
                intent.putExtra(MediaStore.EXTRA_MEDIA_ARTIST, artistTitle);
            }

            String queryText = song.getSongText();
            if (!TextUtils.isEmpty(songTitle) && !TextUtils.isEmpty(artistTitle)) {
                queryText = songTitle + " " + artistTitle;
            }
            intent.putExtra(SearchManager.QUERY, queryText);

            if (intent.resolveActivity(view.getContext().getPackageManager()) != null) {
                view.getContext().startActivity(intent);
            }
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
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.song_info, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.bind(songs.get(position));
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
