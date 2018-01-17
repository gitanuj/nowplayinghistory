package com.tanuj.nowplayinghistory.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tanuj.nowplayinghistory.R;
import com.tanuj.nowplayinghistory.Utils;

import agency.tango.materialintroscreen.SlideFragment;

public class NotificationAccessSlide extends SlideFragment {

    private static final String EXTRA_BACKGROUND_COLOR = "background_color";
    private static final String EXTRA_BUTTONS_COLOR = "buttons_color";

    private int backgroundColor;
    private int buttonsColor;

    public static NotificationAccessSlide newInstance(int backgroundColor, int buttonsColor) {
        NotificationAccessSlide slide = new NotificationAccessSlide();

        Bundle args = new Bundle();
        args.putInt(EXTRA_BACKGROUND_COLOR, backgroundColor);
        args.putInt(EXTRA_BUTTONS_COLOR, buttonsColor);
        slide.setArguments(args);

        return slide;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            backgroundColor = getArguments().getInt(EXTRA_BACKGROUND_COLOR);
            buttonsColor = getArguments().getInt(EXTRA_BUTTONS_COLOR);
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Bundle bundle = new Bundle();
        bundle.putInt("image", R.drawable.slide_3);
        bundle.putString("title", "Need notification access");
        bundle.putString("description", "Uses notifications posted by your phone when it detects songs");
        setArguments(bundle);
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onResume() {
        super.onResume();

        // Fake request permissions to update "Grant Permissions" button
        getActivity().onRequestPermissionsResult(0, null, null);
    }

    @Override
    public void askForPermissions() {
        Utils.LaunchNotificationAccessActivity();
    }

    @Override
    public boolean hasAnyPermissionsToGrant() {
        return hasNeededPermissionsToGrant();
    }

    @Override
    public boolean hasNeededPermissionsToGrant() {
        return !Utils.isNotificationAccessGranted();
    }

    @Override
    public int backgroundColor() {
        return backgroundColor;
    }

    @Override
    public int buttonsColor() {
        return buttonsColor;
    }
}
