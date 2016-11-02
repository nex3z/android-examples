package com.nex3z.examples.shakedetector;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;

public class ExpandableCircleView extends View {
    private static final String LOG_TAG = ExpandableCircleView.class.getSimpleName();

    private static final int DEFAULT_OUTER_COLOR = Color.BLACK;
    private static final int DEFAULT_INNER_COLOR = Color.BLUE;
    private static final int DEFAULT_HEIGHT = 200;
    private static final int DEFAULT_WIDTH = 200;
    private static final int DEFAULT_EXPAND_ANIMATION_DURATION = 100;
    private static final boolean DEFAULT_SHOW_PROGRESS = false;
    private static final float DEFAULT_PROGRESS_TEXT_SIZE_DP = 32;
    private static final int DEFAULT_PROGRESS_TEXT_COLOR = Color.BLACK;

    private static final int DEFAULT_MAX = 100;

    private static final String SUPER_STATE_KEY = "super_state";
    private static final String OUTER_COLOR_KEY = "outer_color";
    private static final String INNER_COLOR_KEY = "inner_color";

    private int mOuterColor;
    private int mInnerColor;
    private int mExpandAnimationDuration;
    private boolean mShowProgressText;
    private float mProgressTextSize;
    private int mProgressTextColor;
    private String mProgressTextSuffix;

    private int mMax;
    private int mProgress = 0;

    private Paint mOuterPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private Paint mInnerPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private Paint mProgressTextPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private Rect mProgressTextRect = new Rect();
    private ObjectAnimator mExpandAnimator;

    public ExpandableCircleView(Context context) {
        super(context);
        init();
    }

