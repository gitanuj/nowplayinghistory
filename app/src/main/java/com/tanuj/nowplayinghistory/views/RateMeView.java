package com.tanuj.nowplayinghistory.views;

import android.app.AlertDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import androidx.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;

import com.tanuj.nowplayinghistory.R;
import com.tanuj.nowplayinghistory.Utils;

public class RateMeView extends FrameLayout implements View.OnClickListener {

    private static final String PREF_NEVER_SHOW = "rate_me_never_show";
    private static final String PREF_LAUNCH_COUNT = "rate_me_launch_count";
    private static final String PREF_DEFER_COUNT = "rate_me_defer_count";
    private static final int MIN_LAUNCH_COUNT_THRESHOLD = 4;

    private SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getContext());
    private boolean dismissed = false;

    public RateMeView(Context context) {
        this(context, null);
    }

    public RateMeView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RateMeView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        this(context, attrs, defStyleAttr, 0);
    }

    public RateMeView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();

        incrementLaunchCount();
        if (shouldShow()) {
            View view = LayoutInflater.from(getContext()).inflate(R.layout.rate_me, this);
            view.findViewById(R.id.no).setOnClickListener(this);
            view.findViewById(R.id.yes).setOnClickListener(this);
        } else {
            dismissed = true;
        }
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();

        if (!dismissed) {
            incrementDeferredCount();
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return true;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.no:
                onNoClicked();
                break;
            case R.id.yes:
                onYesClicked();
                break;
        }
    }

    private void onNoClicked() {
        new AlertDialog.Builder(getContext()).setTitle("Send feedback email?")
                .setMessage("Let us know if something's not working as expected. You can even request new features!")
                .setNegativeButton("No", (dialog, which) -> {
                    hideAndDeferShow();
                    dialog.dismiss();
                })
                .setPositiveButton("Yes", (dialog, which) -> {
                    Utils.composeFeedbackEmail();
                    hideAndDeferShow();
                    dialog.dismiss();
                }).show();
    }

    private void onYesClicked() {
        new AlertDialog.Builder(getContext()).setTitle("Rate us on Google Play?")
                .setMessage("You're awesome! Thanks for trying out the app.")
                .setNegativeButton("Not now", (dialog, which) -> {
                    hideAndDeferShow();
                    dialog.dismiss();
                })
                .setPositiveButton("Sure", (dialog, which) -> {
                    Utils.openPlayStore();
                    hideAndNeverShow();
                    dialog.dismiss();
                }).show();
    }

    private boolean shouldShow() {
        boolean neverShow = sharedPreferences.getBoolean(PREF_NEVER_SHOW, false);
        if (!neverShow) {
            int launchCount = sharedPreferences.getInt(PREF_LAUNCH_COUNT, 0);
            int deferCount = sharedPreferences.getInt(PREF_DEFER_COUNT, 0);
            return MIN_LAUNCH_COUNT_THRESHOLD * Math.pow(2, deferCount) == launchCount;
        }
        return false;
    }

    private void hideAndDeferShow() {
        removeAllViews();
        dismissed = true;
        incrementDeferredCount();
    }

    private void hideAndNeverShow() {
        removeAllViews();
        dismissed = true;
        sharedPreferences.edit().putBoolean(PREF_NEVER_SHOW, true).apply();
    }

    private void incrementLaunchCount() {
        int launchCount = sharedPreferences.getInt(PREF_LAUNCH_COUNT, 0);
        sharedPreferences.edit().putInt(PREF_LAUNCH_COUNT, launchCount + 1).apply();
    }

    private void incrementDeferredCount() {
        int deferCount = sharedPreferences.getInt(PREF_DEFER_COUNT, 0);
        sharedPreferences.edit().putInt(PREF_DEFER_COUNT, deferCount + 1).apply();
    }
}
