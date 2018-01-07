package com.tanuj.nowplayinghistory.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tanuj.nowplayinghistory.R;
import com.tanuj.nowplayinghistory.Utils;

import agency.tango.materialintroscreen.SlideFragment;

public class PermissionSlide extends SlideFragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Bundle bundle = new Bundle();
        bundle.putInt("image", R.drawable.notifications_screen);
        bundle.putString("title", "Need notification access");
        bundle.putString("description", "Uses notifications posted by your phone when it detects songs");
        setArguments(bundle);
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onResume() {
        super.onResume();

        if (Utils.isNotificationServiceEnabled()) {
            Bundle bundle = new Bundle();
            bundle.putString("title", "You're all set!");
            bundle.putString("description", "Relies on notifications posted by your phone");
            setArguments(bundle);
            initializeView();
        }
    }

    @Override
    public int backgroundColor() {
        return R.color.fourth_slide_background;
    }

    @Override
    public int buttonsColor() {
        return R.color.fourth_slide_buttons;
    }

    @Override
    public boolean canMoveFurther() {
        return Utils.isNotificationServiceEnabled();
    }

    @Override
    public String cantMoveFurtherErrorMessage() {
        return "Please grant notifications access";
    }
}
