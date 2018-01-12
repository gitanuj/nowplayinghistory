package com.tanuj.nowplayinghistory.activities;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

import com.tanuj.nowplayinghistory.R;
import com.tanuj.nowplayinghistory.fragments.ListFragment;
import com.tanuj.nowplayinghistory.fragments.MapFragment;

public class MainActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {

    private static final long _24_HRS_MILLIS = 24 * 60 * 60 * 1000;

    enum Filter {
        All,
        Last24Hrs,
        Last7Days,
        Last30Days
    }

    private boolean showMap = false;
    private Filter filter = Filter.All;
    private boolean showFavorites;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_nav);
        bottomNavigationView.setOnNavigationItemSelectedListener(this);
        bottomNavigationView.setSelectedItemId(R.id.action_recents);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_recents:
                showFavorites = false;
                navigate();
                break;
            case R.id.action_favorites:
                showFavorites = true;
                navigate();
                break;
        }
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.top_bar_menu, menu);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        // Set the checked state for menu
        menu.findItem(R.id.action_show_map).setChecked(showMap);
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
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_show_map:
                item.setChecked(!item.isChecked());
                showMap = item.isChecked();
                item.setIcon(showMap ? R.drawable.ic_list : R.drawable.ic_map);
                navigate();
                break;
            case R.id.action_filter_all:
                filter = Filter.All;
                item.setChecked(true);
                navigate();
                break;
            case R.id.action_filter_last_24_hrs:
                filter = Filter.Last24Hrs;
                item.setChecked(true);
                navigate();
                break;
            case R.id.action_filter_last_7_days:
                filter = Filter.Last7Days;
                item.setChecked(true);
                navigate();
                break;
            case R.id.action_filter_last_30_days:
                filter = Filter.Last30Days;
                item.setChecked(true);
                navigate();
                break;
        }
        return true;
    }

    private void navigate() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        Fragment fragment;

        if (showMap) {
            fragment = MapFragment.newInstance(showFavorites, getMinTimestamp());
        } else {
            fragment = ListFragment.newInstance(showFavorites, getMinTimestamp());
        }

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
}
