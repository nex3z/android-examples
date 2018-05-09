package com.nex3z.examples.camera2api;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.TextureView;

public class AutoFitTextureView extends TextureView {
    private static final String LOG_TAG = AutoFitTextureView.class.getSimpleName();

    private int mRatioWidth = 0;
    private int mRatioHeight = 0;

    public AutoFitTextureView(final Context context) {
        this(context, null);
    }

    public AutoFitTextureView(final Context context, final AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public AutoFitTextureView(final Context context, final AttributeSet attrs, final int defStyle) {
        super(context, attrs, defStyle);
    }

    public void setAspectRatio(final int width, final int height) {
        Log.v(LOG_TAG, "setAspectRatio(): width = " + width + ", height = " + height);
        if (width < 0 || height < 0) {
            throw new IllegalArgumentException("Size cannot be negative.");
        }
        mRatioWidth = width;
        mRatioHeight = height;
        requestLayout();
    }

    @Override
    protected void onMeasure(final int widthMeasureSpec, final int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        final int width = MeasureSpec.getSize(widthMeasureSpec);
        final int height = MeasureSpec.getSize(heightMeasureSpec);
        Log.v(LOG_TAG, "onMeasure(): width = " + width + ", height = " + height);
        if (0 == mRatioWidth || 0 == mRatioHeight) {
            Log.v(LOG_TAG, "onMeasure(): 0 width = " + width + ", height = " + height);
            setMeasuredDimension(width, height);
        } else {
            if (width < height * mRatioWidth / mRatioHeight) {
                Log.v(LOG_TAG, "onMeasure(): 1 width = " + width + ", height = " + ( width * mRatioHeight / mRatioWidth));
                setMeasuredDimension(width, width * mRatioHeight / mRatioWidth);
            } else {
                Log.v(LOG_TAG, "onMeasure(): 2 width = " + (height * mRatioWidth / mRatioHeight) + ", height = " + height);
                setMeasuredDimension(height * mRatioWidth / mRatioHeight, height);
            }
        }
    }

}
