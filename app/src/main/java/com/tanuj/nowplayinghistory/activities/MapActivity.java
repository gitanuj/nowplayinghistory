package com.tanuj.nowplayinghistory.activities;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.tanuj.nowplayinghistory.R;
import com.tanuj.nowplayinghistory.fragments.MapFragment;

public class MapActivity extends AppCompatActivity {

    public static final String EXTRA_SHOW_FAVORITES = "show_favorites";
    public static final String EXTRA_MIN_TIMESTAMP = "min_timestamp";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);

        setContentView(R.layout.activity_map);

        boolean showFavorites  = getIntent().getExtras().getBoolean(EXTRA_SHOW_FAVORITES);
        long minTimestamp  = getIntent().getExtras().getLong(EXTRA_MIN_TIMESTAMP);

        Fragment fragment = MapFragment.newInstance(showFavorites, minTimestamp);
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.container, fragment);
        fragmentTransaction.commit();
    }
}
