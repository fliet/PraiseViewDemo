package com.example.praiseviewdemo;

import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;

import androidx.annotation.Nullable;

/**
 * @author wp
 * @date 2018/8/7.
 */

public class PraiseView extends View {
    private String praiseNum = "999";
    private int centerX;
    private int centerY;
    private Paint textPaint;
    /**
     * 文字上下滑动距离
     */
    private float textMoveDis;
    /**
     * 文字的透明度
     */
    private int textAlpha = 255;
    private ObjectAnimator textDisappearAnimator;
    private ObjectAnimator textAlphaAnimator;
    /**
     * 文字的上下间距
     */
    private float textFontSpacing;
    private Bitmap unPraiseBitmap;
    private Bitmap praiseBitmap;
    private Bitmap shiningBitmap;
    /**
     * 点赞标记，false：未点赞
     */
    private boolean praiseFlag = true;
    private Paint picPaint;
    private int picAlpha = 255;

    private static final float SCALE_MAX = 1f;
    private static final float SCALE_MIN = 0.9f;
    private float picScale;
    private ObjectAnimator imageScaleAnimator;
    private Paint circlePaint;
    private float circleRadius;
    private int circleAlpha;
    private ObjectAnimator circleAnimator;

    public PraiseView(Context context) {
        this(context, null);
    }

    public PraiseView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public PraiseView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        init();
    }

    private void init() {
        textPaint = new Paint();
        textPaint.setTextSize(100);
        textPaint.setColor(Color.GRAY);

        textFontSpacing = textPaint.getFontSpacing();

        textDisappearAnimator = ObjectAnimator
                .ofFloat(this, "textMoveDis", 0, textFontSpacing)
                .setDuration(200);

        textAlphaAnimator = ObjectAnimator
                .ofInt(this, "textAlpha", 255, 0)
                .setDuration(200);

        imageScaleAnimator = ObjectAnimator
                .ofFloat(this, "picScale", SCALE_MIN, SCALE_MAX)
                .setDuration(200);

        imageScaleAnimator = ObjectAnimator
                .ofFloat(this, "picScale", SCALE_MIN, SCALE_MAX)
                .setDuration(200);


        PropertyValuesHolder circleRadiusHolder = PropertyValuesHolder.ofFloat("circleRadius", 30, 45);
        PropertyValuesHolder circleAlphaHolder = PropertyValuesHolder.ofInt("circleAlpha", 120, 0);

        circleAnimator = ObjectAnimator
                .ofPropertyValuesHolder(this, circleRadiusHolder, circleAlphaHolder)
                .setDuration(350);
        circleAnimator.setInterpolator(new AccelerateDecelerateInterpolator());


        unPraiseBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.ic_messages_like_unselected);
        praiseBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.ic_messages_like_selected);
        shiningBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.ic_messages_like_selected_shining);

        picPaint = new Paint();
        circlePaint = new Paint();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        centerX = getWidth() / 2;
        centerY = getHeight() / 2;


        drawText(canvas);
        drawPic(canvas);
        drawCircle(canvas);
    }

    private void drawText(Canvas canvas) {

        //文字分区
        int textLen = praiseNum.length();
        int divideIndex = 0;
        int preIndex = 0;
        for (int i = textLen; i > 0; i--) {
            String textNum = praiseNum.substring(i - 1, i);
            if ("9".equals(textNum)) {
                preIndex++;
            } else {
                divideIndex = preIndex + 1;

                i = -1;
            }
        }

        String preStr = praiseNum.substring(0, textLen - divideIndex);
        String nextStr = praiseNum.substring(textLen - divideIndex, textLen);


        //绘制文字
        Paint.FontMetrics fontMetrics = textPaint.getFontMetrics();
        float textChangeHeight = (fontMetrics.ascent + fontMetrics.descent) / 2;
        //修正位置
        float textPosY = centerY - textChangeHeight - textMoveDis;
        float nextTextPosX = divideIndex == 0 ? centerX : centerX + textPaint.measureText(preStr);

        Rect rect = new Rect();
        textPaint.getTextBounds(preStr, 0, preStr.length(), rect);
        int offset = (rect.top + rect.bottom) / 2;

        textPaint.setAlpha(255);
        if (divideIndex != 0) {
            canvas.drawText(preStr, centerX, centerY - textChangeHeight, textPaint);
        }


        textPaint.setAlpha(textAlpha);
        canvas.drawText(divideIndex == 0 ? praiseNum : nextStr, nextTextPosX, textPosY, textPaint);

        //绘制替换文字
        textPaint.setAlpha(255 - textAlpha);
        float textReplaceDis = centerY + textFontSpacing - textChangeHeight - textMoveDis;
        canvas.drawText(String.valueOf(Integer.valueOf(divideIndex == 0 ? praiseNum : nextStr) + 1), nextTextPosX, textReplaceDis, textPaint);

    }

    private void drawPic(Canvas canvas) {
        int picMoveX = unPraiseBitmap.getWidth() / 2;
        int picMoveY = unPraiseBitmap.getHeight() / 2;

        picPaint.setAlpha(picAlpha);
        canvas.save();

        canvas.scale(picScale == 0 ? 1 : picScale, picScale == 0 ? 1 : picScale, centerX , centerY);
        canvas.drawBitmap(unPraiseBitmap, centerX - picMoveX - 50, centerY - picMoveY, picPaint);
        canvas.restore();

        picPaint.setAlpha(255 - picAlpha);

        canvas.save();
        canvas.scale(picScale, picScale, centerX , centerY);
        canvas.drawBitmap(praiseBitmap, centerX - picMoveX - 50, centerY - picMoveY, picPaint);
        canvas.restore();

        canvas.drawBitmap(shiningBitmap, centerX - picMoveX - 45, centerY - picMoveY * 2+5, picPaint);

    }

    private void drawCircle(Canvas canvas) {
        circlePaint.setColor(Color.RED);
        circlePaint.setStyle(Paint.Style.STROKE);
        circlePaint.setStrokeWidth(4);
        circlePaint.setAlpha(circleAlpha);
        canvas.drawCircle(centerX - 50, centerY, circleRadius, circlePaint);
    }

    public float getTextMoveDis() {
        return textMoveDis;
    }

    public void setTextMoveDis(float textMoveDis) {
        this.textMoveDis = textMoveDis;

        invalidate();
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();

    }

    public int getTextAlpha() {
        return textAlpha;
    }

    public void setTextAlpha(int textAlpha) {
        this.textAlpha = textAlpha;

        invalidate();
    }

    public float getPicScale() {
        return picScale;
    }

    public void setPicScale(float picScale) {
        this.picScale = picScale;

        invalidate();
    }

    public float getCircleRadius() {
        return circleRadius;
    }

    public void setCircleRadius(float circleRadius) {
        this.circleRadius = circleRadius;

        invalidate();
    }

    public int getCircleAlpha() {
        return circleAlpha;
    }

    public void setCircleAlpha(int circleAlpha) {
        this.circleAlpha = circleAlpha;

        invalidate();
    }

    public void reset(String number) {
        praiseNum = number;

        if (praiseFlag) {
            picAlpha = 0;

            textDisappearAnimator.start();
            textAlphaAnimator.start();
            imageScaleAnimator.start();
            circleAnimator.start();

            praiseFlag = false;
        } else {
            picAlpha = 255;
            textDisappearAnimator.reverse();
            textAlphaAnimator.reverse();
            imageScaleAnimator.start();

            praiseFlag = true;
        }
    }
}
