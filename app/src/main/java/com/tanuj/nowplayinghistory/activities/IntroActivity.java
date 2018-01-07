package com.tanuj.nowplayinghistory.activities;

import android.content.Intent;
import android.os.Bundle;

import com.tanuj.nowplayinghistory.R;
import com.tanuj.nowplayinghistory.Utils;
import com.tanuj.nowplayinghistory.fragments.PermissionSlide;

import agency.tango.materialintroscreen.MaterialIntroActivity;
import agency.tango.materialintroscreen.MessageButtonBehaviour;
import agency.tango.materialintroscreen.SlideFragmentBuilder;

public class IntroActivity extends MaterialIntroActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getBackButtonTranslationWrapper()
                .setEnterTranslation((view, percentage) -> view.setAlpha(percentage));

        addSlide(new SlideFragmentBuilder()
                .backgroundColor(R.color.first_slide_background)
                .buttonsColor(R.color.first_slide_buttons)
                .image(R.drawable.recents_screen)
                .title("Never lose any song you heard")
                .description("Works automatically in the background")
                .build());

        addSlide(new SlideFragmentBuilder()
                .backgroundColor(R.color.second_slide_background)
                .buttonsColor(R.color.second_slide_buttons)
                .image(R.drawable.tap_to_play_screen)
                .title("Just tap to play")
                .description("Choose your favorite app")
                .build());

        addSlide(new SlideFragmentBuilder()
                .backgroundColor(R.color.third_slide_background)
                .buttonsColor(R.color.third_slide_buttons)
                .image(R.drawable.add_fav_screen)
                .title("Swipe right to favorite")
                .description("Or swipe left to delete")
                .build());

        if (Utils.isNotificationServiceEnabled()) {
            addSlide(new PermissionSlide());
        } else {
            addSlide(new PermissionSlide(), new MessageButtonBehaviour(v -> Utils.LaunchNotificationAccessActivity(), "Grant permission"));
        }
    }

    @Override
    public void onFinish() {
        Utils.setIsIntroRequired(false);
        startActivity(new Intent(this, MainActivity.class));
    }
}
