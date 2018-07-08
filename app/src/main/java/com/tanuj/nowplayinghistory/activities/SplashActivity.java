package com.tanuj.nowplayinghistory.activities;

import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;

import com.tanuj.nowplayinghistory.Utils;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onStart() {
        super.onStart();

        Utils.executeAsync(() -> {
            if (Utils.isIntroRequired()) {
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }, () -> {
            if (Utils.isIntroRequired()) {
                startActivity(new Intent(SplashActivity.this, IntroActivity.class));
            } else {
                startActivity(new Intent(SplashActivity.this, MainActivity.class));
            }
            finish();
        });
    }
}
