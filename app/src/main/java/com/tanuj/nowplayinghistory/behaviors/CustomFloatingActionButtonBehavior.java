package com.tanuj.nowplayinghistory.behaviors;

import android.content.Context;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import androidx.core.view.ViewCompat;
import androidx.interpolator.view.animation.FastOutSlowInInterpolator;
import android.util.AttributeSet;
import android.view.View;

import com.tanuj.nowplayinghistory.views.RateMeView;

public class CustomFloatingActionButtonBehavior extends FloatingActionButton.Behavior {

    public CustomFloatingActionButtonBehavior() {
        super();
    }

    public CustomFloatingActionButtonBehavior(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean layoutDependsOn(CoordinatorLayout parent, FloatingActionButton child, View dependency) {
        boolean result = dependency instanceof RateMeView;
        return result || super.layoutDependsOn(parent, child, dependency);
    }

    @Override
    public boolean onDependentViewChanged(CoordinatorLayout parent, FloatingActionButton child, View dependency) {
        if (dependency instanceof RateMeView) {
            updateFabTranslationForRateMeView(child, dependency);
        }
        return super.onDependentViewChanged(parent, child, dependency);
    }

    @Override
    public void onDependentViewRemoved(CoordinatorLayout parent, FloatingActionButton child, View dependency) {
        if (dependency instanceof RateMeView && child.getTranslationY() != 0.0F) {
            ViewCompat.animate(child).translationY(0.0F).scaleX(1.0F).scaleY(1.0F).alpha(1.0F).setInterpolator(new FastOutSlowInInterpolator()).start();
        }
        super.onDependentViewRemoved(parent, child, dependency);
    }

    private void updateFabTranslationForRateMeView(FloatingActionButton child, View dependency) {
        float translationY = Math.min(0, dependency.getTranslationY() - dependency.getHeight());
        child.setTranslationY(translationY);
    }
}
