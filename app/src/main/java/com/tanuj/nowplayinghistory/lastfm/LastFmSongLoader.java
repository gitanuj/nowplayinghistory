package com.tanuj.nowplayinghistory.lastfm;

import com.bumptech.glide.load.Options;
import com.bumptech.glide.load.model.ModelLoader;
import com.bumptech.glide.load.model.ModelLoaderFactory;
import com.bumptech.glide.load.model.MultiModelLoaderFactory;
import com.tanuj.nowplayinghistory.persistence.Song;

import java.io.InputStream;

import androidx.annotation.NonNull;
import okhttp3.Call;
import okhttp3.OkHttpClient;

public class LastFmSongLoader implements ModelLoader<Song, InputStream> {

    private final Call.Factory client;

    public LastFmSongLoader(@NonNull Call.Factory client) {
        this.client = client;
    }

    @Override
    public boolean handles(@NonNull Song song) {
        return true;
    }

    @Override
    public LoadData<InputStream> buildLoadData(@NonNull Song model, int width, int height,
                                               @NonNull Options options) {
        return new LoadData<>(model, new LastFmSongFetcher(client, model));
    }

    public static class Factory implements ModelLoaderFactory<Song, InputStream> {
        private static volatile Call.Factory internalClient;
        private final Call.Factory client;

        private static Call.Factory getInternalClient() {
            if (internalClient == null) {
                synchronized (Factory.class) {
                    if (internalClient == null) {
                        internalClient = new OkHttpClient();
                    }
                }
            }
            return internalClient;
        }

        public Factory() {
            this(getInternalClient());
        }

        public Factory(@NonNull Call.Factory client) {
            this.client = client;
        }

        @NonNull
        @Override
        public ModelLoader<Song, InputStream> build(MultiModelLoaderFactory multiFactory) {
            return new LastFmSongLoader(client);
        }

        @Override
        public void teardown() {
            // Do nothing, this instance doesn't own the client.
        }
    }
}
