package com.tanuj.nowplayinghistory.lastfm;

import android.util.Log;

import com.bumptech.glide.Priority;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.HttpException;
import com.bumptech.glide.load.data.DataFetcher;
import com.bumptech.glide.util.ContentLengthInputStream;
import com.bumptech.glide.util.Preconditions;
import com.tanuj.nowplayinghistory.App;
import com.tanuj.nowplayinghistory.Utils;
import com.tanuj.nowplayinghistory.lastfm.pojos.Image;
import com.tanuj.nowplayinghistory.lastfm.pojos.TrackInfo;
import com.tanuj.nowplayinghistory.persistence.Song;

import java.io.IOException;
import java.io.InputStream;

import androidx.annotation.NonNull;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

public class LastFmSongFetcher implements DataFetcher<InputStream>, Callback {
    private static final String TAG = "LastFmSongFetcher";
    private final Call.Factory client;
    private final Song song;
    private InputStream stream;
    private ResponseBody responseBody;
    private DataCallback<? super InputStream> callback;
    // call may be accessed on the main thread while the object is in use on other threads. All other
    // accesses to variables may occur on different threads, but only one at a time.
    private volatile Call call;

    public LastFmSongFetcher(Call.Factory client, Song song) {
        this.client = client;
        this.song = song;
    }

    @Override
    public void loadData(@NonNull Priority priority,
                         @NonNull final DataCallback<? super InputStream> callback) {
        String url = getImageUrl(song);
        Request.Builder requestBuilder = new Request.Builder().url(url);
        Request request = requestBuilder.build();
        this.callback = callback;

        call = client.newCall(request);
        call.enqueue(this);
    }

    @Override
    public void onFailure(@NonNull Call call, @NonNull IOException e) {
        if (Log.isLoggable(TAG, Log.DEBUG)) {
            Log.d(TAG, "OkHttp failed to obtain result", e);
        }

        callback.onLoadFailed(e);
    }

    @Override
    public void onResponse(@NonNull Call call, @NonNull Response response) {
        responseBody = response.body();
        if (response.isSuccessful()) {
            long contentLength = Preconditions.checkNotNull(responseBody).contentLength();
            stream = ContentLengthInputStream.obtain(responseBody.byteStream(), contentLength);
            callback.onDataReady(stream);
        } else {
            callback.onLoadFailed(new HttpException(response.message(), response.code()));
        }
    }

    @Override
    public void cleanup() {
        try {
            if (stream != null) {
                stream.close();
            }
        } catch (IOException e) {
            // Ignored
        }
        if (responseBody != null) {
            responseBody.close();
        }
        callback = null;
    }

    @Override
    public void cancel() {
        Call local = call;
        if (local != null) {
            local.cancel();
        }
    }

    @NonNull
    @Override
    public Class<InputStream> getDataClass() {
        return InputStream.class;
    }

    @NonNull
    @Override
    public DataSource getDataSource() {
        return DataSource.REMOTE;
    }

    private String getImageUrl(Song song) {
        String artist = Utils.extractArtistTitleFromText(song.getSongText());
        String track = Utils.extractSongTitleFromText(song.getSongText());
        try {
            retrofit2.Response<TrackInfo> response = App.getLastFmService().trackInfo(artist, track).execute();
            TrackInfo trackInfo = response.body();
            for (Image image : trackInfo.getTrack().getAlbum().getImage()) {
                if (image.getSize().equals("large")) {
                    return image.getText();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }
}
