package com.tanuj.nowplayinghistory.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.tanuj.nowplayinghistory.App;
import com.tanuj.nowplayinghistory.R;
import com.tanuj.nowplayinghistory.Utils;
import com.tanuj.nowplayinghistory.fragments.ListFragment;
import com.tanuj.nowplayinghistory.fragments.MapFragment;

import java.util.Map;

public class MainActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {

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
    private Menu menu;

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

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_nav);
        bottomNavigationView.setSelectedItemId(showFavorites ? R.id.action_favorites : R.id.action_recents);
        bottomNavigationView.setOnNavigationItemSelectedListener(this);

        FloatingActionButton mapFab = findViewById(R.id.map_fab);
        mapFab.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, MapActivity.class);
            intent.putExtra(MapActivity.EXTRA_SHOW_FAVORITES, showFavorites);
            intent.putExtra(MapActivity.EXTRA_MIN_TIMESTAMP, getMinTimestamp());
            startActivity(intent);
        });
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_recents:
                showFavorites = false;
                break;
            case R.id.action_favorites:
                showFavorites = true;
                break;
        }

        navigate();
        updateActionBarMenu();
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.top_bar_menu, menu);
        this.menu = menu;
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        updateActionBarMenu();
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_clear_songs:
                showClearSongsDialog();
                return true;
            case R.id.action_filter:
                // Ignore this
                return false;
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
        updateActionBarMenu();
        return true;
    }

    private void navigate() {
        Fragment fragment = ListFragment.newInstance(showFavorites, getMinTimestamp());
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.container, fragment);
        fragmentTransaction.commit();
    }

    private void updateActionBarMenu() {
        if (menu == null) {
            return;
        }

        switch (filter) {
            case All:
                menu.findItem(R.id.action_filter_all).setChecked(true);
                break;
            case Last24Hrs:
                menu.findItem(R.id.action_filter_last_24_hrs).setChecked(true);
                break;
            case Last7Days:
                menu.findItem(R.id.action_filter_last_7_days).setChecked(true);
                break;
            case Last30Days:
                menu.findItem(R.id.action_filter_last_30_days).setChecked(true);
                break;
        }
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

                    showConfirmClearSongsDialog("Do you want to clear " + group + " older than "+ options[which] +"?", favorites, maxTimestamp);
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
