package com.tanuj.nowplayinghistory;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.SearchManager;
import android.content.ComponentName;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.location.Location;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.text.format.DateUtils;
import android.util.TypedValue;

import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;

import java.util.Collection;

import static android.content.res.Configuration.ORIENTATION_PORTRAIT;

public class Utils {

    private static final String ENABLED_NOTIFICATION_LISTENERS = "enabled_notification_listeners";
    private static final String SONG_STRING_QUALIFIER = "%1$s";
    private static final String ARTIST_STRING_QUALIFIER = "%2$s";

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

    public static String extractSongTitleFromText(String text) {
        try {
            String songFormatText = App.getContext().getString(R.string.song_format_string);
            String beforeText = songFormatText.substring(0, songFormatText.indexOf(SONG_STRING_QUALIFIER));
            String afterText = songFormatText.substring(songFormatText.indexOf(SONG_STRING_QUALIFIER) + SONG_STRING_QUALIFIER.length());
            if (beforeText.contains(ARTIST_STRING_QUALIFIER)) {
                beforeText = beforeText.substring(beforeText.indexOf(ARTIST_STRING_QUALIFIER) + ARTIST_STRING_QUALIFIER.length());
            }
            if (afterText.contains(ARTIST_STRING_QUALIFIER)) {
                afterText = afterText.substring(0, afterText.indexOf(ARTIST_STRING_QUALIFIER));
            }

            int fromIndex = text.indexOf(beforeText) + beforeText.length();
            if (afterText.length() == 0) {
                return text.substring(fromIndex);
            }
            int toIndex = text.indexOf(afterText);
            return text.substring(fromIndex, toIndex);
        } catch (Exception e) {
            return null;
        }
    }

    public static String extractArtistTitleFromText(String text) {
        try {
            String songFormatText = App.getContext().getString(R.string.song_format_string);
            String beforeText = songFormatText.substring(0, songFormatText.indexOf(ARTIST_STRING_QUALIFIER));
            String afterText = songFormatText.substring(songFormatText.indexOf(ARTIST_STRING_QUALIFIER) + ARTIST_STRING_QUALIFIER.length());
            if (beforeText.contains(SONG_STRING_QUALIFIER)) {
                beforeText = beforeText.substring(beforeText.indexOf(SONG_STRING_QUALIFIER) + SONG_STRING_QUALIFIER.length());
            }
            if (afterText.contains(SONG_STRING_QUALIFIER)) {
                afterText = afterText.substring(0, afterText.indexOf(SONG_STRING_QUALIFIER));
            }

            int fromIndex = text.indexOf(beforeText) + beforeText.length();
            if (afterText.length() == 0) {
                return text.substring(fromIndex);
            }
            int toIndex = text.indexOf(afterText);
            return text.substring(fromIndex, toIndex);
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

    public static boolean isNotificationAccessGranted() {
        String pkgName = App.getContext().getPackageName();
        final String flat = Settings.Secure.getString(App.getContext().getContentResolver(), ENABLED_NOTIFICATION_LISTENERS);
        if (!TextUtils.isEmpty(flat)) {
            final String[] names = flat.split(":");
            for (String name : names) {
                final ComponentName cn = ComponentName.unflattenFromString(name);
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
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(App.getContext());
        return sharedPreferences.getBoolean("isIntroRequired", true);
    }

    public static void setIsIntroRequired(boolean isIntroRequired) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(App.getContext());
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

    public static boolean isLocationAccessGranted() {
        return ContextCompat.checkSelfPermission(App.getContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED;
    }

    public static void launchNowPlayingSettings() {
        Intent intent = new Intent();
        intent.setComponent(new ComponentName("com.google.intelligence.sense", "com.google.intelligence.sense.ambientmusic.AmbientMusicSettingsActivity"));
        App.getContext().startActivity(intent);
    }

    public static boolean isEmpty(Collection collection) {
        return collection == null || collection.isEmpty();
    }

    @SuppressLint("MissingPermission")
    public static Location getCurrentLocation() {
        if (Utils.isLocationAccessGranted()) {
            Task<Location> task = LocationServices.getFusedLocationProviderClient(App.getContext()).getLastLocation();
            try {
                return Tasks.await(task);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public static void composeFeedbackEmail() {
        int versionCode = BuildConfig.VERSION_CODE;
        String versionName = BuildConfig.VERSION_NAME;
        String[] addresses = new String[]{"playdevfeedback" + "@gmail.com"};
        String subject = "Feedback";
        String body = "\n\n\n\n" +
                "Version " + versionName + " (" + versionCode + ")\n" +
                "Android " + Build.VERSION.RELEASE + " (" + Build.VERSION.SDK_INT + ")\n" +
                Build.MANUFACTURER + " " + Build.MODEL;

        Intent intent = new Intent(Intent.ACTION_SENDTO);
        intent.setData(Uri.parse("mailto:"));
        intent.putExtra(Intent.EXTRA_EMAIL, addresses);
        intent.putExtra(Intent.EXTRA_SUBJECT, subject);
        intent.putExtra(Intent.EXTRA_TEXT, body);
        if (intent.resolveActivity(App.getContext().getPackageManager()) != null) {
            App.getContext().startActivity(intent);
        }
    }

    public static void openPlayStore() {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse("market://details?id=" + App.getContext().getPackageName()));
        if (intent.resolveActivity(App.getContext().getPackageManager()) != null) {
            App.getContext().startActivity(intent);
        }
    }

    public static void styleMap(Resources resources, GoogleMap map) {
        int statusBarHeight = Utils.getAndroidDimenResource(resources,"status_bar_height");
        int navigationBarHeight = Utils.getAndroidDimenResource(resources, "navigation_bar_height");

        if (resources.getConfiguration().orientation == ORIENTATION_PORTRAIT) {
            map.setPadding(0, statusBarHeight, 0, navigationBarHeight);
        } else {
            map.setPadding(0, statusBarHeight, navigationBarHeight, 0);
        }

        int currentNightMode = resources.getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK;
        if (Configuration.UI_MODE_NIGHT_YES == currentNightMode) {
            try {
                map.setMapStyle(MapStyleOptions.loadRawResourceStyle(App.getContext(), R.raw.map_night_theme));
            } catch (Resources.NotFoundException e) {
            }
        }
    }

    public static int getAndroidDimenResource(Resources resources, String id) {
        int resourceId = resources.getIdentifier(id, "dimen", "android");
        if (resourceId > 0) {
            return resources.getDimensionPixelSize(resourceId);
        }
        return 0;
    }
}
