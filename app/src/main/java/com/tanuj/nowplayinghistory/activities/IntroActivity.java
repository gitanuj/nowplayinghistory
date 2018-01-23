package com.tanuj.nowplayinghistory.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.tanuj.nowplayinghistory.R;
import com.tanuj.nowplayinghistory.Utils;
import com.tanuj.nowplayinghistory.fragments.NotificationAccessSlide;

import agency.tango.materialintroscreen.MaterialIntroActivity;
import agency.tango.materialintroscreen.SlideFragmentBuilder;

public class IntroActivity extends MaterialIntroActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getBackButtonTranslationWrapper()
                .setEnterTranslation(View::setAlpha);

        addSlide(new SlideFragmentBuilder()
                .backgroundColor(R.color.first_slide_background)
                .buttonsColor(R.color.first_slide_buttons)
                .image(R.drawable.slide_1)
                .title("Never lose any song you heard")
                .description("Works automatically in the background")
                .build());

        addSlide(new SlideFragmentBuilder()
                .backgroundColor(R.color.second_slide_background)
                .buttonsColor(R.color.second_slide_buttons)
                .image(R.drawable.slide_2)
                .title("Intuitive actions")
                .description("Filter by time, swipe to favorite/delete")
                .build());

        addSlide(new SlideFragmentBuilder()
                .backgroundColor(R.color.third_slide_background)
                .buttonsColor(R.color.third_slide_buttons)
                .image(R.drawable.slide_3)
                .possiblePermissions(new String[]{"android.permission.ACCESS_COARSE_LOCATION", "android.permission.ACCESS_FINE_LOCATION"})
                .title("Enable location data")
                .description("If you want, you can save location of the song")
                .build());

        addSlide(NotificationAccessSlide.newInstance(R.color.fourth_slide_background, R.color.fourth_slide_buttons));
    }

    @Override
    public void onFinish() {
        Utils.setIsIntroRequired(false);
        startActivity(new Intent(this, MainActivity.class));
    }
}
