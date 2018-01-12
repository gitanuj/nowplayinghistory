package com.tanuj.nowplayinghistory;

import android.annotation.SuppressLint;
import android.app.SearchManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.AsyncTask;
import android.provider.MediaStore;
import android.provider.Settings;
import android.text.TextUtils;
import android.text.format.DateUtils;
import android.util.TypedValue;

public class Utils {

    private static final String ENABLED_NOTIFICATION_LISTENERS = "enabled_notification_listeners";
    private static final String DELIMITER = " by ";

    @SuppressLint("StaticFieldLeak")
    public static void executeAsync(final Runnable doInBackgroundRunnable) {
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                doInBackgroundRunnable.run();
                return null;
            }
        }.execute();
    }

    @SuppressLint("StaticFieldLeak")
    public static void executeAsync(final Runnable doInBackgroundRunnable, final Runnable onPostExecuteRunnable) {
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                doInBackgroundRunnable.run();
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                onPostExecuteRunnable.run();
            }
        }.execute();
    }

    public static String extractSongTitleFromText(String songText) {
        try {
            return songText.substring(0, songText.lastIndexOf(DELIMITER));
        } catch (Exception e) {
            return null;
        }
    }

    public static String extractArtistTitleFromText(String songText) {
        try {
            return songText.substring(songText.lastIndexOf(DELIMITER) + DELIMITER.length());
        } catch (Exception e) {
            return null;
        }
    }

    public static float dpToPx(float dp) {
        Resources r = App.getContext().getResources();
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, r.getDisplayMetrics());
    }

    public static void LaunchNotificationAccessActivity() {
        Intent intent = new Intent("android.settings.ACTION_NOTIFICATION_LISTENER_SETTINGS");
        App.getContext().startActivity(intent);
    }

    public static boolean isNotificationServiceEnabled() {
        String pkgName = App.getContext().getPackageName();
        final String flat = Settings.Secure.getString(App.getContext().getContentResolver(),
                ENABLED_NOTIFICATION_LISTENERS);
        if (!TextUtils.isEmpty(flat)) {
            final String[] names = flat.split(":");
            for (int i = 0; i < names.length; i++) {
                final ComponentName cn = ComponentName.unflattenFromString(names[i]);
                if (cn != null) {
                    if (TextUtils.equals(pkgName, cn.getPackageName())) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public static boolean isIntroRequired() {
        SharedPreferences sharedPreferences = App.getContext().getSharedPreferences("prefs", Context.MODE_PRIVATE);
        return sharedPreferences.getBoolean("isIntroRequired", true);
    }

    public static void setIsIntroRequired(boolean isIntroRequired) {
        SharedPreferences sharedPreferences = App.getContext().getSharedPreferences("prefs", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean("isIntroRequired", isIntroRequired);
        editor.apply();
    }

    public static void launchMusicApp(String songText) {
        Intent intent = new Intent(MediaStore.INTENT_ACTION_MEDIA_PLAY_FROM_SEARCH);
        intent.putExtra(MediaStore.EXTRA_MEDIA_FOCUS, "vnd.android.cursor.item/audio");

        String songTitle = Utils.extractSongTitleFromText(songText);
        if (!TextUtils.isEmpty(songTitle)) {
            intent.putExtra(MediaStore.EXTRA_MEDIA_TITLE, songTitle);
        }

        String artistTitle = Utils.extractArtistTitleFromText(songText);
        if (!TextUtils.isEmpty(artistTitle)) {
            intent.putExtra(MediaStore.EXTRA_MEDIA_ARTIST, artistTitle);
        }

        String queryText = songText;
        if (!TextUtils.isEmpty(songTitle) && !TextUtils.isEmpty(artistTitle)) {
            queryText = songTitle + " " + artistTitle;
        }
        intent.putExtra(SearchManager.QUERY, queryText);

        if (intent.resolveActivity(App.getContext().getPackageManager()) != null) {
            App.getContext().startActivity(intent);
        }
    }

    public static String getTimestampString(long timestamp) {
        return DateUtils.getRelativeTimeSpanString(
                timestamp,
                System.currentTimeMillis(),
                DateUtils.SECOND_IN_MILLIS,
                DateUtils.FORMAT_ABBREV_ALL).toString();
    }
}
