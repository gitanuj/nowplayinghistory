package com.tanuj.nowplayinghistory.activities;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.PopupMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;

import com.tanuj.nowplayinghistory.App;
import com.tanuj.nowplayinghistory.R;
import com.tanuj.nowplayinghistory.Utils;
import com.tanuj.nowplayinghistory.fragments.ListFragment;

public class MainActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener, PopupMenu.OnMenuItemClickListener {

    private static final long _24_HRS_MILLIS = 24 * 60 * 60 * 1000;
    private static final String EXTRA_SHOW_FAVORITES = "show_favorites";
    private static final String EXTRA_FILTER = "fliter";

    enum Filter {
        All,
        Last24Hrs,
        Last7Days,
        Last30Days
    }

    private Filter filter = Filter.All;
    private boolean showFavorites = false;

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean(EXTRA_SHOW_FAVORITES, showFavorites);
        outState.putSerializable(EXTRA_FILTER, filter);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null) {
            showFavorites = savedInstanceState.getBoolean(EXTRA_SHOW_FAVORITES);
            filter = (Filter) savedInstanceState.getSerializable(EXTRA_FILTER);
        }

        setContentView(R.layout.activity_main);

        navigate();

        ImageButton deleteButton = findViewById(R.id.clear_songs);
        deleteButton.setOnClickListener(view -> showClearSongsDialog());

        ImageButton filterButton = findViewById(R.id.filter_songs);
        filterButton.setOnClickListener(this::showFilterSongsPopup);

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_nav);
        bottomNavigationView.setSelectedItemId(showFavorites ? R.id.action_favorites : R.id.action_recents);
        bottomNavigationView.setOnNavigationItemSelectedListener(this);

        FloatingActionButton mapFab = findViewById(R.id.map_fab);
        mapFab.setOnClickListener(v -> {
            int[] location = new int[2];
            mapFab.getLocationInWindow(location);
            int revealX = location[0] + mapFab.getWidth() / 2;
            int revealY = location[1] + mapFab.getHeight() / 2;

            Intent intent = new Intent(MainActivity.this, MapActivity.class);
            intent.putExtra(MapActivity.EXTRA_CIRCULAR_REVEAL_X, revealX);
            intent.putExtra(MapActivity.EXTRA_CIRCULAR_REVEAL_Y, revealY);
            intent.putExtra(MapActivity.EXTRA_SHOW_FAVORITES, showFavorites);
            intent.putExtra(MapActivity.EXTRA_MIN_TIMESTAMP, getMinTimestamp());

            ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(MainActivity.this, mapFab, "transition");
            startActivity(intent, options.toBundle());
        });
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        showFavorites = item.getItemId() == R.id.action_favorites;
        navigate();
        return true;
    }

    public void showFilterSongsPopup(View view) {
        PopupMenu popup = new PopupMenu(this, view);
        popup.setOnMenuItemClickListener(this);
        MenuInflater inflater = popup.getMenuInflater();
        inflater.inflate(R.menu.filter_songs, popup.getMenu());

        switch (filter) {
            case All:
                popup.getMenu().findItem(R.id.action_filter_all).setChecked(true);
                break;
            case Last24Hrs:
                popup.getMenu().findItem(R.id.action_filter_last_24_hrs).setChecked(true);
                break;
            case Last7Days:
                popup.getMenu().findItem(R.id.action_filter_last_7_days).setChecked(true);
                break;
            case Last30Days:
                popup.getMenu().findItem(R.id.action_filter_last_30_days).setChecked(true);
                break;
        }

        popup.show();
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_filter_all:
                filter = Filter.All;
                break;
            case R.id.action_filter_last_24_hrs:
                filter = Filter.Last24Hrs;
                break;
            case R.id.action_filter_last_7_days:
                filter = Filter.Last7Days;
                break;
            case R.id.action_filter_last_30_days:
                filter = Filter.Last30Days;
                break;
        }

        navigate();
        return true;
    }

    private void navigate() {
        Fragment fragment = ListFragment.newInstance(showFavorites, getMinTimestamp());
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.container, fragment);
        fragmentTransaction.commit();
    }

    private long getMinTimestamp() {
        switch (filter) {
            case All:
                return 0;
            case Last24Hrs:
                return System.currentTimeMillis() - _24_HRS_MILLIS;
            case Last7Days:
                return System.currentTimeMillis() - 7 * _24_HRS_MILLIS;
            case Last30Days:
                return System.currentTimeMillis() - 30 * _24_HRS_MILLIS;
        }
        return 0;
    }

    private void showClearSongsDialog() {
        String[] options = new String[]{"90 days", "30 days", "7 days"};
        boolean favorites = showFavorites;
        String group = favorites ? "favorites" : "recents";
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Clear " + group + " older than")
                .setNeutralButton("Clear All", (dialog, which) -> {
                    dialog.dismiss();

                    showConfirmClearSongsDialog("Do you want to clear all " + group + "?", favorites, System.currentTimeMillis());
                })
                .setNegativeButton("Cancel", (dialog, which) -> {
                    dialog.dismiss();
                })
                .setItems(options, (dialog, which) -> {
                    dialog.dismiss();

                    long maxTimestamp = System.currentTimeMillis();
                    switch (which) {
                        case 0:
                            maxTimestamp -= 90 * _24_HRS_MILLIS;
                            break;
                        case 1:
                            maxTimestamp -= 30 * _24_HRS_MILLIS;
                            break;
                        case 2:
                            maxTimestamp -= 7 * _24_HRS_MILLIS;
                            break;
                        default:
                            return;
                    }

                    showConfirmClearSongsDialog("Do you want to clear " + group + " older than " + options[which] + "?", favorites, maxTimestamp);
                });
        builder.create().show();
    }

    private void showConfirmClearSongsDialog(String message, boolean favorites, long maxTimestamp) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Clear songs")
                .setMessage(message)
                .setPositiveButton("Clear", (dialog, which) -> {
                    dialog.dismiss();

                    if (favorites) {
                        Utils.executeAsync(() -> App.getDb().songDao().deleteAllFavSongs(maxTimestamp));
                    } else {
                        Utils.executeAsync(() -> App.getDb().songDao().deleteAllSongs(maxTimestamp));
                    }
                })
                .setNegativeButton("Cancel", (dialog, which) -> {
                    dialog.dismiss();
                });
        builder.create().show();
    }
}
