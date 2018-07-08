package com.tanuj.nowplayinghistory.activities;

import android.Manifest;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import androidx.fragment.app.Fragment;

import com.github.paolorotolo.appintro.AppIntro2;
import com.github.paolorotolo.appintro.AppIntro2Fragment;
import com.github.paolorotolo.appintro.ISlidePolicy;
import com.tanuj.nowplayinghistory.R;
import com.tanuj.nowplayinghistory.Utils;
import com.tanuj.nowplayinghistory.fragments.CustomPolicySlide;

public class IntroActivity extends AppIntro2 {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        addSlide(AppIntro2Fragment.newInstance(
                "Never lose any song you hear",
                "Works automatically in the background",
                R.drawable.slide_1,
                Color.parseColor("#9C27B0")
        ));

        addSlide(AppIntro2Fragment.newInstance(
                "Intuitive actions",
                "Filter by time, swipe to favorite/delete",
                R.drawable.slide_2,
                Color.parseColor("#E91E63")
        ));

        addSlide(CustomPolicySlide.newInstance(
                "Enable location data",
                "If you want, you can save location of the song",
                R.drawable.slide_3,
                Color.parseColor("#009688"),
                new ISlidePolicy() {
                    @Override
                    public boolean isPolicyRespected() {
                        // This permission is optional
                        return true;
                    }

                    @Override
                    public void onUserIllegallyRequestedNextPage() {
                        // Do nothing
                    }
                }
        ));

        addSlide(CustomPolicySlide.newInstance(
                "Need notification access",
                "Uses notifications posted by your phone when it detects songs",
                R.drawable.slide_4,
                Color.parseColor("#795548"),
                new ISlidePolicy() {
                    @Override
                    public boolean isPolicyRespected() {
                        return Utils.isNotificationAccessGranted();
                    }

                    @Override
                    public void onUserIllegallyRequestedNextPage() {
                        Utils.LaunchNotificationAccessActivity();
                    }
                }
        ));

        // Ask for location permission on the third slide
        askForPermissions(new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION}, 3);

        // Hide Skip/Done button.
        showSkipButton(false);
    }

    @Override
    public void onDonePressed(Fragment currentFragment) {
        finish();
        Utils.setIsIntroRequired(false);
        startActivity(new Intent(this, MainActivity.class));
    }
}
