package com.tanuj.nowplayinghistory;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.location.Location;
import android.service.notification.NotificationListenerService;
import android.service.notification.StatusBarNotification;
import android.text.TextUtils;

import com.tanuj.nowplayinghistory.persistence.Song;

public class NotificationListener extends NotificationListenerService {

    private static final String GOOGLE_MUSIC_SERVICE_PKG_NAME = "com.google.intelligence.sense";
    private static final String GOOGLE_MUSIC_SERVICE_CHANNEL_NAME = "com.google.intelligence.sense.ambientmusic.MusicNotificationChannel";

    @Override
    public void onListenerConnected() {
        StatusBarNotification[] notifications = getActiveNotifications();
        if (notifications != null) {
            process(notifications);
        }
    }

    @Override
    public void onNotificationPosted(StatusBarNotification sbn) {
        if (sbn != null) {
            process(sbn);
        }
    }

    private void process(StatusBarNotification... statusBarNotifications) {
        for (StatusBarNotification statusBarNotification : statusBarNotifications) {
            if (statusBarNotification.getPackageName().equals(GOOGLE_MUSIC_SERVICE_PKG_NAME)) {
                Notification notification = statusBarNotification.getNotification();
                if (notification.getChannelId().equals(GOOGLE_MUSIC_SERVICE_CHANNEL_NAME)) {
                    long timeMillis = notification.when;
                    String songText = notification.extras.getString(Notification.EXTRA_TITLE);
                    if (!TextUtils.isEmpty(songText)) {
                        persistSongAsync(timeMillis, songText);
                    }
                }
            }
        }
    }

    @SuppressLint("StaticFieldLeak")
    private void persistSongAsync(final long timeMillis, final String songText) {
        Utils.executeAsync(() -> {
            Song song = new Song(timeMillis, songText);
            Song latestSong = App.getDb().songDao().loadLatestSong();

            if (!song.equals(latestSong)) {
                Location location = Utils.getCurrentLocation();
                if (location != null) {
                    song.setLat(location.getLatitude());
                    song.setLon(location.getLongitude());
                }
                App.getDb().songDao().insert(song);
            }
        });
    }
}
