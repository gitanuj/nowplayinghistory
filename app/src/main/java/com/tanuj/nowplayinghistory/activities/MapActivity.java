package com.tanuj.nowplayinghistory.activities;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.ViewTreeObserver;
import android.view.animation.AccelerateInterpolator;

import com.tanuj.nowplayinghistory.R;
import com.tanuj.nowplayinghistory.fragments.MapFragment;

public class MapActivity extends AppCompatActivity {

    public static final String EXTRA_CIRCULAR_REVEAL_X = "EXTRA_CIRCULAR_REVEAL_X";
    public static final String EXTRA_CIRCULAR_REVEAL_Y = "EXTRA_CIRCULAR_REVEAL_Y";
    public static final String EXTRA_SHOW_FAVORITES = "show_favorites";
    public static final String EXTRA_MIN_TIMESTAMP = "min_timestamp";

    private View root;

    private int revealX;
    private int revealY;

    private boolean useReveal;
    private boolean unRevealInProgress;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_map);

        root = findViewById(R.id.container);

        revealX = getIntent().getExtras().getInt(EXTRA_CIRCULAR_REVEAL_X);
        revealY = getIntent().getExtras().getInt(EXTRA_CIRCULAR_REVEAL_Y);
        boolean showFavorites  = getIntent().getExtras().getBoolean(EXTRA_SHOW_FAVORITES);
        long minTimestamp  = getIntent().getExtras().getLong(EXTRA_MIN_TIMESTAMP);

        Fragment fragment = MapFragment.newInstance(showFavorites, minTimestamp);
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.container, fragment);
        fragmentTransaction.commit();

        if (savedInstanceState == null) {
            ViewTreeObserver viewTreeObserver = root.getViewTreeObserver();
            if (viewTreeObserver.isAlive()) {
                viewTreeObserver.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                    @Override
                    public void onGlobalLayout() {
                        useReveal = true;
                        revealActivity();
                        root.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                    }
                });
            }
        }
    }

    protected void revealActivity() {
        float finalRadius = (float) (Math.max(root.getWidth(), root.getHeight()));

        // create the animator for this view (the start radius is zero)
        Animator circularReveal = ViewAnimationUtils.createCircularReveal(root, revealX, revealY, 0, finalRadius);
        circularReveal.setDuration(400);
        circularReveal.setInterpolator(new AccelerateInterpolator());
        circularReveal.start();
    }

    protected void unrevealActivity() {
        if (!unRevealInProgress) {
            unRevealInProgress = true;

            float finalRadius = (float) (Math.max(root.getWidth(), root.getHeight()));
            Animator circularReveal = ViewAnimationUtils.createCircularReveal(root, revealX, revealY, finalRadius, 0);

            circularReveal.setDuration(400);
            circularReveal.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    unRevealInProgress = false;
                    finish();
                    overridePendingTransition(0, 0);
                }
            });

            circularReveal.start();
        }
    }

    @Override
    public void onBackPressed() {
        if (useReveal) {
            unrevealActivity();
        } else {
            super.onBackPressed();
        }
    }
}
