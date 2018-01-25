package com.tanuj.nowplayinghistory.fragments;

import android.os.Bundle;

import com.tanuj.nowplayinghistory.R;
import com.tanuj.nowplayinghistory.Utils;

import agency.tango.materialintroscreen.SlideFragment;

public class NotificationAccessSlide extends SlideFragment {

    private static final String BACKGROUND_COLOR = "background_color";
    private static final String BUTTONS_COLOR = "buttons_color";
    private static final String TITLE = "title";
    private static final String DESCRIPTION = "description";
    private static final String IMAGE = "image";

    public static NotificationAccessSlide newInstance(int backgroundColor, int buttonsColor) {
        NotificationAccessSlide slide = new NotificationAccessSlide();

        Bundle args = new Bundle();
        args.putInt(BACKGROUND_COLOR, backgroundColor);
        args.putInt(BUTTONS_COLOR, buttonsColor);
        args.putInt(IMAGE, R.drawable.slide_4);
        args.putString(TITLE, "Need notification access");
        args.putString(DESCRIPTION, "Uses notifications posted by your phone when it detects songs");
        slide.setArguments(args);

        return slide;
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
}
