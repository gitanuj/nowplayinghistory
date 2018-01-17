package com.tanuj.nowplayinghistory;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.location.Location;
import android.service.notification.NotificationListenerService;
import android.service.notification.StatusBarNotification;
import android.text.TextUtils;

import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.tanuj.nowplayinghistory.persistence.Song;

public class NotificationListener extends NotificationListenerService {

    private static final String GOOGLE_MUSIC_SERVICE_PKG_NAME = "com.google.intelligence.sense";
    private static final String GOOGLE_MUSIC_SERVICE_CHANNEL_NAME = "com.google.intelligence.sense.ambientmusic.MusicNotificationChannel";

    @Override
    public void onListenerConnected() {
        process(getActiveNotifications());
    }

    @Override
    public void onNotificationPosted(StatusBarNotification sbn) {
        process(sbn);
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
            Song latestSong = App.getDb().recentsDao().loadLatest();

            if (!song.equals(latestSong)) {
                Location location = getCurrentLocation();
                if (location != null) {
                    song.setLat(location.getLatitude());
                    song.setLon(location.getLongitude());
                }
                App.getDb().recentsDao().insert(song);
            }
        });
    }

    private Location getCurrentLocation() {
        if (Utils.isLocationAccessGranted()) {
            Task<Location> task = LocationServices.getFusedLocationProviderClient(this).getLastLocation();
            try {
                return Tasks.await(task);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }
}
