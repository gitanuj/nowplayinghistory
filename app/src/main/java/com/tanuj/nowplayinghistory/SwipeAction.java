package com.tanuj.nowplayinghistory;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.text.TextPaint;
import android.view.View;

public class SwipeAction {

    public enum Dir {
        LEFT,
        RIGHT
    }

    private static final float ACTION_TEXT_PADDING = Utils.dpToPx(16);
    private static final float ACTION_TEXT_SIZE = Utils.dpToPx(13);

    private Dir swipeDir;
    private String actionText;

    private Paint backgroundPaint = new Paint();
    private Paint textPaint = new TextPaint();
    private Rect textBounds = new Rect();

    public SwipeAction(Dir swipeDir, String actionText) {
        this.swipeDir = swipeDir;
        this.actionText = actionText;
        init();
    }

    private void init() {
        textPaint.setTextSize(ACTION_TEXT_SIZE);
        textPaint.setColor(Color.WHITE);
        textPaint.setTypeface(Typeface.DEFAULT_BOLD);
        textPaint.getTextBounds(actionText, 0, actionText.length(), textBounds);

        if (swipeDir == Dir.LEFT) {
            textPaint.setTextAlign(Paint.Align.LEFT);
            backgroundPaint.setColor(Color.RED);
        } else if (swipeDir == Dir.RIGHT) {
            textPaint.setTextAlign(Paint.Align.RIGHT);
            backgroundPaint.setColor(Color.GREEN);
        }
    }

    public void draw(Canvas c, View itemView, float dX) {
        if (swipeDir == Dir.LEFT) {
            drawLeftSwipeAction(c, itemView, dX);
        } else if (swipeDir == Dir.RIGHT) {
            drawRightSwipeAction(c, itemView, dX);
        }
    }

    private void drawLeftSwipeAction(Canvas c, View itemView, float dX) {
        float x = itemView.getRight() + dX + ACTION_TEXT_PADDING;
        float y = itemView.getTop() + itemView.getHeight() / 2 - textBounds.exactCenterY();
        c.drawRect(itemView.getRight() + dX, itemView.getTop(), itemView.getRight(), itemView.getBottom(), backgroundPaint);
        c.drawText(actionText, x, y, textPaint);
    }

    private void drawRightSwipeAction(Canvas c, View itemView, float dX) {
        float x = itemView.getLeft() + dX - ACTION_TEXT_PADDING;
        float y = itemView.getTop() + itemView.getHeight() / 2 - textBounds.exactCenterY();
        c.drawRect(itemView.getLeft(), itemView.getTop(), itemView.getLeft() + dX, itemView.getBottom(), backgroundPaint);
        c.drawText(actionText, x, y, textPaint);
    }
}