    public ExpandableCircleView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
        init();
    }

    public ExpandableCircleView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        TypedArray a = context.getTheme().obtainStyledAttributes(
                attrs,
                R.styleable.ExpandableCircleView,
                0, 0);

        try {
            mOuterColor = a.getColor(R.styleable.ExpandableCircleView_outerColor,
                    DEFAULT_OUTER_COLOR);
            mInnerColor = a.getColor(R.styleable.ExpandableCircleView_innerColor,
                    DEFAULT_INNER_COLOR);

            mMax = a.getInt(R.styleable.ExpandableCircleView_max, DEFAULT_MAX);
            mExpandAnimationDuration = a.getInt(
                    R.styleable.ExpandableCircleView_expandAnimationDuration,
                    DEFAULT_EXPAND_ANIMATION_DURATION);
            mShowProgressText = a.getBoolean(R.styleable.ExpandableCircleView_showProgressText,
                    DEFAULT_SHOW_PROGRESS);
            mProgressTextSize = a.getDimensionPixelSize(
                    R.styleable.ExpandableCircleView_progressTextSize,
                    (int)dpToPx(DEFAULT_PROGRESS_TEXT_SIZE_DP));
            mProgressTextColor = a.getColor(R.styleable.ExpandableCircleView_progressTextColor,
                    DEFAULT_PROGRESS_TEXT_COLOR);
            mProgressTextSuffix = a.getString(R.styleable.ExpandableCircleView_progressTextSuffix);
        } finally {
            a.recycle();
        }

        init();
    }

    private void init() {
        mOuterPaint.setColor(mOuterColor);
        mOuterPaint.setStyle(Paint.Style.STROKE);

        mInnerPaint.setColor(mInnerColor);

        mProgressTextPaint.setColor(mProgressTextColor);
        mProgressTextPaint.setTextSize(mProgressTextSize);

        mExpandAnimator = ObjectAnimator.ofInt(this, "Progress", 0);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);

        if (widthMode == MeasureSpec.AT_MOST && heightMode == MeasureSpec.AT_MOST) {
            setMeasuredDimension(DEFAULT_WIDTH, DEFAULT_HEIGHT);
        } else if (widthMode == MeasureSpec.AT_MOST) {
            widthSize = widthSize > heightSize ? heightSize : widthSize;
            setMeasuredDimension(widthSize, heightSize);
        } else if (heightMode == MeasureSpec.AT_MOST) {
            heightSize = heightSize > widthSize ? widthSize : heightSize;
            setMeasuredDimension(widthSize, heightSize);
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        int paddingLeft = getPaddingLeft();
        int paddingRight = getPaddingRight();
        int paddingTop = getPaddingTop();
        int paddingBottom = getPaddingBottom();

        int width = getWidth() - paddingLeft - paddingRight;
        int height = getHeight() - paddingTop - paddingBottom;

        float borderRadius = (width < height ? width : height) / 2;
        float cx = paddingLeft + borderRadius;
        float cy = paddingTop + borderRadius;

        float proportion = (float)mProgress / mMax;
        canvas.drawCircle(cx, cy, borderRadius, mOuterPaint);
        canvas.drawCircle(cx, cy, borderRadius * proportion, mInnerPaint);

        if (mShowProgressText) {
            String progress = String.valueOf(mProgress);
            if (mProgressTextSuffix != null) {
                progress += mProgressTextSuffix;
            }
            mProgressTextPaint.getTextBounds(progress, 0, progress.length(), mProgressTextRect);
            canvas.drawText(progress, cx - mProgressTextRect.width() / 2,
                    cy + mProgressTextRect.height() / 2, mProgressTextPaint);
        }
    }

    @Override
    protected Parcelable onSaveInstanceState() {
        Bundle bundle = new Bundle();
        bundle.putParcelable(SUPER_STATE_KEY, super.onSaveInstanceState());
        bundle.putInt(OUTER_COLOR_KEY, mOuterColor);
        bundle.putInt(INNER_COLOR_KEY, mInnerColor);
        return bundle;
    }

    @Override
    protected void onRestoreInstanceState(Parcelable state) {
        if (state instanceof Bundle) {
            Bundle bundle = (Bundle) state;
            int outerColor = bundle.getInt(OUTER_COLOR_KEY);
            setOuterColor(outerColor);
            int innerColor = bundle.getInt(INNER_COLOR_KEY);
            setInnerColor(innerColor);
            state = bundle.getParcelable(SUPER_STATE_KEY);
            invalidate();
        }
        super.onRestoreInstanceState(state);
    }

    /**
     * Gets the current progress.
     *
     * @return the current progress, between 0 and getMax()
     */
    public int getProgress() {
        return mProgress;
    }

    /**
     * Sets the current progress to the specified value and immediately update the proportion of
     * the inner circle.
     *
     * @param progress the new progress value, between 0 and getMax()
     */
    public void setProgress(int progress) {
        if (progress > getMax()) {
            mProgress = getMax();
        } else {
            mProgress = progress;
        }
        invalidate();
    }

    /**
     * Sets the current progress to the specified value, optionally animating the proportion of the
     * inner circle between the current and target values.
     *
     * @param progress the new progress value, between 0 and getMax()
     * @param animate true to animate between the current and target values or false to not animate
     */
    public void setProgress(int progress, boolean animate) {
        if (!animate) {
            setProgress(progress);
        } else {
            int target = progress > getMax() ? getMax() : progress;
            if (mExpandAnimator.isRunning()) {
                mExpandAnimator.cancel();
            }
            mExpandAnimator.setIntValues(target);
            mExpandAnimator.setDuration(mExpandAnimationDuration).start();
        }
    }

    /**
     * Return the upper limit of this progress bar's range.
     *
     * @return the upper limit of this progress bar's range
     */
    public int getMax() {
        return mMax;
    }

    /**
     * Set the range of the progress bar to 0...max.
     *
     * @param max the upper range of this progress bar
     */
    public void setMax(int max) {
        mMax = max;
        invalidate();
    }

    /**
     * Gets the color of the progress text.
     *
     * @return the color of the progress text
     */
    public int getProgressTextColor() {
        return mProgressTextColor;
    }

    /**
     * Sets color of the progress text.
     *
     * @param progressTextColor color of the progress text
     */
    public void setProgressTextColor(int progressTextColor) {
        mProgressTextColor = progressTextColor;
        mProgressTextPaint.setColor(mProgressTextColor);
        invalidate();
    }

    /**
     * Gets the size (in pixels) of progress text.
     *
     * @return the size (in pixels) of progress text
     */
    public float getProgressTextSize() {
        return mProgressTextSize;
    }

    /**
     * Sets the size (in pixels) of progress text.
     *
     * @param progressTextSize the size (in pixels) of progress text
     */
    public void setProgressTextSize(float progressTextSize) {
        mProgressTextSize = progressTextSize;
        mProgressTextPaint.setTextSize(mProgressTextSize);
        invalidate();
    }

    /**
     * Returns whether the progress text is shown in the center of the circle. The default is false.
     *
     * @return  whether the progress text is shown in the center of the circle
     */
    public boolean getShowProgressText() {
        return mShowProgressText;
    }

    /**
     * Sets whether the progress text is shown in the center of the circle.
     *
     * @param showProgressText set true to show progress text, false to hide the text
     */
    public void setShowProgressText(boolean showProgressText) {
        mShowProgressText = showProgressText;
        invalidate();
    }

    /**
     * Gets the suffix string to be appended to the progress text. Default is null which appends
     * nothing.
     *
     * @return the suffix string to be appended to the progress text
     */
    public String getProgressTextSuffix() {
        return mProgressTextSuffix;
    }

    /**
     * Sets the suffix string to be appended to the progress text.
     *
     * @param progressTextSuffix the suffix string to be appended to the progress text
     */
    public void setProgressTextSuffix(String progressTextSuffix) {
        mProgressTextSuffix = progressTextSuffix;
        invalidate();
    }

    /**
     * Gets the color of outer circle.
     *
     * @return the color of the outer circle
     */
    public int getOuterColor() {
        return mOuterColor;
    }

    /**
     * Changes the color of outer circle.
     *
     * @param color the color of the outer circle
     */
    public void setOuterColor(int color) {
        mOuterColor = color;
        mOuterPaint.setColor(mOuterColor);
    }

    /**
     * Gets the color of inner circle.
     *
     * @return the color of the inner circle
     */
    public int getInnerColor() {
        return mOuterColor;
    }

    /**
     * Changes the color of inner circle.
     *
     * @param color the color of the inner circle.
     */
    public void setInnerColor(int color) {
        mInnerColor = color;
        mInnerPaint.setColor(mInnerColor);
    }

    /**
     * Get the animation duration in millisecond for the inner circle to expand.
     *
     * @return the animation duration in millisecond for the inner circle to expand.
     */
    public int getExpandAnimationDuration() {
        return mExpandAnimationDuration;
    }

    /**
     * Changes the animation duration in millisecond for the inner circle to expand.
     *
     * @param duration the animation duration in millisecond for the inner circle to expand.
     */
    public void setExpandAnimationDuration(int duration) {
        mExpandAnimationDuration = duration;
    }

    private float dpToPx(float dp){
        return TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP, dp, getResources().getDisplayMetrics());
    }

}
