package com.tanuj.nowplayinghistory.activities;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import com.tanuj.nowplayinghistory.R;
import com.tanuj.nowplayinghistory.fragments.FavoritesFragment;
import com.tanuj.nowplayinghistory.fragments.RecentsFragment;

public class MainActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {

    private BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bottomNavigationView = findViewById(R.id.bottom_nav);
        bottomNavigationView.setOnNavigationItemSelectedListener(this);
        bottomNavigationView.setSelectedItemId(R.id.action_recents);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        Fragment fragment = null;
        switch (item.getItemId()) {
            case R.id.action_recents:
                fragment = new RecentsFragment();
                break;
            case R.id.action_favorites:
                fragment = new FavoritesFragment();
                break;
        }

        if (fragment != null) {
            fragmentTransaction.replace(R.id.container, fragment);
            fragmentTransaction.commit();
        }
        return true;
    }
}
